package org.restlet.engine;

import org.restlet.Client;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Template;

/**
 * Base call dispatcher capable of resolving target resource URI templates.
 * 
 * Concurrency note: instances of this class or its subclasses can be invoked by
 * several threads at the same time and therefore must be thread-safe. You
 * should be especially careful when storing state as member variables.
 * 
 * @author Jerome Louvel
 */
public class TemplateDispatcher extends Client {

    /** The context. */
    private volatile Context context;

    /**
     * Constructor.
     * 
     * @param context
     *            The context.
     */
    public TemplateDispatcher(Context context) {
        super(null, (Protocol) null);
        this.context = context;
    }

    /**
     * Actually handles the call. Since this method only sets the request's
     * original reference ({@link Request#getOriginalRef()} with the the
     * targeted one, it must be overridden by subclasses.
     * 
     * 
     * @param request
     *            The request to handle.
     * @param response
     *            The response to update.
     */
    protected void doHandle(Request request, Response response) {
        request.setOriginalRef(request.getResourceRef().getTargetRef());
    }

    /**
     * Returns the context. Override default behavior from {@link Restlet}.
     * 
     * @return The context.
     */
    @Override
    public Context getContext() {
        return context;
    }

    /**
     * Handles the call after resolving any URI template on the request's target
     * resource reference.
     * 
     * @param request
     *            The request to handle.
     * @param response
     *            The response to update.
     */
    public void handle(Request request, Response response) {
        Response.setCurrent(response);
        Protocol protocol = request.getProtocol();
        if (protocol == null) {
            throw new UnsupportedOperationException("Unable to determine the protocol to use for this call.");
        }
        String targetUri = request.getResourceRef().toString(true, false);
        if (targetUri.contains("{")) {
            Template template = new Template(targetUri);
            request.setResourceRef(template.format(request, response));
        }
        doHandle(request, response);
        if ((response.getEntity() != null) && (response.getEntity().getLocationRef() == null)) {
            response.getEntity().setLocationRef(request.getResourceRef().toString());
        }
    }

    /**
     * Sets the context. Override default behavior from {@link Restlet}.
     * 
     * @param context
     *            The context to set.
     */
    @Override
    public void setContext(Context context) {
        this.context = context;
    }
}
