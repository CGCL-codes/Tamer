package org.activebpel.rt.axis.bpel;

import org.activebpel.rt.bpel.AeBusinessProcessException;
import org.activebpel.rt.bpel.server.engine.AeInvokeHandlerUri;
import org.activebpel.rt.bpel.server.engine.IAeInvokeHandlerFactory;
import org.activebpel.wsio.invoke.IAeInvoke;
import org.activebpel.wsio.invoke.IAeInvokeHandler;

/**
 * Factory for the default axis invoke handler.
 */
public class AeAxisInvokeHandlerFactory implements IAeInvokeHandlerFactory {

    /** The invoke handler used to delegate our requests to */
    private static final IAeInvokeHandler HANDLER = new AeAxisInvokeHandler();

    /**
    * @see org.activebpel.rt.bpel.server.engine.IAeInvokeHandlerFactory#createInvokeHandler(org.activebpel.wsio.invoke.IAeInvoke)
    */
    public IAeInvokeHandler createInvokeHandler(IAeInvoke aInvoke) throws AeBusinessProcessException {
        return HANDLER;
    }

    /**
    * @see org.activebpel.rt.bpel.server.engine.IAeInvokeHandlerFactory#getQueryData(org.activebpel.wsio.invoke.IAeInvoke)
    */
    public String getQueryData(IAeInvoke aInvoke) {
        return AeInvokeHandlerUri.getInvokerString(aInvoke.getInvokeHandler());
    }
}
