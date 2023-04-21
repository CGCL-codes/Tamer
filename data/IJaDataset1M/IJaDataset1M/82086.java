package org.apache.axis2.jaxws.sample.addnumbershandler;

import org.apache.axis2.jaxws.handler.LogicalMessageContext;
import javax.xml.ws.handler.MessageContext;

public class AddNumbersClientLogicalHandler4 implements javax.xml.ws.handler.LogicalHandler<LogicalMessageContext> {

    HandlerTracker tracker = new HandlerTracker(AddNumbersClientLogicalHandler4.class.getSimpleName());

    public void close(MessageContext messagecontext) {
        tracker.close();
    }

    public boolean handleFault(LogicalMessageContext messagecontext) {
        Boolean outbound = (Boolean) messagecontext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        tracker.handleFault(outbound);
        return true;
    }

    public boolean handleMessage(LogicalMessageContext messagecontext) {
        Boolean outbound = (Boolean) messagecontext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        tracker.handleMessage(outbound);
        return true;
    }
}
