package com.dyuproject.web.rest;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Dispatches a request to generate a view from a Model
 * 
 * @author David Yu
 * @created May 16, 2008
 */
public interface ViewDispatcher extends LifeCycle {

    public void dispatch(String uri, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
