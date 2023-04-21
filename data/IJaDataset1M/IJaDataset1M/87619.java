package net.kano.joscar.ratelim;

import junit.framework.TestCase;
import net.kano.joscar.flapcmd.SnacCommand;
import net.kano.joscar.snac.SnacRequest;
import net.kano.joscar.snaccmd.conn.RateClassInfo;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class RateQueueTest extends TestCase {

    private RateQueue queue;

    private int sent;

    private int possible;

    protected void setUp() throws Exception {
        super.setUp();
        sent = 0;
        possible = 0;
        queue = new RateQueue(new NeverPausedConnectionQueueMgr(), new DummyRateClassMonitor(), new CountingAndImmediatelyDequeueingSender());
    }

    /**
   * Tests that when {@code sendAndDequeueReadyRequestsIfPossible} is called
   * multiple times before SNAC commands are actually sent, it does not send
   * more commands than it's supposed to send according to the rate class
   * monitor. This simulates a situation where multiple threads could call
   * {@code sendAndDequeueReadyRequestsIfPossible} simultaneously.
   * <br><br>
   * For more information on this behavior, see
   * {@link RateQueue#dequeueReadyRequests()} 
   * <br><br>
   * The behavior is simulated by calling
   * {@code sendAndDequeueReadyRequestsIfPossible} inside the
   * {@code SnacRequestSender}'s send method, before registering the commands as
   * sent (which would update the rate monitor).
   */
    public void testDequeuedRequestListWorks() {
        for (int i = 0; i < 8; i++) {
            queue.enqueue(new SnacRequest(new DummySnacCommand()));
        }
        possible = 2;
        queue.sendAndDequeueReadyRequestsIfPossible();
        assertEquals(2, sent);
        assertEquals(6, queue.getQueueSize());
        possible = 1;
        queue.sendAndDequeueReadyRequestsIfPossible();
        assertEquals(3, sent);
        assertEquals(5, queue.getQueueSize());
        possible = 6;
        queue.sendAndDequeueReadyRequestsIfPossible();
        assertEquals(8, sent);
        assertEquals(0, queue.getQueueSize());
        queue.sendAndDequeueReadyRequestsIfPossible();
        assertEquals(8, sent);
        assertEquals(0, queue.getQueueSize());
    }

    private class DummyRateClassMonitor implements RateClassMonitor {

        public int getPossibleCmdCount() {
            return possible;
        }

        public RateClassInfo getRateInfo() {
            throw new UnsupportedOperationException();
        }

        public int getErrorMargin() {
            throw new UnsupportedOperationException();
        }

        public int getLocalErrorMargin() {
            throw new UnsupportedOperationException();
        }

        public void setErrorMargin(int errorMargin) {
            throw new UnsupportedOperationException();
        }

        public boolean isLimited() {
            throw new UnsupportedOperationException();
        }

        public long getLastRateAvg() {
            throw new UnsupportedOperationException();
        }

        public long getPotentialAvg() {
            throw new UnsupportedOperationException();
        }

        public long getPotentialAvg(long time) {
            throw new UnsupportedOperationException();
        }

        public long getOptimalWaitTime() {
            throw new UnsupportedOperationException();
        }

        public long getTimeUntil(long minAvg) {
            throw new UnsupportedOperationException();
        }

        public int getMaxCmdCount() {
            throw new UnsupportedOperationException();
        }
    }

    private static class DummySnacCommand extends SnacCommand {

        public DummySnacCommand() {
            super(0, 0);
        }

        public void writeData(OutputStream out) throws IOException {
        }
    }

    private static class NeverPausedConnectionQueueMgr implements ConnectionQueueMgr {

        public boolean isPaused() {
            return false;
        }
    }

    private class CountingAndImmediatelyDequeueingSender implements SnacRequestSender {

        public void sendRequests(List<SnacRequest> toSend) {
            queue.sendAndDequeueReadyRequestsIfPossible();
            for (SnacRequest request : toSend) {
                queue.removePending(request);
                sent++;
            }
        }
    }
}
