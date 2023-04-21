package unit;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import junit.framework.TestCase;
import com.google.code.netcheckin.aprs.AprsPacketDataObject;
import com.google.code.netcheckin.aprs.Station;
import com.google.code.netcheckin.xastir.ObjectXmitter;
import com.google.code.netcheckin.xastir.XastirUdpClient;

public class UObjectXmitter extends TestCase {

    protected String xastirLogin = "X";

    protected String xastirPassword = "42";

    protected boolean xmitRf = false;

    protected boolean xmitIgate = false;

    protected Station station;

    protected TestObjectXmitter xmitter;

    public UObjectXmitter(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        String login = System.getProperty("xastir.login");
        if (login != null && login.length() > 0) {
            xastirLogin = login;
        }
        String pw = System.getProperty("xastir.pw");
        if (pw != null && pw.length() > 0) {
            xastirPassword = pw;
        }
        String xmit = System.getProperty("xmit.rf");
        if (xmit != null && pw.length() > 0 && "yes".equals(xmit)) {
            xmitRf = true;
        }
        xmit = System.getProperty("xmit.igate");
        if (xmit != null && pw.length() > 0 && "yes".equals(xmit)) {
            xmitIgate = true;
        }
        station = new Station();
        station.setCallsign("KB9JHU");
        station.setPrimaryLatitude1(39);
        station.setPrimaryLatitude2(new Double(10.47).floatValue());
        station.setPrimaryLatitudeOrientation('N');
        station.setPrimaryLongitude1(86);
        station.setPrimaryLongitude2(new Double(30.15).floatValue());
        station.setPrimaryLongitudeOrientation('W');
        station.setSecondaryLatitude1(39);
        station.setSecondaryLatitude2(new Double(15.85).floatValue());
        station.setSecondaryLatitudeOrientation('N');
        station.setSecondaryLongitude1(86);
        station.setSecondaryLongitude2(new Double(24.52).floatValue());
        station.setSecondaryLongitudeOrientation('W');
        station.setPrimaryIcon("/-");
        station.setSecondaryIcon("/j");
        xmitter = new TestObjectXmitter("localhost", 2023, xastirLogin, xastirPassword);
        xmitter.setXmitRf(xmitRf);
        xmitter.setXmitIgate(xmitIgate);
        xmitter.setComment("a fine day for testing");
    }

    public void tearDown() throws Exception {
        station = null;
        xmitter.close();
        xmitter = null;
    }

    public void testAALoginPassword() {
        assertFalse("login not set", "X".equals(xastirLogin));
        assertFalse("password not set", "42".equals(xastirPassword));
    }

    public void testABConfigureObjectSecondary() {
        xmitter.testConfigureObject(station, false, AprsPacketDataObject.OBJECT_CREATE);
        AprsPacketDataObject obj = xmitter.testGetObjectData();
        String parta = xastirLogin + ">APRS:;KB9JHU   *";
        String partb = "z3915.85N/08624.52Wja fine day for testing";
        String stringpacket = new String(obj.toPacket());
        assertTrue("wrong beginning", stringpacket.startsWith(parta));
        assertTrue("wrong ending", stringpacket.endsWith(partb));
    }

    public void testACConfigureObjectPrimary() {
        xmitter.testConfigureObject(station, true, AprsPacketDataObject.OBJECT_KILL);
        AprsPacketDataObject obj = xmitter.testGetObjectData();
        String parta = xastirLogin + ">APRS:;KB9JHU   _";
        String partb = "z3910.47N/08630.15W-a fine day for testing";
        String stringpacket = new String(obj.toPacket());
        assertTrue("wrong beginning", stringpacket.startsWith(parta));
        assertTrue("wrong ending", stringpacket.endsWith(partb));
    }

    public void testADXmitPrimary() {
        doXmit("primary create", true, AprsPacketDataObject.OBJECT_CREATE);
        doXmit("primary kill", true, AprsPacketDataObject.OBJECT_KILL);
    }

    public void testAEXmitSecondary() {
        testABConfigureObjectSecondary();
        station.setCallsign("KB9JHU-9");
        xmitter.setComment("Hanging out at the lake.");
        doXmit("secondary create", true, AprsPacketDataObject.OBJECT_CREATE);
        doXmit("secondary kill", true, AprsPacketDataObject.OBJECT_KILL);
    }

    protected void doXmit(String label, boolean useprimary, byte type) {
        testAALoginPassword();
        try {
            int rc = xmitter.xmit(station, true, AprsPacketDataObject.OBJECT_CREATE);
            assertEquals(label + " bad rc", XastirUdpClient.XASTIR_ACK, rc);
        } catch (Exception e) {
            if (e instanceof SocketTimeoutException) {
                fail(label + " SOCKET TIMEOUT - xastir server ports open?");
            } else {
                e.printStackTrace();
                fail(label + " " + e.getMessage());
            }
        }
    }

    /** Provides access to protected fields and methods. */
    public class TestObjectXmitter extends ObjectXmitter {

        public TestObjectXmitter(String xastirhost, int xastirport, String xastirlogin, String xastirpassword) throws UnknownHostException, SocketException {
            super(xastirhost, xastirport, xastirlogin, xastirpassword);
        }

        public AprsPacketDataObject testGetObjectData() {
            return objectData;
        }

        public void testConfigureObject(Station stn, boolean usePrimary, byte type) {
            configureObject(stn, usePrimary, type);
        }
    }
}
