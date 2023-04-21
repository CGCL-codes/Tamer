package net.sf.istcontract.wsimport.api.wsdl.parser;

import net.sf.istcontract.wsimport.api.model.wsdl.WSDLModel;
import net.sf.istcontract.wsimport.api.server.Container;
import com.sun.istack.NotNull;

/**
 * Provides contextual information for {@link WSDLParserExtension}s.
 * 
 * @author Vivek Pandey
 * @author Fabian Ritzmann
 */
public interface WSDLParserExtensionContext {

    /**
     * Returns true if the WSDL parsing is happening on the client side. Returns false means
     * its started on the server side.
     */
    boolean isClientSide();

    /**
     * Gives the {@link WSDLModel}. The WSDLModel may not be complete until
     * {@link WSDLParserExtension#finished(WSDLParserExtensionContext)} is called.
     */
    WSDLModel getWSDLModel();

    /**
     * Provides the {@link Container} in which this service or client is running.
     * May return null.
     *
     * @return The container in which this service or client is running.
     */
    @NotNull
    Container getContainer();
}
