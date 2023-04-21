package org.openmoney.page;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.openmoney.struct.*;
import org.openmoney.util.*;
import org.openmoney.html.*;

/**
 * Interface definition and basic method functionality for the Page hierarchy
 * @author Reuben Firmin (openmoney@digitalsheep.com)
 */
public abstract class Page {

    private Request _request = null;

    private PropertyResourceBundle _resources = null;

    public Page(Request request) {
        _request = request;
    }

    /**
 	 * Builds the html part of the response and returns it 
	 */
    public final String getResponse() throws java.io.IOException, java.sql.SQLException {
        return getContent();
    }

    /**
	 * Return the actual contents of the page
	 */
    protected abstract String getContent() throws java.io.IOException, java.sql.SQLException;

    /**
	 * Return the resource bundle appropriate to this page/user
	 */
    protected PropertyResourceBundle getResources() {
        if (_resources == null) {
            try {
                _resources = (PropertyResourceBundle) ResourceBundle.getBundle(_request.getPageName());
            } catch (MissingResourceException e) {
                String classPath = System.getProperty("java.class.path", ".");
                Log.debug("Page.getResources", "Missing resource: " + e.getMessage() + " ++++ classpath is: " + classPath);
            }
        }
        return _resources;
    }

    /**
 	 * Gets the specified html file, merges in the localized string bundle for
	 * this page (i.e. pagename_XX.properties, XX being the locale; blank for 
	 * english/default, or other), and additionally munges the key/value pairs 
	 * specified in the munge table
	 */
    protected String getText(String htmlPage, HashMap munge) throws java.io.IOException {
        String file = Munge.getHtmlText(htmlPage);
        PropertyResourceBundle resources = getResources();
        if (resources != null) {
            Enumeration keys = resources.getKeys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                String value = resources.getString(key);
                munge.put(key, value);
            }
            file = Munge.munge(file, munge);
        }
        return Munge.munge(file, munge);
    }

    /**
 	 * Provide access to the request
	 */
    protected Request getRequest() {
        return _request;
    }

    protected void debug() {
        Enumeration e = getRequest().getRequest().getParameterNames();
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            Log.debug("Page:(Debug)", name + " | " + getRequest().getRequest().getParameter(name));
        }
    }

    protected String bold(String s) {
        return "<STRONG>" + s + "</STRONG>";
    }

    protected String link(String href, String link) {
        return "<A HREF=\"" + href + "\">" + link + "</A>";
    }
}
