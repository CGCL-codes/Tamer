package org.apache.myfaces.trinidadinternal.webapp.wrappers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;
import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;

/**
 * TODO: Document this
 *
 * @version $Revision: 245 $ $Date: 2008-11-25 19:05:42 -0500 (Tue, 25 Nov 2008) $
 */
public class RenderResponseWrapper extends PortletResponseWrapper implements RenderResponse {

    public RenderResponseWrapper(RenderResponse response) {
        super(response);
        _resp = response;
    }

    private RenderResponse _resp;

    public PortletURL createActionURL() {
        return _resp.createActionURL();
    }

    public PortletURL createRenderURL() {
        return _resp.createRenderURL();
    }

    public void flushBuffer() throws IOException {
        _resp.flushBuffer();
    }

    public int getBufferSize() {
        return _resp.getBufferSize();
    }

    public String getCharacterEncoding() {
        return _resp.getCharacterEncoding();
    }

    public String getContentType() {
        return _resp.getContentType();
    }

    public Locale getLocale() {
        return _resp.getLocale();
    }

    public String getNamespace() {
        return _resp.getNamespace();
    }

    public OutputStream getPortletOutputStream() throws IOException {
        return _resp.getPortletOutputStream();
    }

    public PrintWriter getWriter() throws IOException {
        return _resp.getWriter();
    }

    public boolean isCommitted() {
        return _resp.isCommitted();
    }

    public void reset() {
        _resp.reset();
    }

    public void resetBuffer() {
        _resp.resetBuffer();
    }

    public void setBufferSize(int arg0) {
        _resp.setBufferSize(arg0);
    }

    public void setContentType(String arg0) {
        _resp.setContentType(arg0);
    }

    public void setTitle(String arg0) {
        _resp.setTitle(arg0);
    }

    @Override
    public RenderResponse getResponse() {
        return _resp;
    }
}
