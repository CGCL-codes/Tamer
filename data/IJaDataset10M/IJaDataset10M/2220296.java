package org.iosgi.util;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sven Schulz
 */
@Component(immediate = true)
@Provides(specifications = Network.class)
public class Networking implements Network {

    private static final Logger LOGGER = LoggerFactory.getLogger(Networking.class);

    public static InetAddress getPrimaryAddress() throws SocketException {
        List<NetworkInterface> candidates = Collections.list(NetworkInterface.getNetworkInterfaces());
        LOGGER.debug("Examining {} network interfaces", candidates.size());
        for (int i = 0; i < candidates.size(); i++) {
            NetworkInterface intf = candidates.get(i);
            String name = intf.getName();
            if (intf.isLoopback()) {
                LOGGER.debug("[{}/{}] {} is a loopback interface", new Object[] { i + 1, candidates.size(), name });
            } else if (!intf.isUp()) {
                LOGGER.debug("[{}/{}] {} is not up and running", new Object[] { i + 1, candidates.size(), name });
            } else if (intf.getDisplayName().toLowerCase().contains("virtualbox")) {
                LOGGER.debug("[{}/{}] {} is a VirtualBox interface", new Object[] { i + 1, candidates.size(), name });
            } else {
                List<InterfaceAddress> addresses = intf.getInterfaceAddresses();
                LOGGER.debug("Examining {} addresses for interface {}", addresses.size(), name);
                for (int j = 0; j < addresses.size(); j++) {
                    InetAddress addr = addresses.get(j).getAddress();
                    if (addr instanceof Inet6Address) {
                        LOGGER.debug("[{}/{}] {} is a IPv6 address", new Object[] { j + 1, addresses.size(), addr });
                    } else if (addr.isLinkLocalAddress()) {
                        LOGGER.debug("[{}/{}] {} is link local", new Object[] { j + 1, addresses.size(), addr });
                    } else if (addr.isLoopbackAddress()) {
                        LOGGER.debug("[{}/{}] {} is a loopback addrsss", new Object[] { j + 1, addresses.size(), addr });
                    } else {
                        LOGGER.debug("[{}/{}] {} selected", new Object[] { j + 1, addresses.size(), addr });
                        return addr;
                    }
                }
            }
        }
        LOGGER.error("No suitable interface / address found");
        return null;
    }

    private InetAddress primaryAddress;

    @Override
    public synchronized InetAddress getAddress() throws IOException {
        if (primaryAddress == null) {
            primaryAddress = getPrimaryAddress();
        }
        return primaryAddress;
    }
}
