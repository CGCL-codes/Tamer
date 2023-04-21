package net.jxta.impl.endpoint.servlethttp;

import java.util.Timer;
import java.util.TimerTask;
import java.io.IOException;
import java.util.logging.Level;
import net.jxta.logging.Logging;
import java.util.logging.Logger;
import net.jxta.endpoint.EndpointAddress;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.MessageElement;
import net.jxta.endpoint.StringMessageElement;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.impl.endpoint.BlockingMessenger;
import net.jxta.impl.endpoint.EndpointServiceImpl;
import net.jxta.impl.util.TimeUtils;

/**
 * Simple messenger that waits for a message to give back to the requesting client
 *
 * <p/>This messenger is not entirely thread-safe. You should not use any
 * of the <code>sendMessage</code> methods from more than one thread.
 *
 */
final class HttpServletMessenger extends BlockingMessenger {

    /**
     *  Logger
     */
    private static final transient Logger LOG = Logger.getLogger(HttpServletMessenger.class.getName());

    private static final int SEND_IDLE = 0;

    private static final int SEND_INPROGRESS = 1;

    private static final int SEND_SUCCESS = 2;

    private static final int SEND_FAIL = 3;

    private static final int SEND_TOOLONG = 4;

    private static final long MAX_SENDING_BLOCK = 2 * TimeUtils.AMINUTE;

    private static final long MAX_SENDING_WAIT = 3 * TimeUtils.ASECOND;

    private static final EndpointAddress nullEndpointAddr = new EndpointAddress("http", "0.0.0.0:0", null, null);

    private static final Timer closeMessengerTimer = new Timer("HttpServletMessenger Expiration timer", true);

    private final EndpointAddress logicalAddress;

    private final MessageElement srcAddressElement;

    private ScheduledExipry expirationTask;

    /**
     *  The message "queue"
     */
    private Message outgoingMessage = null;

    private int sendResult = SEND_IDLE;

    private long sendingSince = 0;

    /**
     *  Allows us to schedule the closing of a messenger.
     */
    private static class ScheduledExipry extends TimerTask {

        /**
         *  The messenger we will be expiring.
         */
        HttpServletMessenger messenger;

        ScheduledExipry(HttpServletMessenger toExpire) {
            messenger = toExpire;
        }

        /**
         *  {@inheritDoc}
         */
        @Override
        public boolean cancel() {
            messenger = null;
            boolean result = super.cancel();
            closeMessengerTimer.purge();
            return result;
        }

        /**
         *  {@inheritDoc}
         */
        @Override
        public void run() {
            try {
                HttpServletMessenger temp = messenger;
                messenger = null;
                if (null != temp) {
                    temp.close();
                }
            } catch (Throwable all) {
                if (Logging.SHOW_SEVERE && LOG.isLoggable(Level.SEVERE)) {
                    LOG.log(Level.SEVERE, "Uncaught Throwable in timer task :" + Thread.currentThread().getName(), all);
                }
            }
        }
    }

    /**
     *  Standard constructor.
     *
     * @param peerGroupID the peer group id
     * @param srcAddress  source address
     * @param logicalAddress logical address
     * @param validFor validity in milliseconds
     */
    HttpServletMessenger(PeerGroupID peerGroupID, EndpointAddress srcAddress, EndpointAddress logicalAddress, long validFor) {
        super(peerGroupID, nullEndpointAddr, false);
        this.logicalAddress = logicalAddress;
        this.srcAddressElement = new StringMessageElement(EndpointServiceImpl.MESSAGE_SOURCE_NAME, srcAddress.toString(), null);
        if ((0 != validFor) && (validFor < Long.MAX_VALUE)) {
            expirationTask = new ScheduledExipry(this);
            closeMessengerTimer.schedule(expirationTask, validFor);
        }
        if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
            LOG.fine("HttpServletMessenger\n\t" + toString());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void closeImpl() {
        if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
            LOG.fine("close\n\t" + toString());
        }
        ScheduledExipry cancelExpire = expirationTask;
        expirationTask = null;
        if (null != cancelExpire) {
            cancelExpire.cancel();
        }
        super.close();
        notifyAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EndpointAddress getLogicalDestinationImpl() {
        return logicalAddress;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isIdleImpl() {
        return false;
    }

    /**
     * Send messages. Messages are queued and processed by a thread
     * running HttpClientConnection.
     */
    @Override
    public synchronized void sendMessageBImpl(Message message, String service, String serviceParam) throws IOException {
        if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
            LOG.fine("Send " + message + " to " + dstAddress.toString() + "\n\t" + toString());
        }
        if (isClosed()) {
            IOException failure = new IOException("Messenger was closed, it cannot be used to send messages.");
            if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                LOG.log(Level.FINE, failure.getMessage(), failure);
            }
            throw failure;
        }
        message.replaceMessageElement(EndpointServiceImpl.MESSAGE_SOURCE_NS, srcAddressElement);
        EndpointAddress destAddressToUse = getDestAddressToUse(service, serviceParam);
        MessageElement dstAddressElement = new StringMessageElement(EndpointServiceImpl.MESSAGE_DESTINATION_NAME, destAddressToUse.toString(), null);
        message.replaceMessageElement(EndpointServiceImpl.MESSAGE_DESTINATION_NS, dstAddressElement);
        if (!doSend(message)) {
            IOException failure = new IOException("Messenger was closed, it cannot be used to send messages.");
            if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                LOG.log(Level.FINE, "sendMessage failed (messenger closed).\n\t" + toString(), failure);
            }
            throw failure;
        }
        if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
            LOG.fine("sendMessage successful for " + message + "\n\t" + toString());
        }
    }

    private boolean doSend(Message message) {
        if (isClosed()) {
            return false;
        }
        long now = TimeUtils.timeNow();
        if (sendResult != SEND_IDLE) {
            if ((sendResult == SEND_TOOLONG) && (now > (sendingSince + MAX_SENDING_BLOCK))) {
                close();
            }
            return true;
        }
        outgoingMessage = message;
        sendResult = SEND_INPROGRESS;
        sendingSince = now;
        if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
            LOG.fine("Queued " + message);
        }
        notifyAll();
        long absoluteTimeOut = TimeUtils.toAbsoluteTimeMillis(MAX_SENDING_WAIT);
        while (!isClosed()) {
            if (sendResult != SEND_INPROGRESS) {
                break;
            }
            long waitfor = TimeUtils.toRelativeTimeMillis(absoluteTimeOut);
            if (waitfor <= 0) {
                break;
            }
            try {
                wait(waitfor);
            } catch (InterruptedException e) {
                Thread.interrupted();
                if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                    LOG.log(Level.FINE, "InterruptedException timeout = " + MAX_SENDING_WAIT + "\n\t" + toString(), e);
                }
                break;
            }
        }
        if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
            LOG.fine("Got result\n\t" + toString());
        }
        if (isClosed() && (SEND_INPROGRESS == sendResult)) {
            return false;
        }
        boolean result = (sendResult != SEND_FAIL);
        if (sendResult == SEND_INPROGRESS) {
            sendResult = SEND_TOOLONG;
            outgoingMessage = null;
        } else {
            sendResult = SEND_IDLE;
        }
        notifyAll();
        return result;
    }

    /**
     *  Retrieve a message from the "queue" of messages for the servlet.
     *
     *  @param timeout Number of milliseconds to wait for a message. Per Java
     *  convention 0 (zero) means wait forever.
     *  @return the message or <code>null</code> if no message was available
     *  before the timeout was reached.
     *  @throws InterruptedException If the thread is interrupted while waiting.
     */
    protected synchronized Message waitForMessage(long timeout) throws InterruptedException {
        if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
            LOG.fine("Waiting (" + (0 == timeout ? "forever" : Long.toString(timeout)) + ") for message\n\t" + toString());
        }
        if (0 == timeout) {
            timeout = Long.MAX_VALUE;
        }
        long absoluteTimeOut = TimeUtils.toAbsoluteTimeMillis(timeout);
        while (!isClosed() && (null == outgoingMessage)) {
            long waitfor = TimeUtils.toRelativeTimeMillis(absoluteTimeOut);
            if (waitfor <= 0) {
                break;
            }
            wait(waitfor);
        }
        Message result = outgoingMessage;
        outgoingMessage = null;
        if (!isClosed() && (result == null)) {
            sendResult = SEND_IDLE;
            notifyAll();
        }
        if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
            LOG.fine("Returning " + result + "\n\t" + toString());
        }
        return result;
    }

    protected synchronized void messageSent(boolean wasSuccessful) {
        if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
            LOG.fine("messageSent(" + wasSuccessful + ")\n\t" + toString());
        }
        if (SEND_TOOLONG == sendResult) {
            sendResult = SEND_IDLE;
        } else {
            sendResult = wasSuccessful ? SEND_SUCCESS : SEND_FAIL;
        }
        notifyAll();
    }

    /**
     * {@inheritDoc}
     *
     *  <p/>An implementation for debugging. Do not depend on the format.
     */
    @Override
    public String toString() {
        return "[" + super.toString() + "] isClosed=" + isClosed() + " sendResult=" + sendResult + " outmsg=" + outgoingMessage;
    }
}
