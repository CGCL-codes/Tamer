package org.opennms.netmgt.poller.monitors;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.opennms.netmgt.model.PollStatus;
import org.opennms.netmgt.poller.MonitoredService;
import org.opennms.netmgt.poller.NetworkInterface;
import org.opennms.netmgt.poller.ServiceMonitor;

/**
 * @author <a href="mailto:david@opennms.org">David Hustace</a>
 */
public class AvailabilityMonitorTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for {@link org.opennms.netmgt.poller.monitors.AvailabilityMonitor#poll(org.opennms.netmgt.poller.MonitoredService, Map)}.
     */
    public final void testPoll() {
        ServiceMonitor sm = new AvailabilityMonitor();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("timeout", "3000");
        MonitoredService svc = new MonitoredService() {

            protected InetAddress getNetworkAddress() {
                return getAddress();
            }

            public InetAddress getAddress() {
                try {
                    return InetAddress.getByName("127.0.0.1");
                } catch (UnknownHostException e) {
                    throw new IllegalStateException("Error getting localhost address", e);
                }
            }

            public String getIpAddr() {
                return getAddress().getHostAddress();
            }

            public NetworkInterface getNetInterface() {
                return new NetworkInterface() {

                    public Object getAddress() {
                        return getNetworkAddress();
                    }

                    public Object getAttribute(String property) {
                        return null;
                    }

                    public int getType() {
                        return 0;
                    }

                    public Object setAttribute(String property, Object value) {
                        return null;
                    }
                };
            }

            public int getNodeId() {
                return 0;
            }

            public String getNodeLabel() {
                return "localhost";
            }

            public String getSvcName() {
                return "ICMP";
            }
        };
        PollStatus status = sm.poll(svc, parameters);
        assertEquals(PollStatus.SERVICE_AVAILABLE, status.getStatusCode());
    }
}
