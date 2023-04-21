package com.volantis.mcs.marlin.sax;

import com.volantis.mcs.context.MarinerRequestContext;

/**
 * A factory which creates {@link IAPIContentHandler} instances.
 */
public class IAPIContentHandlerFactory implements AbstractContentHandlerFactory {

    /**
     * String array of the namespaces that should be handled by the
     * {@link IAPIContentHandler}.
     */
    private static final String[] handledNamespaces = new String[] { "http://www.volantis.com/im" };

    public MCSInternalContentHandler createContentHandler(MarinerRequestContext requestContext) {
        IAPIContentHandler handler = new IAPIContentHandler();
        handler.setInitialRequestContext(requestContext);
        return handler;
    }

    public String[] getHandledNamespaces() {
        return handledNamespaces;
    }
}
