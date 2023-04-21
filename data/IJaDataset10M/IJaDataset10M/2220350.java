package ch.sahits.codegen;

import static org.junit.Assert.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class QueuedActionElementTest {

    private QueuedActionElement e1This;

    private QueuedActionElement e2OtherCaller;

    private int check = 0;

    private Runnable action = new Runnable() {

        public void run() {
            check++;
        }
    };

    @Before
    public void setUp() throws Exception {
        check = 0;
        e1This = new QueuedActionElement(getClass().getName(), action);
        e2OtherCaller = new QueuedActionElement("some.phone.Class", action);
    }

    @Test
    public void testIs2Bprocessed() {
        Assert.assertTrue("The queued element should be processable from this class", e1This.is2Bprocessed());
        Assert.assertTrue("The queued element not should be processable from this class", !e2OtherCaller.is2Bprocessed());
    }

    @Test
    public void testProcess() {
        e1This.process();
        Assert.assertEquals("The counter should be increased by one", 1, check);
        e1This.process();
        Assert.assertEquals("The counter should be not be increased by one", 1, check);
    }

    @Test
    public void testGetProzessingClass() {
        Assert.assertEquals("The processing class should be " + getClass().getName(), getClass().getName(), e1This.getProzessingClass());
        Assert.assertEquals("The processing class should be some.phone.Class", "some.phone.Class", e2OtherCaller.getProzessingClass());
    }

    @Test
    public void testIsAlreadyProcessed() {
        Assert.assertTrue("The unprocessed Action should be marked as such", !e1This.isAlreadyProcessed());
        e1This.process();
        assertTrue("The processed action should be marked as such", e1This.isAlreadyProcessed());
    }
}
