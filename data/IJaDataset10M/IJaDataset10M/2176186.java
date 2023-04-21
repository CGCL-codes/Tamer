package org.opennms.netmgt.capsd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.util.Map;
import org.apache.log4j.Category;
import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;
import org.opennms.core.utils.ThreadCategory;
import org.opennms.netmgt.utils.ParameterMap;

/**
 * <P>
 * This class is designed to be used by the capabilities daemon to test for the existance of an
 * TCP server on remote interfaces. The class implements the Plugin interface that allows it to
 * be used along with other plugins by the daemon.
 * </P>
 * 
 * @author <A HREF="mailto:mike@opennms.org">Mike </A>
 * @author <A HREF="mailto:weave@oculan.com">Weaver </A>
 * @author <A HREF="http://www.opennsm.org">OpenNMS </A>
 * 
 *  
 */
public final class TcpPlugin extends AbstractPlugin {

    /**
     * The protocol supported by the plugin
     */
    private static final String PROTOCOL_NAME = "TCP";

    /**
     * Default number of retries for TCP requests
     */
    private static final int DEFAULT_RETRY = 0;

    /**
     * Default timeout (in milliseconds) for TCP requests
     */
    private static final int DEFAULT_TIMEOUT = 5000;

    /**
     * <P>
     * Test to see if the passed host-port pair is the endpoint for a TCP server. If there is a
     * TCP server at that destination then a value of true is returned from the method.
     * Otherwise a false value is returned to the caller. In order to return true the remote
     * host must generate a banner line which contains the text from the bannerMatch argument.
     * </P>
     * 
     * @param host
     *            The remote host to connect to.
     * @param port
     *            The remote port on the host.
     * @param bannerResult
     *            Banner line generated by the remote host must contain this text.
     * 
     * @return True if a connection is established with the host and the banner line contains
     *         the bannerMatch text.
     */
    private boolean isServer(InetAddress host, int port, int retries, int timeout, RE regex, StringBuffer bannerResult) {
        Category log = ThreadCategory.getInstance(getClass());
        boolean isAServer = false;
        for (int attempts = 0; attempts <= retries && !isAServer; attempts++) {
            Socket socket = null;
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(host, port), timeout);
                socket.setSoTimeout(timeout);
                log.debug("TcpPlugin: connected to host: " + host + " on port: " + port);
                if (regex == null) {
                    isAServer = true;
                } else {
                    BufferedReader lineRdr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String response = lineRdr.readLine();
                    if (regex.match(response)) {
                        if (log.isDebugEnabled()) log.debug("isServer: matching response=" + response);
                        isAServer = true;
                        if (bannerResult != null) bannerResult.append(response);
                    } else {
                        isAServer = false;
                        if (log.isDebugEnabled()) log.debug("isServer: NON-matching response=" + response);
                        break;
                    }
                }
            } catch (ConnectException e) {
                log.debug("TcpPlugin: Connection refused to " + host.getHostAddress() + ":" + port);
                isAServer = false;
            } catch (NoRouteToHostException e) {
                e.fillInStackTrace();
                log.info("TcpPlugin: Could not connect to host " + host.getHostAddress() + ", no route to host", e);
                isAServer = false;
                throw new UndeclaredThrowableException(e);
            } catch (InterruptedIOException e) {
                log.debug("TcpPlugin: did not connect to host within timeout: " + timeout + " attempt: " + attempts);
                isAServer = false;
            } catch (IOException e) {
                log.info("TcpPlugin: An expected I/O exception occured connecting to host " + host.getHostAddress() + " on port " + port, e);
                isAServer = false;
            } catch (Throwable t) {
                isAServer = false;
                log.warn("TcpPlugin: An undeclared throwable exception was caught connecting to host " + host.getHostAddress() + " on port " + port, t);
            } finally {
                try {
                    if (socket != null) socket.close();
                } catch (IOException e) {
                }
            }
        }
        return isAServer;
    }

    /**
     * Returns the name of the protocol that this plugin checks on the target system for
     * support.
     * 
     * @return The protocol name for this plugin.
     */
    public String getProtocolName() {
        return PROTOCOL_NAME;
    }

    /**
     * Returns true if the protocol defined by this plugin is supported. If the protocol is not
     * supported then a false value is returned to the caller.
     * 
     * @param address
     *            The address to check for support.
     * 
     * @return True if the protocol is supported by the address.
     * 
     * @throws java.lang.UnsupportedOperationException
     *             This is always thrown by this plugin.
     */
    public boolean isProtocolSupported(InetAddress address) {
        throw new UnsupportedOperationException("Undirected TCP checking not supported");
    }

    /**
     * Returns true if the protocol defined by this plugin is supported. If the protocol is not
     * supported then a false value is returned to the caller. The qualifier map passed to the
     * method is used by the plugin to return additional information by key-name. These
     * key-value pairs can be added to service events if needed.
     * 
     * @param address
     *            The address to check for support.
     * @param qualifiers
     *            The map where qualification are set by the plugin.
     * 
     * @return True if the protocol is supported by the address.
     */
    public boolean isProtocolSupported(InetAddress address, Map qualifiers) {
        int retries = DEFAULT_RETRY;
        int timeout = DEFAULT_TIMEOUT;
        int port = -1;
        String banner = null;
        String match = null;
        if (qualifiers != null) {
            retries = ParameterMap.getKeyedInteger(qualifiers, "retry", DEFAULT_RETRY);
            timeout = ParameterMap.getKeyedInteger(qualifiers, "timeout", DEFAULT_TIMEOUT);
            port = ParameterMap.getKeyedInteger(qualifiers, "port", -1);
            banner = ParameterMap.getKeyedString(qualifiers, "banner", null);
            match = ParameterMap.getKeyedString(qualifiers, "match", null);
        }
        if (port == -1) throw new IllegalArgumentException("The port must be specified when doing TCP discovery");
        try {
            StringBuffer bannerResult = null;
            RE regex = null;
            if (match == null && (banner == null || banner.equals("*"))) {
                regex = null;
            } else if (match != null) {
                regex = new RE(match);
                bannerResult = new StringBuffer();
            } else if (banner != null) {
                regex = new RE(banner);
                bannerResult = new StringBuffer();
            }
            boolean result = isServer(address, port, retries, timeout, regex, bannerResult);
            if (result && qualifiers != null) {
                if (bannerResult != null && bannerResult.length() > 0) qualifiers.put("banner", bannerResult.toString());
            }
            return result;
        } catch (RESyntaxException e) {
            throw new java.lang.reflect.UndeclaredThrowableException(e);
        }
    }
}