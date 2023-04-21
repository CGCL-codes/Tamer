package org.opennms.netmgt.poller.monitors;

import static org.junit.Assert.assertEquals;
import static org.opennms.netmgt.poller.monitors.DNSResolutionMonitor.RESOLUTION_TYPE_PARM;
import static org.opennms.netmgt.poller.monitors.DNSResolutionMonitor.RT_BOTH;
import static org.opennms.netmgt.poller.monitors.DNSResolutionMonitor.RT_EITHER;
import static org.opennms.netmgt.poller.monitors.DNSResolutionMonitor.RT_V4;
import static org.opennms.netmgt.poller.monitors.DNSResolutionMonitor.RT_V6;
import java.net.InetAddress;
import java.util.Collections;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.opennms.netmgt.mock.MockMonitoredService;
import org.opennms.netmgt.model.PollStatus;
import org.opennms.netmgt.poller.monitors.DNSResolutionMonitor;
import org.opennms.test.mock.MockLogAppender;

/**
 * DNSResolutionMonitorTest
 *
 * @author brozow
 */
public class DNSResolutionMonitorTest {

    @Before
    public void setUp() {
        MockLogAppender.setupLogging(true);
    }

    @Test
    public void testPoll() throws Exception {
        MockMonitoredService dual = new MockMonitoredService(1, "wipv6day.opennms.org", InetAddress.getLocalHost(), "RESOLVE");
        MockMonitoredService v4only = new MockMonitoredService(1, "choopa-ipv4.opennms.org", InetAddress.getLocalHost(), "RESOLVE");
        MockMonitoredService v6only = new MockMonitoredService(1, "choopa-ipv6.opennms.org", InetAddress.getLocalHost(), "RESOLVE");
        MockMonitoredService neither = new MockMonitoredService(1, "no-such-name.example.com", InetAddress.getLocalHost(), "RESOLVE");
        DNSResolutionMonitor monitor = new DNSResolutionMonitor();
        monitor.initialize(Collections.<String, Object>emptyMap());
        monitor.initialize(dual);
        monitor.initialize(v4only);
        monitor.initialize(v6only);
        monitor.initialize(neither);
        Map<String, Object> v4Parms = Collections.<String, Object>singletonMap(RESOLUTION_TYPE_PARM, RT_V4);
        Map<String, Object> v6Parms = Collections.<String, Object>singletonMap(RESOLUTION_TYPE_PARM, RT_V6);
        Map<String, Object> bothParms = Collections.<String, Object>singletonMap(RESOLUTION_TYPE_PARM, RT_BOTH);
        Map<String, Object> eitherParms = Collections.<String, Object>singletonMap(RESOLUTION_TYPE_PARM, RT_EITHER);
        assertEquals(PollStatus.available(), monitor.poll(dual, v4Parms));
        assertEquals(PollStatus.available(), monitor.poll(dual, v6Parms));
        assertEquals(PollStatus.available(), monitor.poll(dual, bothParms));
        assertEquals(PollStatus.available(), monitor.poll(dual, eitherParms));
        assertEquals(PollStatus.available(), monitor.poll(v4only, v4Parms));
        assertEquals(PollStatus.unavailable(), monitor.poll(v4only, v6Parms));
        assertEquals(PollStatus.unavailable(), monitor.poll(v4only, bothParms));
        assertEquals(PollStatus.available(), monitor.poll(v4only, eitherParms));
        assertEquals(PollStatus.unavailable(), monitor.poll(v6only, v4Parms));
        assertEquals(PollStatus.available(), monitor.poll(v6only, v6Parms));
        assertEquals(PollStatus.unavailable(), monitor.poll(v6only, bothParms));
        assertEquals(PollStatus.available(), monitor.poll(v6only, eitherParms));
        assertEquals(PollStatus.unavailable(), monitor.poll(neither, v4Parms));
        assertEquals(PollStatus.unavailable(), monitor.poll(neither, v6Parms));
        assertEquals(PollStatus.unavailable(), monitor.poll(neither, bothParms));
        assertEquals(PollStatus.unavailable(), monitor.poll(neither, eitherParms));
        monitor.release(dual);
        monitor.release(v4only);
        monitor.release(v6only);
        monitor.release(neither);
        monitor.release();
    }
}
