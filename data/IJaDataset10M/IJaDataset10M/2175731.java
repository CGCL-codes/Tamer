package org.apache.myfaces.trinidadinternal.webapp;

import java.util.Iterator;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;

/**
 * Pseudo FacesContext, vended by the filter for code that 
 * needs to run before (or after) the FacesServlet, but needs
 * access to servlet objects.  This object is only available
 * inside the filter.
 * 
 */
class PseudoFacesContext extends FacesContext {

    public PseudoFacesContext(ExternalContext ec) {
        assert ec != null;
        _external = ec;
    }

    @Override
    public ResponseWriter getResponseWriter() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setResponseWriter(ResponseWriter responseWriter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<FacesMessage> getMessages() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<FacesMessage> getMessages(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addMessage(String id, FacesMessage message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FacesMessage.Severity getMaximumSeverity() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<String> getClientIdsWithMessages() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Application getApplication() {
        throw new UnsupportedOperationException();
    }

    @Override
    public UIViewRoot getViewRoot() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setViewRoot(UIViewRoot viewRoot) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ExternalContext getExternalContext() {
        return _external;
    }

    @Override
    public RenderKit getRenderKit() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getRenderResponse() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getResponseComplete() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseStream getResponseStream() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setResponseStream(ResponseStream responseStream) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void release() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void renderResponse() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void responseComplete() {
        throw new UnsupportedOperationException();
    }

    private final ExternalContext _external;
}
