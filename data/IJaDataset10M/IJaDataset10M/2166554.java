package com.liferay.util.jsf.apache.myfaces.context.servlet;

import javax.faces.context.ResponseWriter;
import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import org.apache.myfaces.context.ReleaseableExternalContext;
import org.apache.myfaces.context.servlet.ServletFacesContextImpl;

/**
 * <a href="MyFacesContextImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Myunghun Kim
 *
 */
public class MyFacesContextImpl extends ServletFacesContextImpl {

    public MyFacesContextImpl(PortletContext portletContext, PortletRequest portletRequest, PortletResponse portletResponse) {
        super(portletContext, portletRequest, portletResponse);
    }

    public void setResponseWriter(ResponseWriter responseWriter) {
        if (responseWriter == null) {
            throw new NullPointerException("responseWriter");
        }
        _responseWriter = responseWriter;
    }

    public ResponseWriter getResponseWriter() {
        return _responseWriter;
    }

    public void release() {
        super.release();
        _responseWriter = null;
    }

    public void setExternalContext(ReleaseableExternalContext extContext) {
        _responseWriter = null;
        super.setExternalContext(extContext);
    }

    private ResponseWriter _responseWriter = null;
}
