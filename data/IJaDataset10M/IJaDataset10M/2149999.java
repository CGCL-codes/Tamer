package org.opennms.netmgt.model.discovery;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import junit.framework.TestCase;

/**
 * IPAddressRangeTest
 *
 * @author brozow
 */
public class IPAddrRangeTest extends TestCase {

    private final IPAddress addr2 = new IPAddress("192.168.1.3");

    private final IPAddress addr3 = new IPAddress("192.168.1.5");

    private final IPAddrRange singleton;

    private final IPAddrRange small;

    public IPAddrRangeTest() throws UnknownHostException {
        small = new IPAddrRange(addr2.toString(), addr3.toString());
        singleton = new IPAddrRange(addr2.toString(), addr2.toString());
    }

    public void testIterator() {
        Iterator<InetAddress> it = small.iterator();
        assertTrue(it.hasNext());
        assertEquals(addr2.toInetAddress(), it.next());
        assertTrue(it.hasNext());
        assertEquals(addr2.incr().toInetAddress(), it.next());
        assertTrue(it.hasNext());
        assertEquals(addr3.toInetAddress(), it.next());
        assertFalse(it.hasNext());
    }

    public void testIterateSingleton() {
        Iterator<InetAddress> it = singleton.iterator();
        assertTrue(it.hasNext());
        assertEquals(addr2.toInetAddress(), it.next());
        assertFalse(it.hasNext());
    }
}
