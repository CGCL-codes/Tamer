package pl.rzarajczyk.utils.swing;

import org.junit.Ignore;
import junit.framework.Assert;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JFrame;
import org.junit.Before;
import static org.mockito.Mockito.*;

/**
 *
 * @author Rafal
 */
@Ignore
public class AggresiveFocusProtectorThreadTest {

    private AggresiveFocusProtector protector;

    private JFrame frame;

    private AtomicInteger counter;

    @Before
    public void setUp() {
        counter = new AtomicInteger(0);
        frame = mock(JFrame.class);
        doAnswer(new Answer<Void>() {

            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                counter.incrementAndGet();
                return null;
            }
        }).when(frame).requestFocus();
        protector = new AggresiveFocusProtector(frame);
    }

    @Test
    public void shouldBeStoppedRightAfterCreation() throws InterruptedException {
        Thread.sleep(100);
        Assert.assertEquals(0, counter.get());
    }

    @Test
    public void shouldBeProtectingAfterStart() throws InterruptedException {
        protector.setProtecting(true);
        Thread.sleep(100);
        protector.setProtecting(false);
        Assert.assertTrue("counter = " + counter.get(), counter.get() > 0);
    }

    @Test
    public void shouldStopProtectingAfterStop() throws InterruptedException {
        int c = executeProtectionSession();
        Thread.sleep(100);
        Assert.assertEquals(c, counter.get());
    }

    @Test
    public void shouldCorrectlyProtectWhenRequired() throws InterruptedException {
        int c1 = executeProtectionSession();
        Thread.sleep(100);
        int cb1 = counter.get();
        int c2 = executeProtectionSession();
        Thread.sleep(100);
        int cb2 = counter.get();
        int c3 = executeProtectionSession();
        Assert.assertTrue("c1 = " + c1 + "; c2 = " + c2, c1 < c2);
        Assert.assertTrue("c2 = " + c3 + "; c3 = " + c3, c2 < c3);
        Assert.assertEquals(c1, cb1);
        Assert.assertEquals(c2, cb2);
    }

    private int executeProtectionSession() throws InterruptedException {
        protector.setProtecting(true);
        Thread.sleep(100);
        protector.setProtecting(false);
        int c = counter.get();
        return c;
    }
}
