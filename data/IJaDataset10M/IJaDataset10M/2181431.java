package org.apache.harmony.luni.tests.java.net;

import java.io.Serializable;
import java.net.Inet4Address;
import java.net.InetAddress;
import org.apache.harmony.testframework.serialization.SerializationTest;
import org.apache.harmony.testframework.serialization.SerializationTest.SerializableAssert;

public class Inet4AddressTest extends junit.framework.TestCase {

    /**
	 * @tests java.net.Inet4Address#isMulticastAddress()
	 */
    public void test_isMulticastAddress() throws Exception {
        String addrName = "";
        addrName = "224.0.0.0";
        InetAddress addr = InetAddress.getByName(addrName);
        assertTrue("Multicast address " + addrName + " not detected.", addr.isMulticastAddress());
        addrName = "239.255.255.255";
        addr = InetAddress.getByName(addrName);
        assertTrue("Multicast address " + addrName + " not detected.", addr.isMulticastAddress());
        addrName = "42.42.42.42";
        addr = InetAddress.getByName(addrName);
        assertTrue("Non multicast address " + addrName + " reporting as a multicast address.", !addr.isMulticastAddress());
    }

    /**
	 * @tests java.net.Inet4Address#isAnyLocalAddress()
	 */
    public void test_isAnyLocalAddress() throws Exception {
        String addrName = "";
        addrName = "0.0.0.0";
        InetAddress addr = InetAddress.getByName(addrName);
        assertTrue("ANY address " + addrName + " not detected.", addr.isAnyLocalAddress());
    }

    /**
	 * @tests java.net.Inet4Address#isLoopbackAddress()
	 */
    public void test_isLoopbackAddress() throws Exception {
        String addrName = "";
        addrName = "127.0.0.0";
        InetAddress addr = InetAddress.getByName(addrName);
        assertTrue("Loopback address " + addrName + " not detected.", addr.isLoopbackAddress());
        addrName = "127.42.42.42";
        addr = InetAddress.getByName(addrName);
        assertTrue("Loopback address " + addrName + " not detected.", addr.isLoopbackAddress());
        addrName = "42.42.42.42";
        addr = InetAddress.getByName(addrName);
        assertTrue("Address incorrectly " + addrName + " detected as a loopback address.", !addr.isLoopbackAddress());
    }

    /**
	 * @tests java.net.Inet4Address#isLinkLocalAddress()
	 */
    public void test_isLinkLocalAddress() throws Exception {
        String addrName = "";
        addrName = "42.42.42.42";
        InetAddress addr = InetAddress.getByName(addrName);
        assertTrue("IPv4 address " + addrName + " incorrectly reporting as a link local address.", !addr.isLinkLocalAddress());
    }

    /**
	 * @tests java.net.Inet4Address#isSiteLocalAddress()
	 */
    public void test_isSiteLocalAddress() throws Exception {
        String addrName = "";
        addrName = "42.42.42.42";
        InetAddress addr = InetAddress.getByName(addrName);
        assertTrue("IPv4 address " + addrName + " incorrectly reporting as a site local address.", !addr.isSiteLocalAddress());
    }

    /**
	 * @tests java.net.Inet4Address#isMCGlobal()
	 */
    public void test_isMCGlobal() throws Exception {
        String addrName = "";
        addrName = "224.0.0.0";
        InetAddress addr = InetAddress.getByName(addrName);
        assertTrue("IPv4 link-local multicast address " + addrName + " incorrectly identified as a global multicast address.", !addr.isMCGlobal());
        addrName = "224.0.0.255";
        addr = InetAddress.getByName(addrName);
        assertTrue("IPv4 link-local multicast address " + addrName + " incorrectly identified as a global multicast address.", !addr.isMCGlobal());
        addrName = "224.0.1.0";
        addr = InetAddress.getByName(addrName);
        assertTrue("IPv4 global multicast address " + addrName + " not identified as a global multicast address.", addr.isMCGlobal());
        addrName = "238.255.255.255";
        addr = InetAddress.getByName(addrName);
        assertTrue("IPv4 global multicast address " + addrName + " not identified as a global multicast address.", addr.isMCGlobal());
        addrName = "239.0.0.0";
        addr = InetAddress.getByName(addrName);
        assertTrue("IPv4 reserved multicast address " + addrName + " incorrectly identified as a global multicast address.", !addr.isMCGlobal());
        addrName = "239.191.255.255";
        addr = InetAddress.getByName(addrName);
        assertTrue("IPv4 reserved multicast address " + addrName + " incorrectly identified as a global multicast address.", !addr.isMCGlobal());
    }

    /**
	 * @tests java.net.Inet4Address#isMCNodeLocal()
	 */
    public void test_isMCNodeLocal() throws Exception {
        String addrName = "";
        addrName = "224.42.42.42";
        InetAddress addr = InetAddress.getByName(addrName);
        assertTrue("IPv4 multicast address " + addrName + " incorrectly identified as a node-local multicast address.", !addr.isMCNodeLocal());
        addrName = "239.0.0.0";
        addr = InetAddress.getByName(addrName);
        assertTrue("IPv4 reserved multicast address " + addrName + " incorrectly identified as a node-local multicast address.", !addr.isMCNodeLocal());
    }

    /**
	 * @tests java.net.Inet4Address#isMCLinkLocal()
	 */
    public void test_isMCLinkLocal() throws Exception {
        String addrName = "";
        addrName = "224.0.0.0";
        InetAddress addr = InetAddress.getByName(addrName);
        assertTrue("IPv4 link-local multicast address " + addrName + " not identified as a link-local multicast address.", addr.isMCLinkLocal());
        addrName = "224.0.0.255";
        addr = InetAddress.getByName(addrName);
        assertTrue("IPv4 link-local multicast address " + addrName + " not identified as a link-local multicast address.", addr.isMCLinkLocal());
        addrName = "224.0.1.0";
        addr = InetAddress.getByName(addrName);
        assertTrue("IPv4 global multicast address " + addrName + " incorrectly identified as a link-local multicast address.", !addr.isMCLinkLocal());
        addrName = "239.0.0.0";
        addr = InetAddress.getByName(addrName);
        assertTrue("IPv4 reserved multicast address " + addrName + " incorrectly identified as a link-local multicast address.", !addr.isMCLinkLocal());
    }

    /**
	 * @tests java.net.Inet4Address#isMCSiteLocal()
	 */
    public void test_isMCSiteLocal() throws Exception {
        String addrName = "";
        addrName = "240.0.0.0";
        InetAddress addr = InetAddress.getByName(addrName);
        assertTrue("IPv4 multicast address " + addrName + " incorrectly identified as a site-local multicast address.", !addr.isMCSiteLocal());
        addrName = "239.0.0.0";
        addr = InetAddress.getByName(addrName);
        assertTrue("IPv4 reserved multicast address " + addrName + " incorrectly identified as a site-local multicast address.", !addr.isMCSiteLocal());
        addrName = "239.255.0.0";
        addr = InetAddress.getByName(addrName);
        assertTrue("IPv4 site-local multicast address " + addrName + " not identified as a site-local multicast address.", addr.isMCSiteLocal());
        addrName = "239.255.255.255";
        addr = InetAddress.getByName(addrName);
        assertTrue("IPv4 site-local multicast address " + addrName + " not identified as a site-local multicast address.", addr.isMCSiteLocal());
        addrName = "239.255.2.2";
        addr = InetAddress.getByName(addrName);
        assertTrue("IPv4 site-local multicast address " + addrName + " not identified as a site-local multicast address.", addr.isMCSiteLocal());
    }

    /**
	 * @tests java.net.Inet4Address#isMCOrgLocal()
	 */
    public void test_isMCOrgLocal() throws Exception {
        String addrName = "";
        addrName = "239.191.255.255";
        InetAddress addr = InetAddress.getByName(addrName);
        assertTrue("IPv4 reserved multicast address " + addrName + " incorrectly identified as a org-local multicast address.", !addr.isMCOrgLocal());
        addrName = "239.252.0.0";
        addr = InetAddress.getByName(addrName);
        assertTrue("IPv4 site-local multicast address " + addrName + " incorrectly identified as a org-local multicast address.", !addr.isMCOrgLocal());
        addrName = "239.192.0.0";
        addr = InetAddress.getByName(addrName);
        assertTrue("IPv4 org-local multicast address " + addrName + " not identified as a org-local multicast address.", addr.isMCOrgLocal());
        addrName = "239.195.255.255";
        addr = InetAddress.getByName(addrName);
        assertTrue("IPv4 org-local multicast address " + addrName + " not identified as a org-local multicast address.", addr.isMCOrgLocal());
    }

    private static final SerializableAssert COMPARATOR = new SerializableAssert() {

        public void assertDeserialized(Serializable initial, Serializable deserialized) {
            Inet4Address initAddr = (Inet4Address) initial;
            Inet4Address desrAddr = (Inet4Address) deserialized;
            byte[] iaAddresss = initAddr.getAddress();
            byte[] deIAAddresss = desrAddr.getAddress();
            for (int i = 0; i < iaAddresss.length; i++) {
                assertEquals(iaAddresss[i], deIAAddresss[i]);
            }
            assertEquals(4, deIAAddresss.length);
            assertEquals(initAddr.getHostName(), desrAddr.getHostName());
        }
    };

    /**
     * @tests serialization/deserialization compatibility.
     */
    public void testSerializationSelf() throws Exception {
        SerializationTest.verifySelf(Inet4Address.getByName("localhost"), COMPARATOR);
    }

    /**
     * @tests serialization/deserialization compatibility with RI.
     */
    public void testSerializationCompatibility() throws Exception {
        SerializationTest.verifyGolden(this, Inet4Address.getByName("localhost"), COMPARATOR);
    }
}
