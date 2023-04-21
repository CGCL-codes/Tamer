package com.c2b2.ipoint.presentation.portlets.jsr168;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequestWrapper;

/**
  * This class implements the SerlvetRequest used by the JSR168RequestDispatcher
  * and meets the requirements of the Portlet Spec
  * <p>
  * $Date: 2005/12/26 21:10:20 $
  * 
  * $Id: JSR168ServletRequestWrapper.java,v 1.1 2005/12/26 21:10:20 steve Exp $<br>
  * 
  * Copyright 2005 C2B2 Consulting Limited. All rights reserved.
  * </p>
  * @author $Author: steve $
  * @version $Revision: 1.1 $
  */
public class JSR168ServletRequestWrapper extends HttpServletRequestWrapper {

    private JSR168PortletRequest myRequest;

    private HashMap<String, ArrayList<String>> myParameters;

    private boolean parsedQueyString = false;

    public JSR168ServletRequestWrapper(JSR168PortletRequest prequest) {
        super(prequest.getServletRequest());
        myRequest = prequest;
    }

    public String getMethod() {
        return "GET";
    }

    public StringBuffer getRequestURL() {
        return null;
    }

    public String getProtocol() {
        return null;
    }

    public String getRemoteAddr() {
        return null;
    }

    public String getRemoteHost() {
        return null;
    }

    public String getRealPath(String string) {
        return null;
    }

    public String getContextPath() {
        return myRequest.getContextPath();
    }

    public int getContentLength() {
        return 0;
    }

    public String getCharacterEncoding() {
        return null;
    }

    public void setCharacterEncoding(String string) throws UnsupportedEncodingException {
    }

    public String getContentType() {
        return null;
    }

    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    public BufferedReader getReader() throws IOException {
        return null;
    }

    public String getRequestURI() {
        String result = (String) super.getAttribute("javax.servlet.include.request_uri");
        if (result == null) {
            result = super.getRequestURI();
        }
        return result;
    }

    public String getPathInfo() {
        String result = (String) super.getAttribute("javax.servlet.include.path_info");
        if (result == null) {
            result = super.getPathInfo();
        }
        return result;
    }

    public String getServletPath() {
        String result = (String) super.getAttribute("javax.servlet.include.servlet_path");
        if (result == null) {
            result = super.getServletPath();
        }
        return result;
    }

    public String getQueryString() {
        String result = (String) super.getAttribute("javax.servlet.include.query_string");
        return result;
    }

    public String getParameter(String string) {
        parseQueryString();
        String result = null;
        if (myParameters != null) {
            ArrayList<String> value = myParameters.get(string);
            if (value != null) {
                result = value.get(0);
            }
        }
        if (result == null) {
            result = myRequest.getParameter(string);
        }
        return result;
    }

    public Map getParameterMap() {
        parseQueryString();
        HashMap<String, String[]> result = new HashMap<String, String[]>();
        Enumeration names = getParameterNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            String values[] = getParameterValues(name);
            result.put(name, values);
        }
        return result;
    }

    public Enumeration getParameterNames() {
        parseQueryString();
        Vector<String> resultVec = new Vector<String>();
        if (myParameters != null) {
            resultVec.addAll(myParameters.keySet());
        }
        Enumeration reqNames = myRequest.getParameterNames();
        while (reqNames.hasMoreElements()) {
            String value = (String) reqNames.nextElement();
            resultVec.add(value);
        }
        return resultVec.elements();
    }

    public String[] getParameterValues(String string) {
        parseQueryString();
        ArrayList<String> localValues = null;
        String result[] = null;
        if (myParameters != null) {
            localValues = myParameters.get(string);
        }
        String requestValues[] = myRequest.getParameterValues(string);
        if (requestValues == null && localValues == null) {
            result = null;
        } else if (requestValues != null && localValues == null) {
            result = requestValues;
        } else if (requestValues == null && localValues != null) {
            result = (String[]) localValues.toArray(new String[localValues.size()]);
        } else {
            result = new String[requestValues.length + localValues.size()];
            System.arraycopy(localValues.toArray(), 0, result, 0, localValues.size());
            System.arraycopy(requestValues, 0, result, localValues.size(), requestValues.length);
        }
        return result;
    }

    private void parseQueryString() {
        if (!parsedQueyString) {
            String queryString = getQueryString();
            if (queryString != null) {
                myParameters = new HashMap<String, ArrayList<String>>();
                StringTokenizer strtok = new StringTokenizer(queryString, "&=");
                while (strtok.hasMoreTokens()) {
                    String name = strtok.nextToken();
                    String value = null;
                    try {
                        value = strtok.nextToken();
                    } catch (NoSuchElementException nse) {
                    }
                    ArrayList<String> valueArray = myParameters.get(name);
                    if (valueArray == null) {
                        valueArray = new ArrayList<String>();
                        myParameters.put(name, valueArray);
                    }
                    valueArray.add(value);
                }
            }
            parsedQueyString = true;
        }
    }
}
