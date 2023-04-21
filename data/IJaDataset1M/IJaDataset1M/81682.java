package sf.dhcp4java.test;

import java.net.InetAddress;
import sf.dhcp4java.DHCPBadPacketException;
import sf.dhcp4java.DHCPPacket;
import junit.framework.TestCase;

/**
 * @author yshi7355
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DHCPPacketSanityTest extends TestCase {

    private InetAddress ip0;

    protected void setUp() throws Exception {
        super.setUp();
        try {
            ip0 = InetAddress.getByName("0.0.0.0");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final void testMarshallEmpty() {
        byte[] pacnull;
        DHCPPacket pac;
        try {
            pacnull = new byte[0];
            pac = DHCPPacket.getPacket(pacnull, 0, pacnull.length);
            pacnull = new byte[100];
            pac = null;
        } catch (DHCPBadPacketException e) {
        }
    }
}
