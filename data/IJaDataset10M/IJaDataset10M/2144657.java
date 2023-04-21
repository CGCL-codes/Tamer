package org.avis.io;

import junit.framework.AssertionFailedError;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.avis.io.messages.Message;
import static org.junit.Assert.fail;
import static org.avis.logging.Log.alarm;
import static org.avis.util.Text.className;

/**
 * IO handler that allows a test client to wait for an incoming
 * message.
 * 
 * @author Matthew Phillips
 */
public class TestingIoHandler extends IoHandlerAdapter implements IoHandler {

    private static final int TIMEOUT = 5000;

    public Message message;

    @Override
    public synchronized void messageReceived(IoSession session, Object theMessage) throws Exception {
        message = (Message) theMessage;
        notifyAll();
    }

    public synchronized Message waitForMessage() throws InterruptedException {
        if (message == null) wait(TIMEOUT);
        if (message == null) fail("No message received");
        return message;
    }

    @SuppressWarnings("unchecked")
    public synchronized <T extends Message> T waitForMessage(Class<T> type) throws InterruptedException {
        waitForMessage();
        if (type.isAssignableFrom(message.getClass())) return (T) message; else throw new AssertionFailedError("Expected " + className(type) + ", was " + className(message));
    }

    public synchronized void waitForClose(IoSession session) {
        if (!session.isConnected()) return;
        try {
            wait(TIMEOUT);
        } catch (InterruptedException ex) {
            throw new Error(ex);
        }
        if (session.isConnected() && !session.isClosing()) throw new AssertionFailedError("Session not closed");
    }

    @Override
    public synchronized void sessionClosed(IoSession session) throws Exception {
        notifyAll();
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        alarm("MINA IO exception", this, cause);
    }
}
