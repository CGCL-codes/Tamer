package ca.ucalgary.cpsc.agilePlanner.test.unit.persister;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Ignore;
import org.junit.Test;
import persister.Message;
import persister.data.impl.MessageDataObject;
import persister.xml.InboundConnectionThread;
import persister.xml.XMLSocketClient;
import persister.xml.XMLSocketServer;
import ca.ucalgary.cpsc.agilePlanner.test.planner.MockClientCommunicator;

public class InboundThreadTest {

    @Test
    @Ignore
    public void testRecievingOfBigMessage() throws Exception {
        XMLSocketServer server = new XMLSocketServer(5353, null);
        MockClientCommunicator communicator = new MockClientCommunicator();
        XMLSocketClient client = new XMLSocketClient("localhost", 5353, communicator);
        Message msg = new MessageDataObject(0);
        StringBuffer bigMessage = new StringBuffer();
        while (bigMessage.length() < InboundConnectionThread.BUFFER_SIZE) {
            bigMessage.append("data - ");
        }
        msg.addData("BigData", bigMessage.toString());
        server.send(msg);
        long timeout = 10 * 60 * 1000;
        long start = System.currentTimeMillis();
        while (communicator.messageReceived() == null && (System.currentTimeMillis() < start + timeout)) ;
        assertNotNull(communicator.messageReceived());
        assertEquals(bigMessage.toString(), communicator.messageReceived().getData().get("BigData"));
    }
}
