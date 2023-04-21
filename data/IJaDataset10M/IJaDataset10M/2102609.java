package nl.iblinden.opencms.jsf;

import java.io.StringWriter;
import javax.faces.context.ResponseWriter;
import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.myfaces.context.servlet.ServletFacesContextImpl;

/**
 * 
 *
 * @author Martin van den Bemt
 * @version $Id: ServletFacesContextExtension.java,v 1.1 2005/11/17 11:19:37 mvdb Exp $
 */
public class ServletFacesContextExtension extends ServletFacesContextImpl {

    /**
     * @param portletContext
     * @param portletRequest
     * @param portletResponse
     */
    public ServletFacesContextExtension(PortletContext portletContext, PortletRequest portletRequest, PortletResponse portletResponse) {
        super(portletContext, portletRequest, portletResponse);
    }

    /**
     * @param servletContext
     * @param servletRequest
     * @param servletResponse
     */
    public ServletFacesContextExtension(ServletContext servletContext, ServletRequest servletRequest, ServletResponse servletResponse) {
        super(servletContext, servletRequest, servletResponse);
        ServletExternalContextExtension sece = new ServletExternalContextExtension(servletContext, servletRequest, servletResponse);
        this.setExternalContext(sece);
        System.out.println("ServletRequest : " + servletRequest);
        if (servletRequest instanceof FacesRequestWrapper) {
            FacesRequestWrapper frw = (FacesRequestWrapper) servletRequest;
            sece.setRequestPathInfo(frw.getTarget());
            if (frw.needToClearViewRoot()) {
                System.out.println("Viewroot : " + getViewRoot());
            }
        }
    }

    /**
     * @see org.apache.myfaces.context.servlet.ServletFacesContextImpl#setResponseWriter(javax.faces.context.ResponseWriter)
     */
    public void setResponseWriter(ResponseWriter responseWriter) {
        super.setResponseWriter(responseWriter);
        StringWriter strWriter = new StringWriter();
        responseWriter.cloneWithWriter(strWriter);
    }
}
