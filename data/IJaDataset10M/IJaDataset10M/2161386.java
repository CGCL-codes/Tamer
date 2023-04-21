package net.sf.miniportal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import javax.portlet.ActionRequest;
import javax.servlet.http.HttpServletRequest;

class ActionRequestImpl extends PortletRequestImpl implements ActionRequest {

    ActionRequestImpl(HttpServletRequest request, Map params, PortletConfigImpl config, PortalContextImpl portalContext) {
        super(request, params, config, portalContext);
    }

    public InputStream getPortletInputStream() throws IOException {
        return getRequest().getInputStream();
    }

    public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException {
        getRequest().setCharacterEncoding(arg0);
    }

    public BufferedReader getReader() throws UnsupportedEncodingException, IOException {
        return getRequest().getReader();
    }

    public String getCharacterEncoding() {
        return getRequest().getCharacterEncoding();
    }

    public String getContentType() {
        return getRequest().getContentType();
    }

    public int getContentLength() {
        return getRequest().getContentLength();
    }
}
