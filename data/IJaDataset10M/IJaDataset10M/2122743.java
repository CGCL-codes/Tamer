package perfectjpattern.core.behavioral.observer.data;

import junit.framework.TestCase;

/**
 * Test Suite for <code>ProgressData</code> implementation.
 * 
 * @see ProgressData
 *   
 * @author <a href="mailto:bravegag@hotmail.com">Giovanni Azua</a>
 * @version $Revision: 1.0 $ $Date: Jun 23, 2007 1:23:01 AM $
 */
public final class TestProgressData extends TestCase {

    public void testValueOf() {
        ProgressData myInstanceOne = new ProgressData(Status.STARTED, "There was a time", 0);
        ProgressData myInstanceTwo = new ProgressData(myInstanceOne);
        assertEquals("valueOf implemented incorrectly", myInstanceOne, myInstanceTwo);
    }

    public void testEquals() {
        assertEquals("Equals implemented incorrectly", ProgressData.STARTED, new ProgressData(Status.STARTED, "Task has started.", 0));
        assertTrue("Equals implemented incorrectly", !ProgressData.STARTED.equals(ProgressData.COMPLETED));
        assertTrue("equals() implemented incorrectly", !ProgressData.STARTED.equals(StatusData.STARTED));
    }

    public void testHashcode() {
        assertTrue("hashCode() implemented incorrectly", ProgressData.STARTED.hashCode() == ProgressData.STARTED.hashCode() && ProgressData.STARTED.equals(ProgressData.STARTED));
        ProgressData myData = new ProgressData(Status.STARTED, "Task has started.", 0);
        assertTrue("hashCode() implemented incorrectly", ProgressData.STARTED.hashCode() != myData.hashCode() && ProgressData.STARTED.equals(myData));
        assertTrue("hashCode() implemented incorrectly", !ProgressData.COMPLETED.equals(myData) && ProgressData.COMPLETED.hashCode() != myData.hashCode());
    }

    public void testToString() {
        assertEquals("toString() implemented incorrectly", ProgressData.STARTED.toString(), "ProgressData(StatusData(Status='Started', " + "Message='Task has started.'), Progress='0%')");
    }
}
