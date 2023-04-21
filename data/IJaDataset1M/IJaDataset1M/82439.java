package org.opennms.protocols.radius.monitor;

import static org.junit.Assert.assertEquals;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.core.test.OpenNMSJUnit4ClassRunner;
import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.dao.db.JUnitConfigurationEnvironment;
import org.opennms.netmgt.model.PollStatus;
import org.opennms.netmgt.poller.MonitoredService;
import org.opennms.netmgt.poller.ServiceMonitor;
import org.opennms.netmgt.poller.monitors.MonitorTestUtils;
import org.opennms.test.mock.MockLogAppender;
import org.opennms.test.mock.MockUtil;
import org.springframework.test.context.ContextConfiguration;

@RunWith(OpenNMSJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/opennms/emptyContext.xml" })
@JUnitConfigurationEnvironment
public class RadiusAuthMonitorTest {

    @Before
    public void setup() throws Exception {
        MockLogAppender.setupLogging();
    }

    @Test
    @Ignore("have to have a radius server set up")
    public void testResponse() throws Exception {
        final Map<String, Object> m = new ConcurrentSkipListMap<String, Object>();
        final ServiceMonitor monitor = new RadiusAuthMonitor();
        final MonitoredService svc = MonitorTestUtils.getMonitoredService(99, InetAddressUtils.addr("192.168.211.11"), "RADIUS");
        m.put("user", "testing");
        m.put("password", "password");
        m.put("retry", "1");
        m.put("secret", "testing123");
        m.put("authtype", "chap");
        final PollStatus status = monitor.poll(svc, m);
        MockUtil.println("Reason: " + status.getReason());
        assertEquals(PollStatus.SERVICE_AVAILABLE, status.getStatusCode());
    }
}
