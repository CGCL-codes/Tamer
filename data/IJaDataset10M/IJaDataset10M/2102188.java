package org.apache.axis2.dispatchers;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.HandlerDescription;
import org.apache.axis2.engine.AbstractDispatcher;

/**
 * Dispatches the service based on the information from the target endpoint URL.
 */
public class RequestURIBasedDispatcher extends AbstractDispatcher {

    public static final String NAME = "RequestURIBasedDispatcher";

    private RequestURIBasedServiceDispatcher rubsd = new RequestURIBasedServiceDispatcher();

    public AxisOperation findOperation(AxisService service, MessageContext messageContext) throws AxisFault {
        return null;
    }

    public AxisService findService(MessageContext messageContext) throws AxisFault {
        return rubsd.findService(messageContext);
    }

    public void initDispatcher() {
        init(new HandlerDescription(NAME));
    }
}
