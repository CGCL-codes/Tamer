package org.apache.axis2.receivers;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;

/**
 * This is takes care of the IN-OUT sync MEP in the server side
 *
 * @deprecated no longer needed, going away after 1.3
 */
public abstract class AbstractInOutAsyncMessageReceiver extends AbstractInOutMessageReceiver {

    public void receive(final MessageContext messageCtx) throws AxisFault {
        messageCtx.setProperty(DO_ASYNC, Boolean.TRUE);
        super.receive(messageCtx);
    }
}
