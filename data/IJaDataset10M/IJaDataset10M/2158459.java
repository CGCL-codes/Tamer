package org.avis.io;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.filterchain.IoFilterChain;
import org.apache.mina.core.session.IoSession;
import org.avis.io.messages.ConfConn;
import org.avis.io.messages.LivenessFailureMessage;
import org.avis.io.messages.Message;
import org.avis.io.messages.TestConn;
import static java.lang.Math.max;
import static java.lang.Math.random;
import static java.lang.System.currentTimeMillis;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.avis.logging.Log.trace;

/**
 * A MINA I/O filter that adds liveness checking using
 * TestConn/ConfConn. Generates LivenessFailureMessage's when
 * livenessTimeout passes and no response to a TestConn is seen within
 * receiveTimeout seconds.
 * 
 * @author Matthew Phillips
 */
public class LivenessFilter extends IoFilterAdapter implements IoFilter {

    protected long receiveTimeout;

    protected long livenessTimeout;

    protected String filterName;

    protected ScheduledExecutorService executor;

    /**
   * Create a new instance. Uses a {@link SharedExecutor}.
   * 
   * @param livenessTimeout The time (in millis) that must pass before
   *                a liveness check is issued.
   * @param receiveTimeout The amount of time (in millis) to wait for
   *                a reply.
   */
    public LivenessFilter(long livenessTimeout, long receiveTimeout) {
        this(null, livenessTimeout, receiveTimeout);
    }

    /**
   * Create a new instance.
   * 
   * @param executor The executor to use for timed callbacks.
   * @param livenessTimeout The time (in millis) that must pass before
   *                a liveness check is issued.
   * @param receiveTimeout The amount of time (in millis) to wait for
   *                a reply.
   */
    public LivenessFilter(ScheduledExecutorService executor, long livenessTimeout, long receiveTimeout) {
        this.executor = executor;
        this.livenessTimeout = livenessTimeout;
        this.receiveTimeout = receiveTimeout;
    }

    /**
   * Force dispose the tracker for a given session.
   */
    public static void dispose(IoSession session) {
        Tracker tracker = trackerFor(session);
        if (tracker != null) tracker.dispose();
    }

    public static LivenessFilter filterFor(IoSession session) {
        return trackerFor(session).filter();
    }

    public long livenessTimeout() {
        return livenessTimeout;
    }

    public static void setLivenessTimeoutFor(IoSession session, long newTimeout) {
        if (newTimeout < 1000) throw new IllegalArgumentException("Timeout cannot be < 1000: " + newTimeout);
        Tracker tracker = trackerFor(session);
        tracker.filter().livenessTimeout = newTimeout;
        tracker.timeoutUpdated();
    }

    public static void setReceiveTimeoutFor(IoSession session, long newTimeout) {
        if (newTimeout < 0) throw new IllegalArgumentException("Timeout cannot be < 0: " + newTimeout);
        Tracker tracker = trackerFor(session);
        tracker.filter().receiveTimeout = newTimeout;
        tracker.timeoutUpdated();
    }

    @Override
    public void onPreAdd(IoFilterChain parent, String name, NextFilter nextFilter) throws Exception {
        this.filterName = name;
        if (executor == null) executor = SharedExecutor.acquire();
    }

    @Override
    public void filterClose(NextFilter nextFilter, IoSession session) throws Exception {
        if (SharedExecutor.release(executor)) executor = null;
        nextFilter.filterClose(session);
    }

    @Override
    public void sessionOpened(NextFilter nextFilter, IoSession session) throws Exception {
        session.setAttribute("livenessTracker", new Tracker(session));
        nextFilter.sessionOpened(session);
    }

    static Tracker trackerFor(IoSession session) {
        return (Tracker) session.getAttribute("livenessTracker");
    }

    @Override
    public void sessionClosed(NextFilter nextFilter, IoSession session) throws Exception {
        Tracker tracker = trackerFor(session);
        if (tracker != null) tracker.dispose();
        nextFilter.sessionClosed(session);
    }

    @Override
    public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
        trackerFor(session).connectionIsLive();
        if (message == ConfConn.INSTANCE) {
            trace("Liveness confirmed: received ConfConn", this);
        } else if (message == TestConn.INSTANCE) {
            if (session.getScheduledWriteMessages() == 0) {
                session.write(ConfConn.INSTANCE);
                trace("Sent ConfConn in response to TestConn liveness check", this);
            } else {
                trace("Ignored TestConn: outgoing messages already in queue", this);
            }
        } else {
            nextFilter.messageReceived(session, message);
        }
    }

    /**
   * An instance of this is attached to each session to track liveness.
   */
    class Tracker {

        private IoSession session;

        private ScheduledFuture<?> livenessFuture;

        private long lastLive;

        private long lastTestConnCheck;

        public Tracker(IoSession session) {
            this.session = session;
            this.lastLive = currentTimeMillis();
            scheduleLivenessCheck((long) (random() * livenessTimeout));
        }

        public LivenessFilter filter() {
            return LivenessFilter.this;
        }

        public synchronized void dispose() {
            cancelLivenessCheck();
            session = null;
        }

        public boolean isDisposed() {
            return livenessFuture == null && session == null;
        }

        /**
     * Call to reset liveness timeout.
     */
        public synchronized void connectionIsLive() {
            lastLive = currentTimeMillis();
        }

        public void timeoutUpdated() {
            cancelLivenessCheck();
            lastLive = currentTimeMillis();
            scheduleLivenessCheck();
        }

        private void scheduleLivenessCheck() {
            scheduleLivenessCheck(max(0, livenessTimeout - (currentTimeMillis() - lastLive)));
        }

        private void scheduleLivenessCheck(long delay) {
            if (livenessFuture == null) {
                livenessFuture = executor.schedule(new Runnable() {

                    public void run() {
                        checkLiveness();
                    }
                }, delay, MILLISECONDS);
            }
        }

        private void cancelLivenessCheck() {
            if (livenessFuture != null) {
                livenessFuture.cancel(false);
                livenessFuture = null;
            }
        }

        /**
     * Check if liveness timeout has expired: if so send TestConn and
     * schedule checkConnReply ().
     */
        protected synchronized void checkLiveness() {
            livenessFuture = null;
            if (currentTimeMillis() - lastLive >= livenessTimeout) {
                trace("Liveness timeout: sending TestConn", this);
                lastTestConnCheck = currentTimeMillis();
                session.write(TestConn.INSTANCE);
                livenessFuture = executor.schedule(new Runnable() {

                    public void run() {
                        checkConnReply();
                    }
                }, receiveTimeout, MILLISECONDS);
            } else {
                scheduleLivenessCheck();
            }
        }

        /**
     * If no response seen to TestConn within replyTimeout, send
     * LivenessTimeoutMessage.
     */
        protected synchronized void checkConnReply() {
            livenessFuture = null;
            if (lastLive < lastTestConnCheck) {
                trace("No reply to TestConn: signaling liveness failure", this);
                injectMessage(new LivenessFailureMessage());
            } else {
                scheduleLivenessCheck();
            }
        }

        private void injectMessage(Message message) {
            NextFilter filter = session.getFilterChain().getNextFilter(filterName);
            filter.messageReceived(session, message);
        }
    }
}
