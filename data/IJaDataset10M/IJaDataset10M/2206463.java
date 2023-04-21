package com.yuchengtech.simpleServer.core.handler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.util.HashMap;
import com.yuchengtech.simpleServer.core.HttpRequest;
import com.yuchengtech.simpleServer.core.HttpResponse;
import com.yuchengtech.simpleServer.servlet.ServletBean;
import com.yuchengtech.simpleServer.servlet.ServletConfig;
import com.yuchengtech.simpleServer.servlet.SimpleServlet;

public class ServletHandler implements Handler {

    private static final ServletHandler SERVLET_HANDLER = new ServletHandler();

    private static HashMap<String, ServletBean> servletMap;

    public void doHandle(Socket connection, HttpRequest request, HttpResponse response) {
        ServletBean servletBean = servletMap.get(request.getUri());
        if (servletBean == null) {
            response.setStatusCode(HttpURLConnection.HTTP_NOT_FOUND);
            try {
                response.doResponse();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        try {
            SimpleServlet servlet = servletBean.getSimpleServlet();
            if (servlet == null) {
                servlet = (SimpleServlet) Class.forName(servletBean.getClassPath()).newInstance();
                servletBean.setSimpleServlet(servlet);
            }
            servlet.service(request, response);
            response.doResponse();
        } catch (Exception e) {
            response.setStatusCode(HttpURLConnection.HTTP_SERVER_ERROR);
            try {
                response.doResponse();
            } catch (IOException ee) {
            }
        }
    }

    public static void setServletConfig(ServletConfig servletConfig) {
        servletMap = servletConfig.getServletMap();
    }

    public static ServletHandler getSingleHandler() {
        return SERVLET_HANDLER;
    }
}
