package vqwiki.servlets;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import vqwiki.Constants;

public class WikiContext implements Constants {

    private static final ThreadLocal wikiContext = new ThreadLocal();

    private static final Logger logger = Logger.getLogger(WikiContext.class.getName());

    private HttpServletRequest request;

    private HttpServletResponse response;

    private static ServletContext servletContext;

    public WikiContext(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public static void setContext(WikiContext context) {
        wikiContext.set(context);
    }

    public static WikiContext getContext() {
        return (WikiContext) wikiContext.get();
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public String getTopic() {
        return WikiContext.getTopic(request);
    }

    public String getVirtualWiki() {
        return WikiContext.getVirtualWiki(request);
    }

    public static String getTopic(HttpServletRequest request) {
        return (String) request.getAttribute(TOPIC_KEY);
    }

    public static String getVirtualWiki(HttpServletRequest request) {
        String virtualWiki = (String) request.getAttribute(VIRTUAL_WIKI_KEY);
        return virtualWiki != null ? virtualWiki : DEFAULT_VWIKI;
    }

    public static ServletContext getServletContext() {
        return servletContext;
    }

    public static void clearServletContext() {
        WikiContext.servletContext = null;
    }

    public static void setServletContext(ServletContext servletContext) {
        WikiContext.servletContext = servletContext;
        servletContext.setAttribute(CACHED_CONTENTS_KEY, new HashMap());
    }

    public static void cleanup() {
        wikiContext.set(null);
    }

    /**
     * Clears the cached content This method is called when a "edit-save" or
     * "edit-cancel" is invoked.
     * <p/>
     * Clearing all cached contents forces to reload.
     */
    public void removeCachedContents() {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Removing Cached Contents; cachedContents.size() = " + getCachedContents().size());
        }
        getCachedContents().clear();
    }

    public Map getCachedContents() {
        return (Map) servletContext.getAttribute(CACHED_CONTENTS_KEY);
    }
}
