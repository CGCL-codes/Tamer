package org.opennms.protocols.dhcp.monitor;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;
import org.apache.log4j.Level;
import org.opennms.core.utils.ParameterMap;
import org.opennms.core.utils.ThreadCategory;
import org.opennms.netmgt.dhcpd.Dhcpd;
import org.opennms.netmgt.model.PollStatus;
import org.opennms.netmgt.poller.Distributable;
import org.opennms.netmgt.poller.DistributionContext;
import org.opennms.netmgt.poller.MonitoredService;
import org.opennms.netmgt.poller.NetworkInterface;
import org.opennms.netmgt.poller.NetworkInterfaceNotSupportedException;
import org.opennms.netmgt.poller.monitors.AbstractServiceMonitor;

/**
 * This class is designed to be used by the service poller framework to test the
 * availability of the DHCP service on remote interfaces as defined by RFC 2131.
 *
 * This class relies on the DHCP API provided by JDHCP v1.1.1 (please refer to
 * <A HREF="http://www.dhcp.org/javadhcp">http://www.dhcp.org/javadhcp </A>).
 *
 * The class implements the ServiceMonitor interface that allows it to be used
 * along with other plug-ins by the service poller framework.
 *
 * @author <A HREF="mailto:tarus@opennms.org">Tarus Balog </A>
 * @author <A HREF="http://www.opennms.org/">OpenNMS </A>
 */
@Distributable(DistributionContext.DAEMON)
public final class DhcpMonitor extends AbstractServiceMonitor {

    /**
     * Default retries.
     */
    private static final int DEFAULT_RETRY = 0;

    /**
     * Default timeout. Specifies how long (in milliseconds) to block waiting
     * for data from the monitored interface.
     */
    private static final int DEFAULT_TIMEOUT = 3000;

    /**
     * {@inheritDoc}
     *
     * Poll the specified address for DHCP service availability.
     */
    public PollStatus poll(MonitoredService svc, Map<String, Object> parameters) {
        NetworkInterface<InetAddress> iface = svc.getNetInterface();
        if (iface.getType() != NetworkInterface.TYPE_INET) throw new NetworkInterfaceNotSupportedException("Unsupported interface type, only TYPE_INET currently supported");
        ThreadCategory log = ThreadCategory.getInstance(getClass());
        int retry = ParameterMap.getKeyedInteger(parameters, "retry", DEFAULT_RETRY);
        int timeout = ParameterMap.getKeyedInteger(parameters, "timeout", DEFAULT_TIMEOUT);
        InetAddress ipv4Addr = (InetAddress) iface.getAddress();
        if (log.isDebugEnabled()) log.debug("DhcpMonitor.poll: address: " + ipv4Addr + " timeout: " + timeout + " retry: " + retry);
        PollStatus serviceStatus = PollStatus.unavailable();
        long responseTime = -1;
        try {
            responseTime = Dhcpd.isServer(ipv4Addr, (long) timeout, retry);
            if (responseTime >= 0) {
                serviceStatus = PollStatus.available((double) responseTime);
            }
        } catch (IOException e) {
            e.fillInStackTrace();
            serviceStatus = logDown(Level.WARN, "An I/O exception occured during DHCP polling", e);
        } catch (Throwable e) {
            e.fillInStackTrace();
            serviceStatus = logDown(Level.WARN, "An unexpected exception occured during DHCP polling", e);
        }
        return serviceStatus;
    }
}
