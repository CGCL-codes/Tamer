package org.opennms.netmgt.alarmd.api.support;

import static org.junit.Assert.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.opennms.netmgt.alarmd.api.NorthboundAlarm;
import org.opennms.netmgt.alarmd.api.NorthbounderException;
import org.opennms.netmgt.alarmd.api.support.AbstractNorthbounder;
import org.opennms.netmgt.model.OnmsAlarm;

/**
 * Tests NBI Supporting abstract class
 * 
 * @author <a mailto:brozow@opennms.org>Matt Brozowski</a>
 * @author <a mailto:david@opennms.org>David Hustace</a>
 */
public class AbstractNorthbounderTest {

    public static class TestNorthbounder extends AbstractNorthbounder {

        private List<NorthboundAlarm> m_alarms;

        private boolean m_accepting;

        private CountDownLatch m_forwardAlarmsCalled = new CountDownLatch(1);

        private CountDownLatch m_acceptsCalled = new CountDownLatch(1);

        public TestNorthbounder() {
            super("TestNorthbounder");
        }

        @Override
        protected boolean accepts(NorthboundAlarm alarm) {
            m_acceptsCalled.countDown();
            return m_accepting;
        }

        @Override
        public void forwardAlarms(List<NorthboundAlarm> alarms) throws NorthbounderException {
            m_alarms = alarms;
            m_forwardAlarmsCalled.countDown();
        }

        public void waitForForwardToBeCalled(long waitTime) throws InterruptedException {
            m_forwardAlarmsCalled.await(waitTime, TimeUnit.MILLISECONDS);
        }

        public void waitForAcceptsToBeCalled(long waitTime) throws InterruptedException {
            m_acceptsCalled.await(waitTime, TimeUnit.MILLISECONDS);
        }

        public List<NorthboundAlarm> getAlarms() {
            return m_alarms;
        }

        public boolean isAccepting() {
            return m_accepting;
        }

        public void setAccepting(boolean accepting) {
            m_accepting = accepting;
        }
    }

    @Test
    public void testAlarmForwarding() throws InterruptedException {
        TestNorthbounder tnb = new TestNorthbounder();
        tnb.setAccepting(true);
        tnb.start();
        NorthboundAlarm a = createNorthboundAlarm(1);
        tnb.onAlarm(a);
        tnb.waitForAcceptsToBeCalled(2000);
        tnb.waitForForwardToBeCalled(2000);
        assertNotNull(tnb.getAlarms());
        assertTrue(tnb.getAlarms().contains(a));
    }

    @Test
    public void testAlarmNotAccepted() throws InterruptedException {
        TestNorthbounder tnb = new TestNorthbounder();
        tnb.setAccepting(false);
        tnb.start();
        tnb.onAlarm(createNorthboundAlarm(1));
        tnb.waitForAcceptsToBeCalled(2000);
        Thread.sleep(100);
        assertNull(tnb.getAlarms());
    }

    @Test
    public void testAlarmForwardingWithNagles() throws InterruptedException {
        TestNorthbounder tnb = new TestNorthbounder();
        tnb.setAccepting(true);
        tnb.setNaglesDelay(500);
        tnb.start();
        NorthboundAlarm a1 = createNorthboundAlarm(1);
        NorthboundAlarm a2 = createNorthboundAlarm(2);
        NorthboundAlarm a3 = createNorthboundAlarm(3);
        tnb.onAlarm(a1);
        Thread.sleep(100);
        tnb.onAlarm(a2);
        Thread.sleep(100);
        tnb.onAlarm(a3);
        tnb.waitForAcceptsToBeCalled(2000);
        tnb.waitForForwardToBeCalled(2000);
        assertNotNull(tnb.getAlarms());
        assertEquals(3, tnb.getAlarms().size());
        assertTrue(tnb.getAlarms().contains(a1));
        assertTrue(tnb.getAlarms().contains(a2));
        assertTrue(tnb.getAlarms().contains(a3));
    }

    private NorthboundAlarm createNorthboundAlarm(int alarmid) {
        OnmsAlarm alarm = new OnmsAlarm();
        alarm.setId(alarmid);
        alarm.setUei("uei.opennms.org/test/httpNorthBounder");
        return new NorthboundAlarm(alarm);
    }
}
