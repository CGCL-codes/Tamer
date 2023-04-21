package net.grinder.communication;

import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import net.grinder.common.UncheckedInterruptedException;
import net.grinder.testutility.IsolatedObjectFactory;
import junit.framework.TestCase;

/**
 *  Unit test case for <code>StreamReceiver</code>.
 *
 * @author Philip Aston
 * @version $Revision: 3762 $
 */
public class TestStreamReceiver extends TestCase {

    public TestStreamReceiver(String name) {
        super(name);
    }

    public void testReceive() throws Exception {
        final PipedOutputStream outputStream = new PipedOutputStream();
        final InputStream inputStream = new PipedInputStream(outputStream);
        final StreamReceiver streamReceiver = new StreamReceiver(inputStream);
        final SimpleMessage message1 = new SimpleMessage();
        final ObjectOutputStream objectStream1 = new ObjectOutputStream(outputStream);
        objectStream1.writeObject(message1);
        objectStream1.flush();
        final SimpleMessage message2 = new SimpleMessage();
        message2.setPayload(IsolatedObjectFactory.getIsolatedObject());
        final ObjectOutputStream objectStream2 = new ObjectOutputStream(outputStream);
        objectStream2.writeObject(message2);
        objectStream2.flush();
        final SimpleMessage message3 = new SimpleMessage();
        final ObjectOutputStream objectStream3 = new ObjectOutputStream(outputStream);
        objectStream3.writeObject(message3);
        objectStream3.flush();
        final Message receivedMessage1 = streamReceiver.waitForMessage();
        try {
            streamReceiver.waitForMessage();
            fail("Expected CommunicationException");
        } catch (CommunicationException e) {
        }
        final Message receivedMessage2 = streamReceiver.waitForMessage();
        assertEquals(message1, receivedMessage1);
        assertEquals(message3, receivedMessage2);
        assertEquals(UncheckedInterruptedException.class, new BlockingActionThread() {

            protected void blockingAction() throws CommunicationException {
                streamReceiver.waitForMessage();
            }
        }.getException().getClass());
        outputStream.close();
        try {
            streamReceiver.waitForMessage();
            fail("Expected CommunicationException");
        } catch (CommunicationException e) {
        }
    }

    public void testShutdown() throws Exception {
        final PipedOutputStream outputStream = new PipedOutputStream();
        final InputStream inputStream = new PipedInputStream(outputStream);
        final StreamReceiver streamReceiver = new StreamReceiver(inputStream);
        final SimpleMessage message = new SimpleMessage();
        final ObjectOutputStream objectStream = new ObjectOutputStream(outputStream);
        objectStream.writeObject(message);
        objectStream.flush();
        final Message receivedMessage = streamReceiver.waitForMessage();
        assertNotNull(receivedMessage);
        streamReceiver.shutdown();
        assertNull(streamReceiver.waitForMessage());
    }

    public void testCloseCommunicationMessage() throws Exception {
        final PipedOutputStream outputStream = new PipedOutputStream();
        final InputStream inputStream = new PipedInputStream(outputStream);
        final StreamReceiver streamReceiver = new StreamReceiver(inputStream);
        final SimpleMessage message = new SimpleMessage();
        final ObjectOutputStream objectStream1 = new ObjectOutputStream(outputStream);
        objectStream1.writeObject(message);
        objectStream1.flush();
        final Message receivedMessage = streamReceiver.waitForMessage();
        assertNotNull(receivedMessage);
        final Message closeCommunicationMessage = new CloseCommunicationMessage();
        final ObjectOutputStream objectStream2 = new ObjectOutputStream(outputStream);
        objectStream2.writeObject(closeCommunicationMessage);
        objectStream2.flush();
        assertNull(streamReceiver.waitForMessage());
    }
}
