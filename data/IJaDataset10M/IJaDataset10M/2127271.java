package gov.sns.apps.scope;

import gov.sns.ca.Channel;
import gov.sns.ca.ChannelFactory;
import gov.sns.ca.ChannelTimeRecord;
import gov.sns.ca.ConnectionException;
import gov.sns.ca.IEventSinkValTime;
import gov.sns.ca.Monitor;
import gov.sns.ca.MonitorException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Craig McChesney
 */
public class MonitorTest extends TestCase {

    private static long SLEEP = 60000;

    private class ValueListener implements IEventSinkValTime {

        private int events = 0;

        private long start;

        private Channel channel;

        private Monitor monitor;

        public ValueListener(String pvName) {
            channel = ChannelFactory.defaultFactory().getChannel(pvName);
            try {
                monitor = channel.addMonitorValTime(this, Monitor.VALUE);
            } catch (ConnectionException e1) {
                fail("ConnectionException on: " + pvName);
            } catch (MonitorException e1) {
                fail("MonitorException on: " + pvName);
            }
        }

        public synchronized void eventValue(ChannelTimeRecord record, Channel chan) {
            if (events == 0) start = System.currentTimeMillis();
            events++;
        }

        private long getDuration() {
            return System.currentTimeMillis() - start;
        }

        public void debugPrint() {
            System.out.println("PV: " + channel.getId());
            System.out.println("handled events: " + events);
            System.out.println("event frequency: " + getDuration() / events);
            System.out.println();
        }
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(MonitorTest.class);
    }

    public static Test suite() {
        return new TestSuite(MonitorTest.class);
    }

    private ValueListener addMonitor(String pvName) {
        Channel c1 = ChannelFactory.defaultFactory().getChannel(pvName);
        ValueListener l = new ValueListener(pvName);
        try {
            c1.addMonitorValTime(l, Monitor.VALUE);
        } catch (ConnectionException e1) {
            fail("ConnectionException on: " + pvName);
        } catch (MonitorException e1) {
            fail("MonitorException on: " + pvName);
        }
        return l;
    }

    private void waitForMonitor() {
        try {
            Thread.sleep(SLEEP);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testCAM() {
        ValueListener l1 = addMonitor("CAM:sine0");
        ValueListener l2 = addMonitor("CAM:sine1");
        ValueListener l3 = addMonitor("CAM:cos0");
        waitForMonitor();
        l1.debugPrint();
        l2.debugPrint();
        l3.debugPrint();
    }

    public void testLlrf() {
        Channel rate = ChannelFactory.defaultFactory().getChannel("Test_LLRF:FCM13:HBInt_Rate");
        ValueListener l1 = addMonitor("Test_LLRF:FCM13:Fwd_WfA");
        ValueListener l2 = addMonitor("Test_LLRF:FCM13:Rfl_WfA");
        ValueListener l3 = addMonitor("Test_LLRF:FCM13:Field_WfA");
        waitForMonitor();
        l1.debugPrint();
        l2.debugPrint();
        l3.debugPrint();
    }
}
