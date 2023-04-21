package com.javascales.monitoring.inspector.servlet23;

import javax.servlet.http.HttpServletRequest;
import com.javascales.monitoring.inspector.RequestInspector;

/**
 * request.getQueryString()
 */
public class QuerystringInspector extends RequestInspector {

    public String report(HttpServletRequest request) {
        return request.getQueryString();
    }

    public String getDescription() {
        return "Querystring";
    }
}
