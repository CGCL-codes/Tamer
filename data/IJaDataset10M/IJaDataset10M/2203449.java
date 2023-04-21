package org.jmonit.monitors;

import org.jmonit.AbstractRepositoryTestCase;
import org.jmonit.Monitor;
import org.jmonit.features.Statistics;

/**
 * @author <a href="mailto:nicolas.deloof@gmail.com">Nicolas De Loof</a>
 */
public class DefaultMonitorTest extends AbstractRepositoryTestCase {

    public void testRegister() throws Exception {
        Monitor monitor = repository.getMonitor("test");
        assertTrue(!monitor.isFeatureSupported(Statistics.class));
        monitor.tag("perf");
        assertTrue(monitor.isFeatureSupported(Statistics.class));
        monitor.add(12);
        Statistics stats = monitor.getFeature(Statistics.class);
        assertEquals(12, stats.getMax());
    }
}
