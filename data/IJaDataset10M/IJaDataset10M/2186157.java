package org.mobicents.protocols.smpp.message;

import org.testng.annotations.Test;
import org.mobicents.protocols.smpp.Address;
import org.mobicents.protocols.smpp.message.QueryBroadcastSM;

@Test
public class QueryBroadcastSMTest extends PacketTests<QueryBroadcastSM> {

    protected Class<QueryBroadcastSM> getPacketType() {
        return QueryBroadcastSM.class;
    }

    @Override
    protected QueryBroadcastSM getInitialisedPacket() {
        QueryBroadcastSM packet = new QueryBroadcastSM();
        packet.setMessageId("messageId");
        packet.setSource(new Address(1, 2, "3333333"));
        return packet;
    }
}
