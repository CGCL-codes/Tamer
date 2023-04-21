package lelouet.datacenter.simulation.vms;

import static org.mockito.Mockito.atLeastOnce;
import java.util.Collections;
import java.util.List;
import lelouet.datacenter.simulation.Event;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * test the {@link HPC} class
 * 
 * @author lelouet
 * 
 */
public class HPCTest {

    protected static long frequency = 1000;

    protected static long instructions = frequency * 3600;

    /** if we make an HPC vm sleep for 1H, there should not be any event */
    @Test
    public void testNoSleepProblem() {
        HPC toTest = new HPC("toTest", 0, 1000, instructions);
        toTest.work(1000);
        Assert.assertEquals(toTest.work(1000 * 3600).size(), 0);
    }

    /**
	 * test if, given enough memory and a 1000 MIPS frequency, the stop time is
	 * correct
	 */
    @Test
    public void testCorrectEnd() {
        final Object marker = Mockito.mock(Object.class);
        HPC toTest = new HPC("toTest", 0, 1000, instructions) {

            @Override
            protected List<Event> onJobDone() {
                marker.hashCode();
                return Collections.emptyList();
            }
        };
        toTest.work(1000);
        toTest.setAllocatedCPU(frequency);
        long nextEventTime = toTest.nextEventTime();
        Assert.assertEquals(nextEventTime, 1000 + (long) Math.ceil((double) instructions / frequency));
        List<Event> events = toTest.work(nextEventTime);
        Assert.assertEquals(events.size(), 1);
        Assert.assertEquals(toTest.getTime(), nextEventTime);
        Mockito.verify(marker, atLeastOnce()).hashCode();
        Assert.assertTrue(toTest.isFinished());
    }
}
