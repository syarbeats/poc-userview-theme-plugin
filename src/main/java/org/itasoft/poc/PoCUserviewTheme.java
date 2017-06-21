package org.itasoft.poc;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.joget.apps.app.service.AppUtil;
import org.joget.apps.app.service.MobileUtil;
import org.joget.apps.userview.model.UserviewTheme;
import org.joget.workflow.util.WorkflowUtil;


public class PoCUserviewTheme extends UserviewTheme {

	public String getName()
	  {
	    return "PoC Userview Theme";
	  }
	  
	  public String getVersion()
	  {
	    return "5.0.0";
	  }
	  
	  public String getDescription()
	  {
	    return "PoC Userview Theme plugin to add menu in header";
	  }
	  
	  public String getLabel()
	  {
	    return "PoC Userview Theme";
	  }
	  
	  public String getClassName()
	  {
	    return getClass().getName();
	  }
	  
	  public String getPropertyOptions()
	  {
	    return AppUtil.readPluginResource(getClass().getName(), "/properties/userview/PoCUserviewTheme.json", null, true, null);
	  }
	  
	  public String getCss()
	  {
	    String css = "</style>\n";
	    css = css + "<link href=\"" + AppUtil.getRequestContextPath() + "/wro/bootstrap_theme_commons.css" + "\" rel=\"stylesheet\" />\n";
	    css = css + "<script src=\"" + AppUtil.getRequestContextPath() + "/js/bootstrap/js/bootstrap.min.js\"></script>\n";
	    css = css + "<link href=\"" + AppUtil.getRequestContextPath() + "/wro/bootstrap_theme.min.css" + "\" rel=\"stylesheet\" />\n";
	    css = css + "<script src=\"" + AppUtil.getRequestContextPath() + "/wro/bootstrap_theme.min.js\"></script>\n";
	    
	    String customCss = getPropertyString("css");
	    css = css + "\n<style type='text/css'>" + customCss;
	    
	    return css;
	  }
	  
	  public String getJavascript()
	  {
	    String js = getPropertyString("js");
	    
	    return js;
	  }
	  
	  public String getHeader()
	  {
	    String url = getRequestParameterString("contextPath");
	    if (getRequestParameterString("isPreview").equalsIgnoreCase("true"))
	    {
	      url = url + "/web/console/app/" + getRequestParameterString("appId") + "/" + getRequestParameterString("appVersion") + "/userview/builderPreview/" + getUserview().getPropertyString("id") + "/";
	    }
	    else
	    {
	      String prefix = "/web/userview/";
	      if (getRequestParameterString("embed").equalsIgnoreCase("true")) {
	        prefix = "/web/embed/userview/";
	      }
	      url = url + prefix + getRequestParameterString("appId") + "/" + getUserview().getPropertyString("id") + "/" + (getRequestParameterString("key") != null ? URLEncoder.encode(getRequestParameterString("key")) : "") + "/";
	    }
	    String header = "<div class=\"container\"><div class=\"navbar navbar-inverse navbar-fixed-top\"><div class=\"navbar-inner\"><div class=\"container\">";
	    header = header + "<a class=\"brand\" href=\"" + url + getUserview().getPropertyString("homeMenuId") + "\">" + getUserview().getPropertyString("name") + "</a>";
	    header = header + "<ul class=\"nav\"><li><a href=\"" + url + getUserview().getPropertyString("homeMenuId") + "\"><i class=\"icon-home\"></i> " + getPropertyString("homeLinkLabel") + "</a></li>";
	    
	    
	    
	    if (!MobileUtil.isMobileView())
	    {
	    	 HashMap<Integer, String> menu = new HashMap<Integer, String>();
		     menu.put(0, "shortcut");
		     menu.put(1, "shortcut2");
		     menu.put(2, "shortcut3");
		    
		     HashMap<Integer, String> linklabel = new HashMap<Integer, String>();
		     linklabel.put(0, "shortcutLinkLabel");
		     linklabel.put(1, "shortcutLinkLabel2");
		     linklabel.put(2, "shortcutLinkLabel3");
	    	
	    	 Set set = menu.entrySet();
	         Iterator iterator = set.iterator();
	         Set setLinkLabel = linklabel.entrySet();
	         Iterator iteratorLabel = setLinkLabel.iterator();
	          
	         while(iterator.hasNext() && iteratorLabel.hasNext()) {
	        	         
	        	  @SuppressWarnings("rawtypes")
				  Map.Entry menuShortcut  = (Map.Entry)iterator.next();
	            
	        	  @SuppressWarnings("rawtypes")
				  Map.Entry menuLinkLabel = (Map.Entry)iteratorLabel.next();
	            
	              Object[] shortcut = (Object[])getProperty((String) menuShortcut.getValue());
		  	      if ((shortcut != null) && (shortcut.length > 0))
		  	      {
		  	        header = header + "<li class=\"dropdown\"><a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\"><i class=\"icon-link\"></i> " + getPropertyString((String) menuLinkLabel.getValue()) + " <b class=\"caret\"></b></a><ul class=\"dropdown-menu\">";
		  	        for (Object o : shortcut)
		  	        {
		  	          Map link = (HashMap)o;
		  	          String href = link.get("href").toString();
		  	          String label = link.get("label").toString();
		  	          String target = link.get("target") == null ? "" : link.get("target").toString();
		  	          if ("divider".equalsIgnoreCase(label))
		  	          {
		  	            header = header + "<li class=\"divider\"></li>";
		  	          }
		  	          else if (href.isEmpty())
		  	          {
		  	            header = header + "<li class=\"nav-header\">" + label + "</li>";
		  	          }
		  	          else
		  	          {
		  	            if (!href.contains("/")) {
		  	              href = url + href;
		  	            }
		  	            header = header + "<li><a href=\"" + href + "\" target=\"" + target + "\">" + label + "</a></li>";
		  	          }
		  	        }
		  	        header = header + "</ul></li>";
		  	      }
	         }
	      
	      header = header + "</ul>";
	      if (WorkflowUtil.isCurrentUserAnonymous())
	      {
	        header = header + "<a id=\"loginButton\" href=\"" + getRequestParameterString("contextPath") + "/web/ulogin/" + getRequestParameterString("appId") + "/" + getUserview().getPropertyString("id") + "/\" class=\"btn pull-right\">" + getPropertyString("loginLinkLabel") + "</a>";
	      }
	      else
	      {
	        String profileLink = getPropertyString("editUserProfileLink");
	        if ((profileLink != null) && (!profileLink.isEmpty()))
	        {
	          if (!profileLink.contains("/")) {
	            profileLink = url + profileLink;
	          }
	          header = header + "<a id=\"accountButton\" href=\"" + profileLink + "\" class=\"btn pull-right\">" + getPropertyString("editUserProfileLinkLabel") + "</a> ";
	        }
	        String logoutLinkLabel = getPropertyString("logoutLinkLabel");
	        if ((logoutLinkLabel == null) || (logoutLinkLabel.isEmpty())) {
	          logoutLinkLabel = "Logout";
	        }
	        header = header + "<a id=\"logoutButton\" href=\"" + getRequestParameterString("contextPath") + "/j_spring_security_logout\" class=\"btn pull-right\">" + logoutLinkLabel + "</a>";
	      }
	    }
	    header = header + "</div></div></div>";
	    if (!MobileUtil.isMobileView())
	    {
	      String banner = getPropertyString("homeAttractBanner");
	      if ((!banner.isEmpty()) && ((getRequestParameter("menuId") == null) || (getUserview().getPropertyString("homeMenuId").equals(getRequestParameter("menuId"))))) {
	        header = header + "<div class=\"row\"><div class=\"span12\">" + banner + "</div></div>";
	      }
	    }
	    header = header + "</div>";
	    
	    return header;
	  }
	  
	  public String getFooter()
	  {
	    String footer = "<div class=\"container\"><div class=\"row\"><div class=\"span12\"><p id =\"footer-content\">";
	    footer = footer + getUserview().getPropertyString("footerMessage");
	    footer = footer + "</p></div></div></div>\n<style>";
	    footer = footer + ".subform-container, .subform-section { background: #F9FFFF }\n";
	    footer = footer + ".dataList select { width: auto }\n";
	    footer = footer + ".dataList td, .dataList tr { padding: 8px 4px !important }\n";
	    footer = footer + ".dataList td a { background: #CDD; padding: 5px; border-radius: 10px; color: #005580; min-width: 50px; text-align: center; display: inline-block; }\n";
	    footer = footer + "</style>";
	    return footer;
	  }
	  
	  public String getPageTop()
	  {
	    if (((getRequestParameter("menuId") != null) || (getRequestParameterString("isPreview").equalsIgnoreCase("true"))) && (!getRequestParameterString("embed").equalsIgnoreCase("true"))) {
	      return "<div class=\"container\"><div class=\"row\"><div class=\"span3\">";
	    }
	    if (getRequestParameterString("embed").equalsIgnoreCase("true")) {
	      return "";
	    }
	    return "<div class=\"container\"><div class=\"row\"><div class=\"span12\">";
	  }
	  
	  public String getPageBottom()
	  {
	    if (getRequestParameterString("embed").equalsIgnoreCase("true")) {
	      return "";
	    }
	    return "</div></div></div>";
	  }
	  
	  public String getBeforeContent()
	  {
	    if (((getRequestParameter("menuId") != null) || (getRequestParameterString("isPreview").equalsIgnoreCase("true"))) && (!getRequestParameterString("embed").equalsIgnoreCase("true"))) {
	      return "</div></div><div class=\"span9\"><div id=\"content\" class=\"main-content\">";
	    }
	    return "";
	  }

}
