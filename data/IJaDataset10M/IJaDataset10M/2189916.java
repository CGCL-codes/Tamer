package org.opennms.netmgt.poller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.util.Map;
import org.apache.log4j.Category;
import org.apache.log4j.Priority;
import org.opennms.core.utils.ThreadCategory;
import org.opennms.netmgt.utils.ParameterMap;

/**
 * This class is designed to be used by the service poller framework to test the availability
 * of a generic TCP service on remote interfaces. The class implements the ServiceMonitor
 * interface that allows it to be used along with other plug-ins by the service poller
 * framework.
 * 
 * @author <A HREF="mailto:tarus@opennms.org">Tarus Balog </A>
 * @author <A HREF="mike@opennms.org">Mike </A>
 * @author Weave
 * @author <A HREF="http://www.opennms.org/">OpenNMS </A>
 *  
 */
final class SshMonitor extends IPv4LatencyMonitor {

    /**
     * Default port.
     */
    private static final int DEFAULT_PORT = -1;

    /**
     * Default retries.
     */
    private static final int DEFAULT_RETRY = 0;

    /**
     * Default timeout. Specifies how long (in milliseconds) to block waiting for data from the
     * monitored interface.
     */
    private static final int DEFAULT_TIMEOUT = 3000;

    /**
     * Poll the specified address for service availability.
     * 
     * During the poll an attempt is made to connect on the specified port. If the connection
     * request is successful, the banner line generated by the interface is parsed and if the
     * banner text indicates that we are talking to Provided that the interface's response is
     * valid we set the service status to SERVICE_AVAILABLE and return.
     * 
     * @param iface
     *            The network interface to test the service on.
     * @param parameters
     *            The package parameters (timeout, retry, etc...) to be used for this poll.
     * 
     * @return The availibility of the interface and if a transition event should be supressed.
     * 
     * @throws java.lang.RuntimeException
     *             Thrown if the interface experiences errors during the poll.
     */
    public int poll(NetworkInterface iface, Map parameters, org.opennms.netmgt.config.poller.Package pkg) {
        Category log = ThreadCategory.getInstance(getClass());
        if (iface.getType() != NetworkInterface.TYPE_IPV4) throw new NetworkInterfaceNotSupportedException("Unsupported interface type, only TYPE_IPV4 currently supported");
        int retry = ParameterMap.getKeyedInteger(parameters, "retry", DEFAULT_RETRY);
        int timeout = ParameterMap.getKeyedInteger(parameters, "timeout", DEFAULT_TIMEOUT);
        String rrdPath = ParameterMap.getKeyedString(parameters, "rrd-repository", null);
        String dsName = ParameterMap.getKeyedString(parameters, "ds-name", null);
        if (rrdPath == null) {
            log.info("poll: RRD repository not specified in parameters, latency data will not be stored.");
        }
        if (dsName == null) {
            dsName = DS_NAME;
        }
        int port = ParameterMap.getKeyedInteger(parameters, "port", DEFAULT_PORT);
        if (port == DEFAULT_PORT) {
            throw new RuntimeException("SshMonitor: required parameter 'port' is not present in supplied properties.");
        }
        String strBannerMatch = (String) parameters.get("banner");
        InetAddress ipv4Addr = (InetAddress) iface.getAddress();
        if (log.isDebugEnabled()) log.debug("poll: address = " + ipv4Addr.getHostAddress() + ", port = " + port + ", timeout = " + timeout + ", retry = " + retry);
        int serviceStatus = SERVICE_UNAVAILABLE;
        long responseTime = -1;
        for (int attempts = 0; attempts <= retry && serviceStatus != SERVICE_AVAILABLE; attempts++) {
            Socket socket = null;
            try {
                long sentTime = System.currentTimeMillis();
                socket = new Socket();
                socket.connect(new InetSocketAddress(ipv4Addr, port), timeout);
                socket.setSoTimeout(timeout);
                log.debug("SshMonitor: connected to host: " + ipv4Addr + " on port: " + port);
                serviceStatus = SERVICE_UNRESPONSIVE;
                if (strBannerMatch == null || strBannerMatch.equals("*")) {
                    serviceStatus = SERVICE_AVAILABLE;
                    break;
                }
                BufferedReader rdr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String response = rdr.readLine();
                responseTime = System.currentTimeMillis() - sentTime;
                if (response == null) continue;
                if (log.isDebugEnabled()) {
                    log.debug("poll: banner = " + response);
                    log.debug("poll: responseTime= " + responseTime + "ms");
                }
                if (response.indexOf(strBannerMatch) > -1) {
                    serviceStatus = SERVICE_AVAILABLE;
                    String cmd = "SSH-1.99-OpenNMS_1.1\r\n";
                    socket.getOutputStream().write(cmd.getBytes());
                    response = null;
                    try {
                        response = rdr.readLine();
                    } catch (IOException e) {
                    }
                    if (responseTime >= 0 && rrdPath != null) {
                        try {
                            this.updateRRD(rrdPath, ipv4Addr, dsName, responseTime, pkg);
                        } catch (RuntimeException rex) {
                            log.debug("There was a problem writing the RRD:" + rex);
                        }
                    }
                } else serviceStatus = SERVICE_UNAVAILABLE;
            } catch (NoRouteToHostException e) {
                e.fillInStackTrace();
                if (log.isEnabledFor(Priority.WARN)) log.warn("poll: No route to host exception for address " + ipv4Addr.getHostAddress(), e);
                break;
            } catch (InterruptedIOException e) {
                log.debug("SshMonitor: did not connect to host within timeout: " + timeout + " attempt: " + attempts);
            } catch (ConnectException e) {
                e.fillInStackTrace();
                if (log.isDebugEnabled()) log.debug("poll: Connection exception for address: " + ipv4Addr, e);
            } catch (IOException e) {
                e.fillInStackTrace();
                if (log.isDebugEnabled()) log.debug("poll: IOException while polling address: " + ipv4Addr, e);
            } finally {
                try {
                    if (socket != null) socket.close();
                } catch (IOException e) {
                    e.fillInStackTrace();
                    if (log.isDebugEnabled()) log.debug("poll: Error closing socket.", e);
                }
            }
        }
        return serviceStatus;
    }
}
