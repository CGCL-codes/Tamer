package org.soda.dpws.service.binding;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.soda.dpws.exchange.InMessage;
import org.soda.dpws.exchange.OutMessage;
import org.soda.dpws.fault.DPWSFault;
import org.soda.dpws.internal.DPWSContextImpl;
import org.soda.dpws.registry.ServiceEndpoint;
import org.soda.dpws.service.MessageInfo;
import org.soda.dpws.service.MessagePartInfo;
import org.soda.dpws.util.serialize.XMLSerializer;
import org.soda.dpws.wsdl.OperationInfo;
import org.xml.sax.SAXException;

/**
 * 
 * 
 */
public class DocumentBinding extends AbstractBinding {

    /**
   * 
   */
    public DocumentBinding() {
    }

    public void readMessage(InMessage inMessage, DPWSContextImpl context) throws DPWSFault {
        ServiceEndpoint service = context.getService();
        Collection<OperationInfo> operations = service.getOperations();
        read(inMessage, context, operations);
    }

    public void writeMessage(OutMessage message, XMLSerializer ser, DPWSContextImpl context) throws DPWSFault {
        OperationInfo op = context.getExchange().getOperation();
        Object[] values = (Object[]) message.getBody();
        int i = 0;
        MessageInfo msgInfo = null;
        boolean client = isClientModeOn(context);
        if (client) {
            msgInfo = op.getInputMessage();
        } else {
            msgInfo = op.getOutputMessage();
        }
        List<MessagePartInfo> messageParts = msgInfo.getMessageParts();
        for (Iterator<MessagePartInfo> itr = messageParts.iterator(); itr.hasNext(); ) {
            MessagePartInfo outParam = itr.next();
            try {
                Object value;
                if (client) value = getClientParam(values, outParam, context); else value = getParam(values, outParam, context);
                writeParameter(ser, context, value, outParam, getBoundNamespace(context, outParam));
            } catch (SAXException e) {
                throw new DPWSFault("Could not write to outgoing stream.", e, DPWSFault.RECEIVER);
            }
            i++;
        }
    }
}
