package org.nexopenframework.eh.management;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.Date;
import org.junit.Test;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Simple TestCase for {@link ExceptionStatistics} features. Mainly, we perform
 * tests for WatchDog functionality</p>
 * 
 * @see org.nexopenframework.eh.management.ExceptionStatistics
 * @author Francesc Xavier Magdaleno
 * @since 2.0.0.GA
 * @version $Revision $,$Date: 2009-08-14 22:48:44 +0100 $
 */
public class ExceptionStatisticsTest {

    /**Component to be tested*/
    private final ExceptionStatistics stats = new ExceptionStatistics();

    @Test
    public void processActivationWatchDog() {
        stats.setPeriod(10);
        stats.setThresholdSize(1);
        assertTrue(stats.isActiveWatchDog());
        stats.deactivateWatchDog();
        assertFalse(stats.isActiveWatchDog());
        stats.activateWatchDog();
        assertTrue(stats.isActiveWatchDog());
    }

    @Test
    public void processConcurrency() throws InterruptedException {
        stats.setPeriod(1);
        stats.setThresholdSize(1);
        stats.setTraceEnabled(true);
        stats.setPersistenceStorage("target/test");
        for (int j = 0; j < 15; j++) {
            for (int k = 0; k < 15; k++) {
                stats.updateStatistics(new Throwable(), "toString", "java.lang.Object", new Date());
            }
            assertNotNull(stats.toXML());
            Thread.sleep(10);
            for (int k = 0; k < 15; k++) {
                stats.updateStatistics(new Throwable("Controlled exception"), "toString", "Some controlled problem", "java.lang.Object", new Date());
                assertNotNull(stats.findCountByException("java.lang.Throwable"));
            }
            assertTrue(stats.isActiveWatchDog());
        }
    }
}
