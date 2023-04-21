package org.granite.gravity;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.granite.context.AMFContextImpl;
import org.granite.context.GraniteContext;
import org.granite.logging.Logger;
import org.granite.messaging.amf.AMF0Message;
import org.granite.messaging.webapp.HttpGraniteContext;
import flex.messaging.messages.AsyncMessage;

/**
 * @author Franck WOLFF
 */
public abstract class AbstractChannel implements Channel {

    private static final Logger log = Logger.getLogger(AbstractChannel.class);

    protected final String id;

    protected final ServletConfig servletConfig;

    protected final ConcurrentMap<String, Subscription> subscriptions = new ConcurrentHashMap<String, Subscription>();

    protected LinkedList<AsyncPublishedMessage> publishedQueue = new LinkedList<AsyncPublishedMessage>();

    protected final Lock publishedQueueLock = new ReentrantLock();

    protected LinkedList<AsyncMessage> receivedQueue = new LinkedList<AsyncMessage>();

    protected final Lock receivedQueueLock = new ReentrantLock();

    protected final AsyncPublisher publisher;

    protected final AsyncReceiver receiver;

    protected AbstractChannel(ServletConfig servletConfig, GravityConfig gravityConfig, String id) {
        if (id == null) throw new NullPointerException("id cannot be null");
        this.id = id;
        this.servletConfig = servletConfig;
        this.publisher = new AsyncPublisher(this);
        this.receiver = new AsyncReceiver(this);
    }

    protected abstract boolean hasAsyncHttpContext();

    protected abstract AsyncHttpContext acquireAsyncHttpContext();

    protected abstract void releaseAsyncHttpContext(AsyncHttpContext context);

    public String getId() {
        return id;
    }

    public Gravity getGravity() {
        return GravityManager.getGravity(getServletContext());
    }

    public Subscription addSubscription(String destination, String subTopicId, String subscriptionId, boolean noLocal) {
        Subscription subscription = new Subscription(this, destination, subTopicId, subscriptionId, noLocal);
        Subscription present = subscriptions.putIfAbsent(subscriptionId, subscription);
        return (present != null ? present : subscription);
    }

    public Collection<Subscription> getSubscriptions() {
        return subscriptions.values();
    }

    public Subscription removeSubscription(String subscriptionId) {
        return subscriptions.remove(subscriptionId);
    }

    public void publish(AsyncPublishedMessage message) throws MessagePublishingException {
        if (message == null) throw new NullPointerException("message cannot be null");
        publishedQueueLock.lock();
        try {
            publishedQueue.add(message);
        } finally {
            publishedQueueLock.unlock();
        }
        publisher.queue(getGravity());
    }

    public boolean hasPublishedMessage() {
        publishedQueueLock.lock();
        try {
            return !publishedQueue.isEmpty();
        } finally {
            publishedQueueLock.unlock();
        }
    }

    public boolean runPublish() {
        LinkedList<AsyncPublishedMessage> publishedCopy = null;
        publishedQueueLock.lock();
        try {
            if (publishedQueue.isEmpty()) return false;
            publishedCopy = publishedQueue;
            publishedQueue = new LinkedList<AsyncPublishedMessage>();
        } finally {
            publishedQueueLock.unlock();
        }
        for (AsyncPublishedMessage message : publishedCopy) {
            try {
                message.publish(this);
            } catch (Exception e) {
                log.error(e, "Error while trying to publish message: %s", message);
            }
        }
        return true;
    }

    public void receive(AsyncMessage message) throws MessageReceivingException {
        if (message == null) throw new NullPointerException("message cannot be null");
        Gravity gravity = getGravity();
        receivedQueueLock.lock();
        try {
            if (receivedQueue.size() + 1 > gravity.getGravityConfig().getMaxMessagesQueuedPerChannel()) throw new MessageReceivingException(message, "Could not queue message (channel's queue is full) for channel: " + this);
            receivedQueue.add(message);
        } finally {
            receivedQueueLock.unlock();
        }
        if (hasAsyncHttpContext()) receiver.queue(gravity);
    }

    public boolean hasReceivedMessage() {
        receivedQueueLock.lock();
        try {
            return !receivedQueue.isEmpty();
        } finally {
            receivedQueueLock.unlock();
        }
    }

    public boolean runReceive() {
        return runReceived(null);
    }

    public boolean runReceived(AsyncHttpContext asyncHttpContext) {
        boolean httpAsParam = (asyncHttpContext != null);
        LinkedList<AsyncMessage> messages = null;
        OutputStream os = null;
        try {
            receivedQueueLock.lock();
            try {
                if (receivedQueue.isEmpty()) return false;
                if (asyncHttpContext == null) {
                    asyncHttpContext = acquireAsyncHttpContext();
                    if (asyncHttpContext == null) return false;
                }
                messages = receivedQueue;
                receivedQueue = new LinkedList<AsyncMessage>();
            } finally {
                receivedQueueLock.unlock();
            }
            HttpServletRequest request = asyncHttpContext.getRequest();
            HttpServletResponse response = asyncHttpContext.getResponse();
            String correlationId = asyncHttpContext.getConnectMessage().getMessageId();
            AsyncMessage[] messagesArray = new AsyncMessage[messages.size()];
            int i = 0;
            for (AsyncMessage message : messages) {
                message.setCorrelationId(correlationId);
                messagesArray[i++] = message;
            }
            Gravity gravity = getGravity();
            GraniteContext context = HttpGraniteContext.createThreadIntance(gravity.getGraniteConfig(), gravity.getServicesConfig(), null, request, response);
            ((AMFContextImpl) context.getAMFContext()).setCurrentAmf3Message(asyncHttpContext.getConnectMessage());
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(AMF0Message.CONTENT_TYPE);
            response.setDateHeader("Expire", 0L);
            response.setHeader("Cache-Control", "no-store");
            os = response.getOutputStream();
            ObjectOutput amf3Serializer = context.getGraniteConfig().newAMF3Serializer(os);
            log.debug("<< [MESSAGES for channel=%s] %s", this, messagesArray);
            amf3Serializer.writeObject(messagesArray);
            os.flush();
            response.flushBuffer();
            return true;
        } catch (IOException e) {
            log.warn(e, "Could not send messages to channel: %s (retrying later)", this);
            GravityConfig gravityConfig = getGravity().getGravityConfig();
            if (messages != null && gravityConfig.isRetryOnError()) {
                receivedQueueLock.lock();
                try {
                    if (receivedQueue.size() + messages.size() > gravityConfig.getMaxMessagesQueuedPerChannel()) {
                        log.warn("Channel %s has reached its maximum queue capacity %s (throwing %s messages)", this, gravityConfig.getMaxMessagesQueuedPerChannel(), messages.size());
                    } else receivedQueue.addAll(0, messages);
                } finally {
                    receivedQueueLock.unlock();
                }
            }
            return true;
        } finally {
            try {
                GraniteContext.release();
            } catch (Exception e) {
            }
            try {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        log.warn(e, "Could not close output stream (ignored)");
                    }
                }
            } finally {
                if (!httpAsParam) releaseAsyncHttpContext(asyncHttpContext);
            }
        }
    }

    public void destroy() {
        Gravity gravity = getGravity();
        gravity.cancel(publisher);
        gravity.cancel(receiver);
        subscriptions.clear();
    }

    protected boolean queueReceiver() {
        if (hasReceivedMessage()) {
            receiver.queue(getGravity());
            return true;
        }
        return false;
    }

    protected ServletConfig getServletConfig() {
        return servletConfig;
    }

    protected ServletContext getServletContext() {
        return servletConfig.getServletContext();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Channel && id.equals(((Channel) obj).getId()));
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getName() + " {id=" + id + ", subscriptions=" + subscriptions.values() + "}";
    }
}
