package com.itroadlabs.maps.tiles.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Date: 14.12.2009
 * Time: 9:06:21
 */
public interface MapTileRequestProcessor {

    void init(ServletConfig servletConfig) throws ServletException;

    void process(HttpServletRequest request, HttpServletResponse response, int x, int y, int zoom) throws IOException, ServletException;
}
