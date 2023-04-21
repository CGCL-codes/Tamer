package org.drftpd.usermanager;

import junit.framework.TestCase;
import org.apache.oro.text.regex.MalformedPatternException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author zubov
 * @version $Id: HostMaskTest.java 1764 2007-08-04 02:01:21Z tdsoul $
 */
public class HostMaskTest extends TestCase {

    public static void main(String[] args) {
    }

    public void testMatchesHost() throws UnknownHostException, MalformedPatternException {
        HostMask h = new HostMask("*@1.1.1.1");
        assertTrue(h.matchesHost(InetAddress.getByName("1.1.1.1")));
        assertFalse(h.matchesHost(InetAddress.getByName("1.1.1.2")));
        h = new HostMask("1.*.*.*");
        assertTrue(h.matchesHost(InetAddress.getByName("1.2.3.4")));
        assertFalse(h.matchesHost(InetAddress.getByName("2.2.3.4")));
    }

    public void testMatchesIdent() throws MalformedPatternException {
        HostMask h = new HostMask("*@1.1.1.1");
        assertTrue(h.matchesIdent(null));
        assertTrue(h.matchesIdent("anything"));
        h = new HostMask("anything@1.1.1.1");
        assertFalse(h.matchesIdent(null));
        assertTrue(h.matchesIdent("anything"));
        assertFalse(h.matchesIdent("nothing"));
    }

    public void testEquals() {
        HostMask a = new HostMask("test@1.1.1.1");
        HostMask b = new HostMask("test@1.1.1.1");
        assertEquals(a, b);
        a = new HostMask("*@1.1.1.*");
        b = new HostMask("1.1.1.*");
        assertEquals(a, b);
        a = new HostMask("@1.1.1.*");
        assertEquals(a, b);
        a = new HostMask("notequal@4.2.3.4");
        assertFalse(a.equals(b));
    }
}
