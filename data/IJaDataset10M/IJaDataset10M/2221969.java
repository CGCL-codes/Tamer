package org.appspy.collector.client.test.osgi;

import org.appspy.collector.client.core.CollectorService;
import org.appspy.collector.client.core.CollectorServiceStatus;
import org.appspy.collector.client.core.ServiceRegistry;
import org.appspy.collector.client.core.bootstrap.factory.BootstrapServiceFactory;
import org.appspy.collector.core.data.AbstractCollectedData;
import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;
import org.springframework.osgi.test.AbstractConfigurableBundleCreatorTests;
import org.springframework.osgi.test.platform.Platforms;
import org.springframework.osgi.util.OsgiStringUtils;

/**
 * @author Olivier HEDIN / olivier@appspy.org
 */
public class BootstrapCollectorTest extends AbstractConfigurableBundleCreatorTests {

    @Override
    protected String getPlatformName() {
        return Platforms.EQUINOX;
    }

    public void testOsgiPlatformStarts() throws Exception {
        System.out.println(bundleContext.getProperty(Constants.FRAMEWORK_VENDOR));
        System.out.println(bundleContext.getProperty(Constants.FRAMEWORK_VERSION));
        System.out.println(bundleContext.getProperty(Constants.FRAMEWORK_EXECUTIONENVIRONMENT));
    }

    public void testOsgiEnvironment() throws Exception {
        Bundle[] bundles = bundleContext.getBundles();
        for (int i = 0; i < bundles.length; i++) {
            System.out.print(OsgiStringUtils.nullSafeName(bundles[i]));
            System.out.println(", ");
        }
        System.out.println();
    }

    protected String[] getTestBundlesNames() {
        return new String[] { "org.appspy.collector, org.appspy.collector.core.data, 2.0-SNAPSHOT", "org.appspy.collector, org.appspy.collector.client.core, 2.0-SNAPSHOT", "org.appspy.collector, org.appspy.collector.client.core.impl, 2.0-SNAPSHOT", "org.appspy.collector, org.appspy.collector.client.core.bootstrap, 2.0-SNAPSHOT", "org.appspy.collector, org.appspy.collector.client.core.bootstrap.factory, 2.0-SNAPSHOT", "org.appspy.collector, org.appspy.collector.client.core.bootstrap.osgi, 2.0-SNAPSHOT", "org.springframework.osgi, spring-osgi-core, 1.1.2", "org.springframework.osgi, spring-osgi-extender, 1.1.2" };
    }

    ;

    @SuppressWarnings("serial")
    public void testStart() {
        waitOnContextCreation("org.appspy.collector.org.appspy.collector.client.core.bootstrap.osgi");
        ServiceRegistry serviceRegistry = BootstrapServiceFactory.getServiceRegistry();
        CollectorService collectorService = (CollectorService) serviceRegistry.getService(CollectorService.NAME);
        assertNotNull("Collector service is null", collectorService);
        assertEquals(CollectorServiceStatus.RUNNING, collectorService.getStatus());
        collectorService.collect(new AbstractCollectedData() {
        });
        assertEquals(1, collectorService.getStatistics().getCollectedCount());
        collectorService.stop();
        assertEquals(CollectorServiceStatus.STOPPED, collectorService.getStatus());
    }
}
