package com.c2b2.open286.container.url;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import javax.portlet.BaseURL;
import javax.portlet.PortletSecurityException;
import javax.servlet.http.HttpServletRequest;

public class DefaultBaseURL implements BaseURL {

    protected static final String BASE_PREFIX = "o286.";

    protected static final String PUBLIC_RENDER_PREFIX = "o286.pr";

    DefaultBaseURL(HttpServletRequest request, String windowID) {
        myRequest = request;
        myWindowID = windowID;
    }

    public void setParameter(String string, String string1) {
    }

    public void setParameter(String string, String[] string1) {
    }

    public void setParameters(Map<String, String[]> map) {
    }

    public void setSecure(boolean b) throws PortletSecurityException {
    }

    public Map getParameterMap() {
        return null;
    }

    public void write(Writer writer) throws IOException {
    }

    public void write(Writer writer, boolean b) throws IOException {
    }

    public void addProperty(String string, String string1) {
    }

    public void setProperty(String string, String string1) {
    }

    protected HttpServletRequest myRequest;

    protected String myWindowID;
}
