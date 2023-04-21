package org.slasoi.gslam.monitoring.manager.tests;

import junit.framework.TestCase;
import org.slasoi.gslam.core.monitoring.IMonitoringManager;
import org.slasoi.gslam.monitoring.manager.MonitoringManagerBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Test Case for starting the MonitoringManagerBuilder service.
 **/
public class MonitoringManagerBuilderTestCase extends TestCase {

    /** An Application Context. **/
    private ApplicationContext ac;

    /**
     * Configures the XML application context.
     **/
    public final void setUp() {
        ac = new FileSystemXmlApplicationContext("src/main/resources/META-INF/spring/gslam-monitoring-manager-context.xml");
    }

    /**
     * Runs the test case.
     **/
    public final void testMonitoringManagerBuilder() {
        MonitoringManagerBuilder service = (MonitoringManagerBuilder) ac.getBean("monitoringManagerService");
        assertNotNull(service);
        IMonitoringManager mm = service.create();
    }
}
