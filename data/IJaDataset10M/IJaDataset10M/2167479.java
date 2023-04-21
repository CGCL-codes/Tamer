package nl.utwente.ewi.hmi.deira.queue;

public class EventComparatorTest {

    /**
	 * Run to test class EventComparator
	 * 
	 * @param arg
	 *            N/A
	 */
    public static void main(String[] arg) {
        EventComparator<Object> comparator = new EventComparator<Object>();
        HREvent e1;
        HREvent e2;
        e1 = new HREvent(1, 0, "TEST", "1", null, 4.99f, 0, null);
        e2 = new HREvent(2, 0, "TEST", "2", null, 5.0f, 0, null);
        if (comparator.compare(e1, e2) < 0) System.out.println("Test1a: PASS"); else System.out.println("Test1a: FAILED");
        e1 = new HREvent(1, 0, "TEST", "1", null, 0.0f, 0, null);
        e2 = new HREvent(2, 0, "TEST", "2", null, 5.0f, 0, null);
        if (comparator.compare(e1, e2) < 0) System.out.println("Test1b: PASS"); else System.out.println("Test1b: FAILED");
        e2 = new HREvent(2, 0, "TEST", "2", null, 5.0f, 0, null);
        synchronized (e2) {
            try {
                e2.wait(20);
            } catch (InterruptedException ex) {
            }
        }
        e1 = new HREvent(1, 0, "TEST", "1", null, 5.0f, 0, null);
        if (comparator.compare(e1, e2) < 0) System.out.println("Test1c: PASS"); else System.out.println("Test1c: FAILED: " + e1.getCreateTime() + " " + e2.getCreateTime());
        e1 = new HREvent(1, 0, "TEST", "1", null, 5.0f, 0, null);
        e2 = new HREvent(2, 0, "TEST", "2", null, 4.99f, 0, null);
        if (comparator.compare(e1, e2) > 0) System.out.println("Test2a: PASS"); else System.out.println("Test2a: FAILED");
        e1 = new HREvent(1, 0, "TEST", "1", null, 5.0f, 0, null);
        e2 = new HREvent(2, 0, "TEST", "2", null, 0.0f, 0, null);
        if (comparator.compare(e1, e2) > 0) System.out.println("Test2b: PASS"); else System.out.println("Test2b: FAILED");
        e1 = new HREvent(1, 0, "TEST", "1", null, 5.0f, 0, null);
        synchronized (e1) {
            try {
                e1.wait(20);
            } catch (InterruptedException ex) {
            }
        }
        e2 = new HREvent(2, 0, "TEST", "2", null, 5.0f, 0, null);
        if (comparator.compare(e1, e2) > 0) System.out.println("Test2c: PASS"); else System.out.println("Test2c: FAILED: " + e1.getCreateTime() + " " + e2.getCreateTime());
        e2 = new HREvent(2, 0, "TEST", "2", null, 5.0f, 0, null);
        e1 = new HREvent(1, 0, "TEST", "1", null, 5.0f, 0, null);
        if (comparator.compare(e1, e2) == 0) System.out.println("Test3: PASS"); else System.out.println("Test3: FAILED");
    }
}
