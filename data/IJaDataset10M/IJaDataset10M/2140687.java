package org.labrad.events;

import org.labrad.data.Context;
import org.labrad.data.Data;
import org.labrad.data.Packet;
import org.labrad.data.Record;

/**
 *
 * @author Matthew Neeley
 */
public class MessageListenerSupport extends ListenerSupport<MessageListener> {

    public MessageListenerSupport(Object source) {
        super(source);
    }

    public void fireMessage(Packet packet) {
        Context ctx = packet.getContext();
        long srcID = packet.getTarget();
        for (Record r : packet.getRecords()) {
            long msgID = r.getID();
            Data data = r.getData();
            for (MessageListener listener : listeners) {
                MessageEvent evt = new MessageEvent(source, ctx, srcID, msgID, data);
                listener.messageReceived(evt);
            }
        }
    }
}
