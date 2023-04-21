package com.tchepannou.rails.core.api;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Defines a set of methods that {@link ActionController} uses to communicate with its container.
 * 
 * @author herve
 */
public interface ActionContext extends Context {

    public HttpServletRequest getRequest();

    public HttpServletResponse getResponse();

    public Map getRequestParameters();

    public List<FilePart> getRequestFiles();
}
