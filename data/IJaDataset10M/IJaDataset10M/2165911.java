package org.opennms.jicmp.standalone;

import java.util.concurrent.TimeUnit;

/**
 * PingReply
 *
 * @author brozow
 */
interface PingReply {

    public abstract long getSentTimeNanos();

    public abstract long getReceivedTimeNanos();

    public abstract long getElapsedTimeNanos();

    public abstract double elapsedTime(TimeUnit unit);

    public abstract long getThreadId();
}
