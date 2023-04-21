package net.sf.sasl.common.schedule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import net.sf.sasl.common.schedule.ScheduledExecutorServiceAdapter;
import net.sf.sasl.common.schedule.ScheduledFutureDecorator;
import org.easymock.classextension.EasyMock;
import org.easymock.classextension.IMocksControl;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for the
 * {@link net.sf.sasl.common.schedule.ScheduledExecutorServiceAdapter
 * ScheduledExecutorServiceAdapter}.
 * 
 * @author Philipp Förmer
 * 
 */
public class ScheduledExecutorServiceAdapterTest {

    /**
	 * Unit under test
	 */
    private ScheduledExecutorServiceAdapter underTest;

    /**
	 * Mock control.
	 */
    private IMocksControl mockControl;

    /**
	 * Scheduler service mock.
	 */
    private ScheduledExecutorService schedulerServiceMock;

    @Before
    public void setUp() {
        underTest = new ScheduledExecutorServiceAdapter();
        mockControl = EasyMock.createControl();
        schedulerServiceMock = mockControl.createMock(ScheduledExecutorService.class);
    }

    /**
	 * Test cases for
	 * {@link net.sf.sasl.common.schedule.ScheduledExecutorServiceAdapter#getScheduledExecutorService()
	 * getScheduledExecutorService()} and
	 * {@link net.sf.sasl.common.schedule.ScheduledExecutorServiceAdapter#setScheduledExecutorService(ScheduledExecutorService)
	 * setScheduledExecutorService(ScheduledExecutorService)}.
	 * 
	 * @throws Exception
	 */
    @Test
    public void testGetSetScheduledExecutorService() throws Exception {
        assertNull(underTest.getScheduledExecutorService());
        underTest.setScheduledExecutorService(schedulerServiceMock);
        assertEquals(schedulerServiceMock, underTest.getScheduledExecutorService());
    }

    /**
	 * Test cases for
	 * {@link net.sf.sasl.common.schedule.ScheduledExecutorServiceAdapter#schedule(Callable, long)
	 * schedule(Callable, long)}
	 * 
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
    @Test
    public void testSchedule() throws Exception {
        underTest.setScheduledExecutorService(schedulerServiceMock);
        Callable<Integer> callable = new Callable<Integer>() {

            public Integer call() throws Exception {
                return 24;
            }
        };
        ScheduledFuture<Integer> scheduledFutureMock = mockControl.createMock(ScheduledFuture.class);
        EasyMock.expect(schedulerServiceMock.schedule((ScheduledFutureDecorator) EasyMock.anyObject(), EasyMock.eq(5L), EasyMock.eq(TimeUnit.MILLISECONDS))).andReturn(scheduledFutureMock);
        mockControl.replay();
        ScheduledFuture<Integer> isScheduledFuture = underTest.schedule(callable, 5);
        mockControl.verify();
    }
}
