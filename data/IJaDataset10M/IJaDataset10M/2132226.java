package net.jxta.impl.endpoint;

import java.util.List;
import java.util.LinkedList;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import net.jxta.endpoint.EndpointAddress;
import net.jxta.endpoint.EndpointListener;
import net.jxta.endpoint.Message;
import net.jxta.impl.util.UnbiasedQueue;
import net.jxta.impl.util.ResourceDispatcher;
import net.jxta.util.ResourceAccount;
import net.jxta.impl.util.Cache;
import net.jxta.impl.util.CacheEntry;
import net.jxta.impl.util.CacheEntryListener;
import net.jxta.impl.util.TimeUtils;

/**
 *  A wrapper around an EndpointListener which imposes fair sharing quotas.
 *
 * <p/><b>NOTE</b>: 20020526 jice
 * There would be great value in making such an object the explicit interface
 * between the endpoint and its clients, rather than a bland listener interface
 * The client would have the ability to specify the threads limit, possibly
 * setting it zero, and then could have access to the buffer.
 * To implement that with a simple listener would mean that the endpoint
 * has to TRUST the client that the listener does no-more than queuing and
 * signaling or else, force a full and possibly redundant hand-shake in all
 * cases, as is the case now.  To be improved.
 */
public class QuotaIncomingMessageListener implements EndpointListener {

    /**
     *  Log4J Logger
     */
    private static final Logger LOG = Logger.getLogger(QuotaIncomingMessageListener.class.getName());

    /**
     * All QuotaIncomingMessageListener share one global resource manager for
     * threads. Its budget is hardcoded for now.
     *
     * <p/>Parameters read as follows:
     *
     * <p/><pre>
     * new ResourceDispatcher(
     *      100,  // support at least that many listeners
     *      1,    // each with at least that many reserved threads each
     *      5,    // let a listener reserve up to that many threads
     *      500,  // additional un-reserved threads
     *      50,   // any listener can have up to this many un-reserved threads
     *      20,   // threads that can never be reserved
     *      true  // use round robin
     *     );
     * </pre>
     *
     * <p/>It means that we will authorize up to 1000 threads total (<code>
     * 100 listeners * 5 reserved threads + 500 additional unreserved threads
     * </code>).
     *
     * <p/>We can support more than 100 listeners, but we cannot garantee
     * that we will be able to reserve even 1 thread for them. If we do
     * it will be by pulling it out of the un-reserved set if there are
     * any available at that time. If every listener uses only the minimal
     * garaunteed of 1 thread, then we can support 600 listeners (<code>
     * 100 listeners + 500 additional unreserved threads</code>).
     *
     * <p/>Round robin means that listeners that want to use threads beyond
     * their reservation are queued waiting for an extra thread to become
     * available.
     */
    private static ResourceDispatcher threadDispatcher = new ResourceDispatcher(100, 1, 3, 150, 6, 5, true, "threadDispatcher");

    /**
     * Max guaranteed supported message size (bytes). This is a theoretical
     * maximum message size and should reflect the maximum size of messages
     * used in relevant protocols. All other calculations assume this as the
     * "default" message size. If message size is very variable then this should
     *  be the median message size rather than the maximum.
     */
    static int GmaxMsgSize = 4 * 1024;

    /**
     * Max guaranteed senders (integer). Expected number of message sources
     * amongst whom resouces are to be shared.
     */
    static int GmaxSenders = 150;

    /**
     * Every sender account will always be granted 2 messages worth of queue
     * size.
     */
    static int GminResPerSender = 2 * GmaxMsgSize;

    /**
     * Every sender account can over allocate up to 5 messages worth of queue
     * size if the space is available. If peers are very bursty with messages
     * then this should be higher.
     */
    static int GmaxResPerSender = 2 * GminResPerSender;

    /**
     * Additional resources in reserve, to be allocated on the fly. Available
     * reservations beyond GminResPerSender are taken from there, so, we
     * must have enough. This space is fairly shared by all senders who are
     * over their minumum reserved allocation.
     */
    static int TotalExtra = 2 * GmaxResPerSender * GmaxSenders;

    /**
     * There is a limit to the amount of on-the-fly that a single sender can
     * hog. If peers are very bursty with messages then this should be higher.
     */
    static int MaxExtraPerSender = 10 * GmaxResPerSender;

    /**
     * There is a part of the non-reserved resources that we will never use for
     * reservation by senders in excess of GmaxSenders even if the number of
     * accounts is way beyond the max garaunteed. Instead we'll prefer to grant
     * 0 reserved items to additional senders.
     */
    static int NeverReserved = TotalExtra / 8;

    private static final ResourceDispatcher messageDispatcher = new ResourceDispatcher(GmaxSenders, GminResPerSender, GmaxResPerSender, TotalExtra, MaxExtraPerSender, NeverReserved, false, "messageDispatcher");

    /**
     * A canonical mapping of all the message originators.
     * This is a cache, since peers can disappear silently.
     *
     * <p/>Note: a number of accounts might not be the best criterion though.
     * since just a few stale accounts could hog a lot of resources.
     * May be we should just make that cache sensitive to the inconvenience
     * endured by live accounts.
     */
    static class MyCacheListener implements CacheEntryListener {

        public void purged(CacheEntry entry) {
            ((ResourceAccount) entry.getValue()).close();
        }
    }

    /**
     * We put a large hard limit on the cache because we detect the
     * need for purging normaly before that limit is reached, and we purge
     * it explicitly.
     *
     * <p/>The number 100 is the maximum number of idle accounts that
     * we keep around in case the peer comes back.
     */
    private static final Cache allSources = new Cache(100, new MyCacheListener());

    private final UnbiasedQueue messageQueue = new UnbiasedQueue(Integer.MAX_VALUE, false, new LinkedList());

    private final String name;

    private final ResourceAccount myAccount;

    /**
     *  The "real" listener.
     */
    private volatile EndpointListener listener = null;

    private boolean closed = false;

    /**
     *  The last time we warned about having a long queue.
     */
    private long lastLongQueueNotification = 0L;

    /**
     *  An incoming message in the queue with its addresses and accounting
     */
    static class MessageFromSource {

        final Message msg;

        final EndpointAddress srcAddress;

        final EndpointAddress destAddress;

        final ResourceAccount src;

        final long timeReceived;

        final long size;

        MessageFromSource(Message msg, EndpointAddress srcAddress, EndpointAddress destAddress, ResourceAccount src, long timeReceived, long size) {
            this.msg = msg;
            this.src = src;
            this.srcAddress = srcAddress;
            this.destAddress = destAddress;
            this.timeReceived = timeReceived;
            this.size = size;
        }
    }

    /**
     *  The thread which services removing items from the queue
     */
    static class ListenerThread extends Thread {

        private static final ThreadGroup listenerGroup = new ThreadGroup("Quota Incoming Message Listeners");

        private static final List idleThreads = new LinkedList();

        private QuotaIncomingMessageListener current;

        private boolean terminated = false;

        static ListenerThread newListenerThread(QuotaIncomingMessageListener current) {
            ListenerThread lt = null;
            synchronized (idleThreads) {
                if (idleThreads.isEmpty()) {
                    return new ListenerThread(current);
                }
                lt = (ListenerThread) idleThreads.remove(0);
            }
            lt.newJob(current);
            return lt;
        }

        private ListenerThread(QuotaIncomingMessageListener current) {
            super(listenerGroup, "QuotaListenerThread");
            this.current = current;
            setDaemon(true);
            start();
        }

        void newJob(QuotaIncomingMessageListener current) {
            synchronized (this) {
                this.current = current;
                notify();
            }
        }

        void terminate() {
            synchronized (idleThreads) {
                terminated = true;
            }
            interrupt();
        }

        boolean getJob() {
            synchronized (idleThreads) {
                if (terminated) {
                    return false;
                }
                idleThreads.add(0, this);
            }
            while (true) {
                synchronized (this) {
                    if (current != null) {
                        return true;
                    }
                    try {
                        wait(4 * TimeUtils.ASECOND);
                    } catch (InterruptedException ie) {
                        Thread.interrupted();
                    }
                    if (current != null) {
                        return true;
                    }
                }
                synchronized (idleThreads) {
                    if (idleThreads.remove(this)) {
                        return false;
                    }
                }
            }
        }

        public void run() {
            try {
                do {
                    while (current != null) {
                        current = current.doOne();
                    }
                } while (getJob());
            } catch (Throwable all) {
                LOG.fatal("Uncaught Throwable in thread :" + Thread.currentThread().getName(), all);
            }
        }
    }

    /**
     * Constructor for the QuotaIncomingMessageListener object
     *
     * @param  name             a unique name for this listener
     * @param  listener         the recipient listener.
    \    */
    public QuotaIncomingMessageListener(String name, EndpointListener listener) {
        this.listener = listener;
        this.name = name;
        synchronized (threadDispatcher) {
            myAccount = threadDispatcher.newAccount(1, -1, this);
            threadDispatcher.notify();
        }
        Thread.yield();
    }

    /**
     *  {@inheritDoc}
     *
     *  <p/>Returns our name
     */
    public String toString() {
        return name;
    }

    /**
     * Gets the listener attribute of the QuotaIncomingMessageListener object
     *
     * @return    The listener value
     */
    public EndpointListener getListener() {
        return listener;
    }

    /**
     *  Close this listener and release all of its resources.
     */
    public void close() {
        LinkedList rmdMessages = new LinkedList();
        synchronized (threadDispatcher) {
            if (closed) {
                return;
            }
            closed = true;
            listener = null;
            messageQueue.close();
            if (myAccount.isIdle()) {
                myAccount.close();
            }
            MessageFromSource mfs = null;
            while ((mfs = (MessageFromSource) messageQueue.pop()) != null) {
                rmdMessages.add(mfs);
            }
            threadDispatcher.notify();
        }
        Thread.yield();
        synchronized (messageDispatcher) {
            while (!rmdMessages.isEmpty()) {
                MessageFromSource mfs = (MessageFromSource) rmdMessages.removeFirst();
                mfs.src.inNeed(false);
                mfs.src.releaseQuantity(mfs.size);
                if (mfs.src.isIdle()) {
                    allSources.stickyCacheEntry((CacheEntry) mfs.src.getUserObject(), false);
                }
            }
        }
        rmdMessages = null;
    }

    /**
     * process one message and move on.
     */
    public QuotaIncomingMessageListener doOne() {
        MessageFromSource mfs = null;
        synchronized (threadDispatcher) {
            mfs = (MessageFromSource) messageQueue.pop();
            myAccount.inNeed(messageQueue.getCurrentInQueue() != 0);
            threadDispatcher.notify();
        }
        if (mfs != null) {
            synchronized (messageDispatcher) {
                mfs.src.inNeed(false);
                mfs.src.releaseQuantity(mfs.size);
                if (mfs.src.isIdle()) {
                    allSources.stickyCacheEntry((CacheEntry) mfs.src.getUserObject(), false);
                }
            }
            long timeDequeued = 0;
            EndpointListener l = listener;
            try {
                if (l != null) {
                    l.processIncomingMessage(mfs.msg, mfs.srcAddress, mfs.destAddress);
                }
            } catch (Throwable ignored) {
                if (LOG.isEnabledFor(Level.ERROR)) {
                    LOG.error("Uncaught Throwable in listener : " + this + "(" + l.getClass().getName() + ")", ignored);
                }
            }
        }
        ResourceAccount next;
        synchronized (threadDispatcher) {
            myAccount.inNeed(messageQueue.getCurrentInQueue() > 0);
            next = myAccount.releaseItem();
            if ((messageQueue.isClosed()) && myAccount.isIdle()) {
                myAccount.close();
            }
            threadDispatcher.notify();
        }
        if (next == null) {
            return null;
        }
        return (QuotaIncomingMessageListener) next.getUserObject();
    }

    /**
     *  {@inheritDoc}
     *
     * <p/>Try to give a new thread for this message (this listener).
     * Subsequently it will run other listenersaccording to what the dispatcher
     * says.
     */
    public void processIncomingMessage(Message message, EndpointAddress srcAddr, EndpointAddress dstAddr) {
        if (messageQueue.isClosed()) {
            return;
        }
        long timeReceived = 0;
        ResourceAccount msgSrcAccount;
        String srcAddrStr = srcAddr.toString();
        CacheEntry ce = null;
        long msgSize = message.getByteLength();
        int attempt = 0;
        while (true) {
            if (attempt > 0) {
                Thread.yield();
            }
            synchronized (messageDispatcher) {
                ce = allSources.getCacheEntry(srcAddrStr);
                if (ce == null) {
                    msgSrcAccount = (ResourceAccount) messageDispatcher.newAccount(4 * 10240, -1, srcAddrStr);
                    if (msgSrcAccount.getNbReserved() < 1) {
                        msgSrcAccount.close();
                        allSources.purge(10);
                        msgSrcAccount = (ResourceAccount) messageDispatcher.newAccount(4 * 10240, -1, "retrying:" + srcAddrStr);
                    }
                    allSources.put(srcAddrStr, msgSrcAccount);
                    ce = allSources.getCacheEntry(srcAddrStr);
                    msgSrcAccount.setUserObject(ce);
                } else {
                    msgSrcAccount = (ResourceAccount) ce.getValue();
                }
                if (!msgSrcAccount.obtainQuantity(msgSize)) {
                    if (++attempt < 2) {
                        continue;
                    }
                    if (LOG.isEnabledFor(Level.INFO)) {
                        LOG.info("Peer exceeds queuing limits; msg discarded.");
                    }
                    return;
                }
                allSources.stickyCacheEntry(ce, true);
                break;
            }
        }
        boolean obtained = false;
        boolean pushed = false;
        synchronized (threadDispatcher) {
            do {
                pushed = messageQueue.push(new MessageFromSource(message, srcAddr, dstAddr, msgSrcAccount, timeReceived, msgSize));
                if ((!pushed) && messageQueue.isClosed()) {
                    if (LOG.isEnabledFor(Level.DEBUG)) {
                        LOG.debug("queue closed, message discarded");
                    }
                    break;
                }
            } while (!pushed);
            if (LOG.isEnabledFor(Level.WARN)) {
                int queueLen = messageQueue.getCurrentInQueue();
                long timeNow = TimeUtils.timeNow();
                if ((queueLen > 100) && (TimeUtils.toRelativeTimeMillis(timeNow, lastLongQueueNotification) > TimeUtils.ASECOND)) {
                    lastLongQueueNotification = timeNow;
                    LOG.warn("Very long queue (" + queueLen + ") for listener: " + this);
                }
            }
            if (pushed) {
                obtained = myAccount.obtainItem();
            }
            threadDispatcher.notify();
        }
        if (!pushed) {
            synchronized (messageDispatcher) {
                msgSrcAccount.inNeed(false);
                msgSrcAccount.releaseQuantity(msgSize);
                if (msgSrcAccount.isIdle()) {
                    allSources.stickyCacheEntry(ce, false);
                }
            }
            return;
        }
        if (obtained) {
            ListenerThread.newListenerThread(this);
        } else {
            if (LOG.isEnabledFor(Level.INFO)) {
                LOG.info("Listener '" + this + "' exceeds thread's limits; msg waits.");
            }
        }
    }
}
