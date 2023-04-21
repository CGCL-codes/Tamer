package android.net.cts;

import android.net.DhcpInfo;
import android.test.AndroidTestCase;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;

@TestTargetClass(DhcpInfo.class)
public class DhcpInfoTest extends AndroidTestCase {

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Test DhcpInfo's constructor.", method = "DhcpInfo", args = {  })
    public void testConstructor() {
        new DhcpInfo();
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Test toString function.", method = "toString", args = {  })
    public void testToString() {
        String expectedDefault = "ipaddr 0.0.0.0 gateway 0.0.0.0 netmask 0.0.0.0 dns1 0.0.0.0 " + "dns2 0.0.0.0 DHCP server 0.0.0.0 lease 0 seconds";
        String STR_ADDR1 = "255.255.255.255";
        String STR_ADDR2 = "127.0.0.1";
        String STR_ADDR3 = "192.168.1.1";
        String STR_ADDR4 = "192.168.1.0";
        int leaseTime = 9999;
        String expected = "ipaddr " + STR_ADDR1 + " gateway " + STR_ADDR2 + " netmask " + STR_ADDR3 + " dns1 " + STR_ADDR4 + " dns2 " + STR_ADDR4 + " DHCP server " + STR_ADDR2 + " lease " + leaseTime + " seconds";
        DhcpInfo dhcpInfo = new DhcpInfo();
        assertEquals(expectedDefault, dhcpInfo.toString());
        dhcpInfo.ipAddress = ipToInteger(STR_ADDR1);
        dhcpInfo.gateway = ipToInteger(STR_ADDR2);
        dhcpInfo.netmask = ipToInteger(STR_ADDR3);
        dhcpInfo.dns1 = ipToInteger(STR_ADDR4);
        dhcpInfo.dns2 = ipToInteger(STR_ADDR4);
        dhcpInfo.serverAddress = ipToInteger(STR_ADDR2);
        dhcpInfo.leaseDuration = leaseTime;
        assertEquals(expected, dhcpInfo.toString());
    }

    private int ipToInteger(String ipString) {
        String ipSegs[] = ipString.split("[.]");
        int tmp = Integer.parseInt(ipSegs[3]) << 24 | Integer.parseInt(ipSegs[2]) << 16 | Integer.parseInt(ipSegs[1]) << 8 | Integer.parseInt(ipSegs[0]);
        return tmp;
    }
}
