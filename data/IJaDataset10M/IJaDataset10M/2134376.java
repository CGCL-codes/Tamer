package org.ashkelon.manager.jsp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class SimpleRequestDispatcher implements RequestDispatcher {

    String _resourcePath;

    public SimpleRequestDispatcher(String resourcePath) {
        _resourcePath = resourcePath;
    }

    JSPServletLoader _loader;

    public void setJspServletLoader(JSPServletLoader loader) {
        _loader = loader;
    }

    public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException {
    }

    public void include(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        if (_resourcePath.endsWith(".html")) {
            PrintWriter writer = response.getWriter();
            BufferedReader reader = new BufferedReader(new FileReader(_resourcePath));
            String line = null;
            while ((line = reader.readLine()) != null) {
                writer.println(line);
            }
        } else {
            try {
                int idx = _resourcePath.lastIndexOf("/");
                String jsp_name = _resourcePath.substring(idx + 1);
                idx = jsp_name.indexOf("?");
                String queryString = "";
                if (idx > -1) {
                    queryString = jsp_name.substring(idx + 1);
                    jsp_name = jsp_name.substring(0, idx);
                }
                if (queryString.length() > 0) {
                    fillRequestFromQueryString(queryString, (Request) request);
                }
                Servlet servlet = _loader.loadJSPServlet(jsp_name);
                servlet.service(request, response);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void fillRequestFromQueryString(String queryString, Request request) {
        String[] pairs = queryString.split("&");
        String[] pair;
        for (int i = 0; i < pairs.length; i++) {
            pair = pairs[i].split("=");
            ((Request) request).setParameter(pair[0], pair[1]);
        }
    }
}
