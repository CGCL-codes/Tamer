package org.knowhowlab.osgi.it.monitoradmin;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.knowhowlab.osgi.testing.commons.assertions.OSGiAssert;
import org.knowhowlab.osgi.testing.commons.assertions.ServiceAssert;
import org.knowhowlab.osgi.testing.commons.utils.BundleUtils;
import org.knowhowlab.osgi.testing.commons.utils.ServiceUtils;
import org.ops4j.pax.exam.Inject;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.service.monitor.MonitorAdmin;
import org.osgi.service.monitor.MonitorListener;
import org.osgi.service.monitor.MonitoringJob;
import static org.ops4j.pax.exam.CoreOptions.*;

/**
 * MonitorAdmin Integration test
 *
 * @author dpishchukhin
 */
@RunWith(JUnit4TestRunner.class)
public class MonitorAdminTest {

    @Configuration
    public static Option[] configuration() {
        return options(frameworks(equinox(), felix(), knopflerfish().version("3.0.0")), provision(mavenBundle().groupId("org.osgi").artifactId("org.osgi.compendium").version("4.1.0"), mavenBundle().groupId("org.apache.felix").artifactId("org.apache.felix.eventadmin").version("1.2.2"), mavenBundle().groupId("org.apache.felix").artifactId("org.apache.felix.log").version("1.0.0"), mavenBundle().groupId("org.knowhowlab.osgi.testing").artifactId("commons").version("1.0.1-SNAPSHOT"), mavenBundle().groupId("org.knowhowlab.osgi").artifactId("monitoradmin").version("1.0.2"), mavenBundle().groupId("org.knowhowlab.osgi.manual-tests").artifactId("test-monitorable").version("1.0.0-SNAPSHOT")));
    }

    @Inject
    private BundleContext bc;

    @Before
    public void initAsserts() {
        OSGiAssert.init(bc);
    }

    @Test
    public void testServices() throws BundleException {
        ServiceAssert.assertServiceAvailable(MonitorAdmin.class);
        ServiceAssert.assertServiceAvailable(MonitorListener.class);
        MonitorAdmin monitorAdmin = ServiceUtils.getService(bc, MonitorAdmin.class);
        String[] monitorableNames = monitorAdmin.getMonitorableNames();
        Assert.assertNotNull(monitorableNames);
        Assert.assertEquals(1, monitorableNames.length);
        MonitoringJob[] runningJobs = monitorAdmin.getRunningJobs();
        Assert.assertNotNull(runningJobs);
        Assert.assertEquals(0, runningJobs.length);
        Bundle bundle = BundleUtils.findBundle(bc, "org.knowhowlab.osgi.monitoradmin");
        bundle.stop();
        ServiceAssert.assertServiceUnavailable(MonitorAdmin.class);
        ServiceAssert.assertServiceUnavailable(MonitorListener.class);
    }
}
