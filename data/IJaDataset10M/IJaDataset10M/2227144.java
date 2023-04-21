package performance.monitor;

import org.scribble.monitor.*;
import org.scribble.monitor.impl.DefaultMonitorContext;
import org.scribble.monitor.model.ConversationType;

public class TestMonitor implements Runnable {

    private static final int NUMBER_OF_SERIALIZATIONS = 1000;

    private static final int NUMBER_OF_ITERATIONS = 1000000;

    public static void main(String[] args) {
        TestMonitor app = new TestMonitor(new MonitorTestCase6());
        app.run();
    }

    public TestMonitor(MonitorTestCase mtc) {
        m_monitorTestCase = mtc;
    }

    public void run() {
        try {
            ConversationTypeRepository sm = m_monitorTestCase.getConversationTypeRepository();
            int len = 0;
            long serstarttime = System.currentTimeMillis();
            for (int i = 0; i < NUMBER_OF_SERIALIZATIONS; i++) {
                try {
                    java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
                    java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(baos);
                    oos.writeObject(sm);
                    byte[] b = baos.toByteArray();
                    len = b.length;
                    baos.close();
                    java.io.ByteArrayInputStream bias = new java.io.ByteArrayInputStream(b);
                    java.io.ObjectInputStream ois = new java.io.ObjectInputStream(bias);
                    Object obj = ois.readObject();
                    bias.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (m_serializeBeforeUse) {
                try {
                    java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
                    java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(baos);
                    oos.writeObject(sm);
                    byte[] b = baos.toByteArray();
                    len = b.length;
                    java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(b);
                    java.io.ObjectInputStream ois = new java.io.ObjectInputStream(bais);
                    sm = (ConversationTypeRepository) ois.readObject();
                    System.out.println("Deserialized");
                    ois.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Serialization time=" + ((double) (System.currentTimeMillis() - serstarttime) / NUMBER_OF_SERIALIZATIONS) + " ms");
            System.out.println("Description length is " + len);
            DefaultMonitorContext context = new DefaultMonitorContext();
            context.setConversationTypeRepository(sm);
            Monitor monitor = MonitorFactory.getMonitor(context);
            ConversationTypeName[] names = m_monitorTestCase.getObservationNames();
            for (int i = 0; i < names.length; i++) {
                monitor.observe(names[i]);
            }
            if (m_debug) {
                monitor.setConversationListener(new ConversationListener() {

                    public void finished(ConversationIdentity[] ids) {
                        System.out.println("FINISHED: " + ids);
                    }

                    public void received(Message mesg) {
                        System.out.println("RECEIVED: " + mesg.getSignature());
                    }

                    public void receivedOutOfSequence(Message mesg) {
                        System.out.println("RECEIVED OOS: " + mesg.getSignature());
                    }

                    public void sent(Message mesg) {
                        System.out.println("SENT: " + mesg.getSignature());
                    }

                    public void sentOutOfSequence(Message mesg) {
                        System.out.println("SENT OOS: " + mesg.getSignature());
                    }

                    public void started(ConversationIdentity[] ids) {
                        System.out.println("STARTED: " + ids);
                    }
                });
                System.out.println("-----(first iter)--------");
                runTestIteration(monitor, m_monitorTestCase);
                System.out.println("-----(second iter)--------");
                runTestIteration(monitor, m_monitorTestCase);
            } else {
                long time1 = runTests(monitor, m_monitorTestCase);
                Monitor monitor2 = new DummyMonitor();
                long time2 = runTests(monitor2, m_monitorTestCase);
                System.out.println("Test Results:");
                System.out.println("Monitored = " + time1);
                System.out.println("Dummy     = " + time2);
                System.out.println("--------------------");
                System.out.println("Difference= " + (time1 - time2));
                System.out.println("Factor= " + ((double) (time1 - time2) / (double) time2));
                System.out.println("--------------------");
                System.out.println("Monitored/Iterations = " + ((double) time1 / NUMBER_OF_ITERATIONS) + " ms");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long runTests(Monitor monitor, MonitorTestCase mtc) {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            runTestIteration(monitor, mtc);
        }
        return (System.currentTimeMillis() - startTime);
    }

    protected void runTestIteration(Monitor monitor, MonitorTestCase mtc) {
        for (int i = 0; i < mtc.getTestData().length; i++) {
            TestData td = mtc.getTestData()[i];
            boolean res = false;
            if (td.isSend()) {
                res = monitor.sent(td.getMessage());
            } else {
                res = monitor.received(td.getMessage());
            }
            if (m_debug) System.out.println("TEST: " + td + " Result=" + res);
        }
    }

    public void setDebug(boolean d) {
        m_debug = d;
    }

    private boolean m_debug = false;

    private boolean m_serializeBeforeUse = true;

    private MonitorTestCase m_monitorTestCase = null;

    public static class DummyMonitor implements Monitor {

        public void observe(ConversationTypeName name) throws MonitorException {
        }

        public boolean received(Message mesg) {
            return true;
        }

        public boolean sent(Message mesg) {
            return true;
        }

        public void setDescription(ConversationType description) {
        }

        public void setConversationListener(ConversationListener l) {
        }

        public void close() {
        }
    }
}
