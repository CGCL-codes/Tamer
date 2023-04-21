package org.apache.harmony.luni.net;

import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;

public class NetUtil {

    /**
     * Answers whether to use a SOCKS proxy.
     * 
     * @param proxy java.net.Proxy <code>proxy</code> is used to determine
     *        whether using SOCKS proxy.
     * @return true if only the type of <code>proxy</code> is
     *         Proxy.Type.SOCKS.
     */
    public static boolean usingSocks(Proxy proxy) {
        if (null != proxy && Proxy.Type.SOCKS == proxy.type()) {
            return true;
        }
        return false;
    }

    /**
     * Answer whether to prefer IPV6 address
     * 
     * @return boolean
     */
    public static boolean preferIPv6Addresses() {
        final Action a = new Action("java.net.preferIPv6Addresses");
        return AccessController.doPrivileged(a).booleanValue();
    }

    /**
     * Answer whether to prefer IPV4 stack
     * 
     * @return boolean
     */
    public static boolean preferIPv4Stack() {
        final Action a = new Action("java.net.preferIPv4Stack");
        return AccessController.doPrivileged(a).booleanValue();
    }

    /**
     * Gets proxy list according to the URI by system ProxySelector.
     * 
     * @param uri
     * @return a list of proxy for the URI. Returns null if no proxy is
     *         available.
     */
    public static List<Proxy> getProxyList(URI uri) {
        ProxySelector selector = ProxySelector.getDefault();
        if (null == selector) {
            return null;
        }
        return selector.select(uri);
    }

    private static final class Action implements PrivilegedAction<Boolean> {

        private final String propertyName;

        Action(String propertyName) {
            super();
            this.propertyName = propertyName;
        }

        public Boolean run() {
            if (Boolean.getBoolean(propertyName)) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }
    }

    static void intToBytes(int value, byte bytes[], int start) {
        bytes[start] = (byte) ((value >> 24) & 255);
        bytes[start + 1] = (byte) ((value >> 16) & 255);
        bytes[start + 2] = (byte) ((value >> 8) & 255);
        bytes[start + 3] = (byte) (value & 255);
    }

    static int bytesToInt(byte bytes[], int start) {
        int value = ((bytes[start + 3] & 255)) | ((bytes[start + 2] & 255) << 8) | ((bytes[start + 1] & 255) << 16) | ((bytes[start] & 255) << 24);
        return value;
    }
}
