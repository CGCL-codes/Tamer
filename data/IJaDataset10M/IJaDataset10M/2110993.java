package org.activebpel.rt.axis.bpel.invokers;

import org.activebpel.rt.axis.bpel.AeMessages;
import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.constants.Style;

/**
 * Provides factorya access for RPC or Document style invokers.  Invoker instances
 * returned from this impl are thread safe.
 */
public class AeInvokerFactory {

    private static final String INVALID_INVOKE_STYLE = "AeInvokerFactory.ERROR_1";

    /** RPC style invoker singleton instance. */
    private static final AeRpcStyleInvoker RPC_INVOKER = new AeRpcStyleInvoker();

    /** Document style invoker singleton instance. */
    private static final AeDocumentStyleInvoker DOC_INVOKER = new AeDocumentStyleInvoker();

    /**
    * Get the invoker.
    * @param aContext
    */
    public static IAeInvoker getInvoker(AeAxisInvokeContext aContext) throws AxisFault {
        IAeInvoker invoker = null;
        String style = (String) aContext.getCall().getProperty(Call.OPERATION_STYLE_PROPERTY);
        if (Style.RPC.getName().equals(style)) {
            invoker = RPC_INVOKER;
        } else if (Style.DOCUMENT.getName().equals(style)) {
            invoker = DOC_INVOKER;
        }
        if (invoker == null) {
            throw new AxisFault(AeMessages.getString(INVALID_INVOKE_STYLE));
        }
        return invoker;
    }
}
