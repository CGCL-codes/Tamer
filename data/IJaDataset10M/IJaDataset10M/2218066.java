package org.opennms.netmgt.threshd;

import org.opennms.netmgt.config.DatabaseSchemaConfigFactory;
import org.opennms.netmgt.config.PollOutagesConfigFactory;
import org.opennms.netmgt.config.ThreshdConfigManager;
import org.opennms.netmgt.dao.support.RrdTestUtils;
import org.opennms.netmgt.eventd.EventIpcManagerFactory;
import org.opennms.netmgt.mock.MockEventIpcManager;
import org.opennms.netmgt.threshd.mock.MockThreshdConfigManager;
import org.opennms.test.ConfigurationTestUtils;
import org.opennms.test.mock.MockLogAppender;
import org.opennms.test.mock.MockUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ThreshdIntegrationTest extends ThresholderTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockLogAppender.setupLogging();
        setupDatabase();
        Resource dbConfig = new ClassPathResource("/org/opennms/netmgt/config/test-database-schema.xml");
        DatabaseSchemaConfigFactory dscf = new DatabaseSchemaConfigFactory(dbConfig.getInputStream());
        DatabaseSchemaConfigFactory.setInstance(dscf);
        RrdTestUtils.initializeNullStrategy();
        EventIpcManagerFactory.setIpcManager(new MockEventIpcManager());
        String dirName = "target/tmp/192.168.1.1";
        String fileName = "icmp.rrd";
        int nodeId = 1;
        String ipAddress = "192.168.1.1";
        String serviceName = "ICMP";
        String groupName = "icmp-latency";
        setupThresholdConfig(dirName, fileName, nodeId, ipAddress, serviceName, groupName);
        Resource resource = new ClassPathResource("etc/poll-outages.xml");
        PollOutagesConfigFactory.setInstance(new PollOutagesConfigFactory(resource.getInputStream()));
    }

    @Override
    protected void tearDown() throws Exception {
        MockLogAppender.assertNoWarningsOrGreater();
        MockUtil.println("------------ End Test " + getName() + " --------------------------");
    }

    public void testThreshd() throws Exception {
        Threshd threshd = new Threshd();
        ThreshdConfigManager config = new MockThreshdConfigManager(ConfigurationTestUtils.getInputStreamForResource(this, "threshd-configuration.xml"), "localhost", false);
        threshd.setThreshdConfig(config);
        threshd.init();
        threshd.start();
        Thread.sleep(5000);
        threshd.stop();
    }
}
