package org.opennms.jicmp.standalone;

import java.net.InetAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * PingReplyMetric
 *
 * @author brozow
 */
public class PingReplyMetric extends Metric implements PingReplyListener {

    CountDownLatch m_latch;

    int m_count;

    long m_interval;

    public PingReplyMetric(int count, long interval) {
        m_latch = new CountDownLatch(count);
        m_count = count;
        m_interval = interval;
    }

    @Override
    public void onPingReply(InetAddress address, PingReply reply) {
        try {
            update(reply.getElapsedTimeNanos());
        } finally {
            m_latch.countDown();
        }
    }

    public void await() throws InterruptedException {
        m_latch.await(m_interval * m_count + 1000, TimeUnit.MILLISECONDS);
    }
}
