package org.elephantt.webby;

import java.util.Map;
import java.lang.reflect.Method;

/**
 * A mapping of a request to a request. Any URL parameters for this requeset (as extracted from the URL)
 * are contained in the URL parameter map.
 */
public class RequestMapping {

    private final Object requestHandler;

    private final Method requestHandlerMethod;

    private final String viewNamePrefix;

    private final Map<String, String> urlParameterMap;

    public RequestMapping(Object requestHandler, Method requestHandlerMethod, String viewNamePrefix, Map<String, String> urlParameterMap) {
        if (requestHandler == null) throw new IllegalArgumentException("requestHandler must be non-null");
        this.requestHandler = requestHandler;
        this.requestHandlerMethod = requestHandlerMethod;
        this.urlParameterMap = urlParameterMap;
        this.viewNamePrefix = viewNamePrefix;
    }

    public Object getRequestHandler() {
        return requestHandler;
    }

    public Method getRequestHandlerMethod() {
        return requestHandlerMethod;
    }

    /**
   * Returns the "view name prefix", which is a combination of the controller name and the method name.
   *
   * For example, if the controller class is com.example.HomeController, and the method name is index(), the
   * view name prefix is "home_index". This is useful to renderers that want to infer a template name (e.g., JSP, FreeMarker, etc.)
   */
    public String getViewNamePrefix() {
        return viewNamePrefix;
    }

    public Map<String, String> getUrlParameterMap() {
        return urlParameterMap;
    }

    public String toString() {
        return "RequestMapping{" + "requestHandler=" + requestHandler + ", urlParameterMap=" + urlParameterMap + '}';
    }
}
