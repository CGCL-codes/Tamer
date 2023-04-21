package org.yajul.net;

import java.util.ArrayList;
import java.util.Iterator;
import java.net.Socket;
import java.net.InetAddress;

/**
 * Filters IP addresses using a list of acceptable address patterns and
 * a list of address patterns to be denied.
 * User: josh
 * Date: Dec 12, 2003
 * Time: 5:57:35 AM
 * @see IPAddressPattern
 */
public class IPAddressFilter {

    /** Lists of allowed and prohibited IP addresses. */
    private ArrayList accept, deny;

    public IPAddressFilter() {
        accept = new ArrayList();
        deny = new ArrayList();
    }

    /**
     * Add an IP address to the list of allowed or denied addresses. The
     * address parameter can contain '*' as wildcard character,
     * e.g. "192.168.*.*".
     * @param address The IP address pattern.
     * @param accept True to add an allowed addres, false to add a denied
     * address.
     * @see IPAddressPattern
     */
    public void add(String address, boolean accept) {
        add(new IPAddressPattern(address), accept);
    }

    /**
     * Add an IP address to the list of allowed or denied addresses.
     * @param pattern The IP address pattern.
     * @param accept True to add an allowed addres, false to add a denied
     * address.
     * @see IPAddressPattern
     */
    public void add(IPAddressPattern pattern, boolean accept) {
        ArrayList list = (accept) ? this.accept : this.deny;
        list.add(pattern);
    }

    /**
     * Returns true if the socket connection is allowed.
     * @param s The socket.
     * @return true if the socket is to be allowed, false if not.
     */
    public boolean checkSocket(Socket s) {
        return checkAddress(s.getInetAddress());
    }

    /**
     * Returns true if the address is allowed.
     * @param inetAddress The IP address.
     * @return true if the address is allowed, false if not.
     */
    public boolean checkAddress(InetAddress inetAddress) {
        return checkAddress(inetAddress.getAddress());
    }

    /**
     * Returns true if the address is allowed.
     * @param address The IP address.
     * @return true if the address is allowed, false if not.
     */
    public boolean checkAddress(byte[] address) {
        if (deny.size() == 0 && accept.size() == 0) return true; else {
            IPAddressPattern pattern = null;
            for (Iterator iterator = deny.iterator(); iterator.hasNext(); ) {
                pattern = (IPAddressPattern) iterator.next();
                if (pattern.matches(address)) return false;
            }
            for (Iterator iterator = accept.iterator(); iterator.hasNext(); ) {
                pattern = (IPAddressPattern) iterator.next();
                if (pattern.matches(address)) return true;
            }
            return false;
        }
    }
}
