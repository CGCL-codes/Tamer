package test.tuwien.auto.calimero.knxnetip;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import junit.framework.TestCase;
import test.tuwien.auto.calimero.Util;
import tuwien.auto.calimero.CloseEvent;
import tuwien.auto.calimero.FrameEvent;
import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.IndividualAddress;
import tuwien.auto.calimero.Priority;
import tuwien.auto.calimero.cemi.CEMI;
import tuwien.auto.calimero.cemi.CEMILData;
import tuwien.auto.calimero.exception.KNXException;
import tuwien.auto.calimero.exception.KNXIllegalArgumentException;
import tuwien.auto.calimero.knxnetip.KNXConnectionClosedException;
import tuwien.auto.calimero.knxnetip.KNXnetIPConnection;
import tuwien.auto.calimero.knxnetip.KNXnetIPRouter;
import tuwien.auto.calimero.knxnetip.LostMessageEvent;
import tuwien.auto.calimero.knxnetip.RouterListener;
import tuwien.auto.calimero.log.LogManager;

/**
 * @author B. Malinowsky
 */
public class KNXnetIPRouterTest extends TestCase {

    private static KNXnetIPConnection.BlockingMode noblock = KNXnetIPConnection.NONBLOCKING;

    private KNXnetIPRouter r;

    private RouterListenerImpl l;

    private CEMILData frame;

    private CEMILData frame2;

    private CEMILData frameNoDest;

    private final class RouterListenerImpl implements RouterListener {

        volatile boolean closed;

        volatile CEMI received;

        List lost = new Vector();

        public void frameReceived(FrameEvent e) {
            assertNotNull(e);
            assertEquals(r, e.getSource());
            received = e.getFrame();
            Debug.printLData((CEMILData) received);
        }

        public void connectionClosed(CloseEvent e) {
            assertNotNull(e);
            assertEquals(r, e.getSource());
            if (closed) fail("already closed");
            closed = true;
        }

        public void lostMessage(LostMessageEvent e) {
            assertNotNull(e);
            lost.add(e);
        }
    }

    /**
	 * @param name name of test case
	 */
    public KNXnetIPRouterTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        l = new RouterListenerImpl();
        LogManager.getManager().addWriter(null, Util.getLogWriter());
        frame = new CEMILData(CEMILData.MC_LDATA_IND, new IndividualAddress(0), new GroupAddress(0, 0, 1), new byte[] { 0, (byte) (0x80 | 1) }, Priority.NORMAL);
        frame2 = new CEMILData(CEMILData.MC_LDATA_IND, new IndividualAddress(0), new GroupAddress(0, 0, 1), new byte[] { 0, (byte) (0x80 | 0) }, Priority.URGENT);
        frameNoDest = new CEMILData(CEMILData.MC_LDATA_IND, new IndividualAddress(0), new GroupAddress(10, 7, 10), new byte[] { 0, (byte) (0x80 | 0) }, Priority.URGENT);
    }

    protected void tearDown() throws Exception {
        if (r != null) {
            r.close();
        }
        LogManager.getManager().removeWriter(null, Util.getLogWriter());
        super.tearDown();
    }

    /**
	 * Test method for
	 * {@link tuwien.auto.calimero.knxnetip.KNXnetIPRouter#send(tuwien.auto.calimero.cemi.CEMI, tuwien.auto.calimero.knxnetip.KNXnetIPConnection.BlockingMode)}.
	 * 
	 * @throws KNXException
	 */
    public final void testSend() throws KNXException {
        newRouter();
        doSend(frame, noblock);
        doSend(frame2, noblock);
        doSend(frameNoDest, noblock);
    }

    /**
	 * Test method for {@link tuwien.auto.calimero.knxnetip.KNXnetIPRouter#send
	 * (tuwien.auto.calimero.cemi.CEMI,
	 * tuwien.auto.calimero.knxnetip.KNXnetIPConnection.BlockingMode)}.
	 * 
	 * @throws KNXException
	 * @throws UnknownHostException
	 * @throws SocketException
	 */
    public final void testSend2() throws KNXException, SocketException, UnknownHostException {
        r = new KNXnetIPRouter(NetworkInterface.getByInetAddress(Util.getLocalHost().getAddress()), InetAddress.getByName(KNXnetIPRouter.DEFAULT_MULTICAST));
        r.addConnectionListener(l);
        doSend(frame, noblock);
        doSend(frame2, noblock);
        doSend(frameNoDest, noblock);
    }

    private void doSend(CEMILData f, KNXnetIPConnection.BlockingMode m) throws KNXConnectionClosedException {
        r.send(f, m);
        try {
            Thread.sleep(500);
        } catch (final InterruptedException e) {
        }
    }

    /**
	 * Test method for {@link tuwien.auto.calimero.knxnetip.KNXnetIPRouter#KNXnetIPRouter
	 * (java.net.NetworkInterface, java.net.InetAddress)}.
	 * 
	 * @throws KNXException
	 * @throws UnknownHostException
	 * @throws SocketException
	 */
    public final void testKNXnetIPRouter() throws SocketException, UnknownHostException, KNXException {
        newRouter();
        assertEquals(KNXnetIPConnection.OK, r.getState());
        r.close();
        r = new KNXnetIPRouter(NetworkInterface.getByInetAddress(Util.getLocalHost().getAddress()), InetAddress.getByName(KNXnetIPRouter.DEFAULT_MULTICAST));
        r.close();
        try {
            r = new KNXnetIPRouter(null, InetAddress.getByName("224.0.23.11"));
            fail("invalid routing multicast");
        } catch (final KNXIllegalArgumentException e) {
        }
        r = new KNXnetIPRouter(null, InetAddress.getByName("224.0.23.13"));
    }

    /**
	 * @throws KNXException
	 */
    public final void testReceive() throws KNXException {
        newRouter();
        System.out.println("waiting for some incoming frames...");
        try {
            Thread.sleep(30000);
        } catch (final InterruptedException e) {
        }
    }

    /**
	 * Test method for
	 * {@link tuwien.auto.calimero.knxnetip.KNXnetIPRouter#setHopCount(int)}.
	 * 
	 * @throws KNXException
	 */
    public final void testSetHopCount() throws KNXException {
        newRouter();
        final int hobbes = r.getHopCount();
        r.setHopCount(hobbes + 1);
        assertEquals(hobbes + 1, r.getHopCount());
        try {
            r.setHopCount(-1);
            fail("negative hopcount");
        } catch (final KNXIllegalArgumentException e) {
        }
        assertEquals(hobbes + 1, r.getHopCount());
        try {
            r.setHopCount(256);
            fail("hopcount too big");
        } catch (final KNXIllegalArgumentException e) {
        }
        assertEquals(hobbes + 1, r.getHopCount());
        r.setHopCount(255);
        assertEquals(255, r.getHopCount());
        r.setHopCount(hobbes);
        assertEquals(hobbes, r.getHopCount());
        r.close();
        r.setHopCount(20);
    }

    /**
	 * Test method for {@link tuwien.auto.calimero.knxnetip.KNXnetIPRouter#close()}.
	 * 
	 * @throws KNXException
	 */
    public final void testClose() throws KNXException {
        newRouter();
        r.close();
        assertEquals(KNXnetIPConnection.CLOSED, r.getState());
        try {
            r.send(frame, noblock);
            fail("we are closed");
        } catch (final KNXConnectionClosedException e) {
        }
        assertEquals(KNXnetIPConnection.CLOSED, r.getState());
    }

    /**
	 * Test method for
	 * {@link tuwien.auto.calimero.knxnetip.KNXnetIPRouter#getRemoteAddress()}.
	 * 
	 * @throws KNXException
	 * @throws UnknownHostException
	 * @throws SocketException
	 */
    public final void testGetRemoteAddress() throws KNXException, SocketException, UnknownHostException {
        newRouter();
        assertEquals(new InetSocketAddress(KNXnetIPRouter.DEFAULT_MULTICAST, KNXnetIPConnection.IP_PORT), r.getRemoteAddress());
        r.close();
        assertTrue(r.getRemoteAddress().getAddress().isAnyLocalAddress());
        assertTrue(r.getRemoteAddress().getPort() == 0);
        r = new KNXnetIPRouter(NetworkInterface.getByInetAddress(Util.getLocalHost().getAddress()), InetAddress.getByName("224.0.23.33"));
        assertEquals(new InetSocketAddress("224.0.23.33", KNXnetIPConnection.IP_PORT), r.getRemoteAddress());
        r.close();
        assertTrue(r.getRemoteAddress().getAddress().isAnyLocalAddress());
        assertTrue(r.getRemoteAddress().getPort() == 0);
    }

    public void testLostMessageIndication() throws KNXException {
        newRouter();
        int sent = 0;
        while (sent < 1000 && l.lost.isEmpty()) {
            r.send(frame, noblock);
            ++sent;
            try {
                Thread.sleep(10);
            } catch (final InterruptedException e) {
            }
        }
        try {
            Thread.sleep(100);
        } catch (final InterruptedException e1) {
        }
        for (final Iterator i = l.lost.iterator(); i.hasNext(); ) {
            final LostMessageEvent e = (LostMessageEvent) i.next();
            System.out.println("dev.state:" + e.getDeviceState() + ", lost msgs:" + e.getLostMessages());
        }
    }

    private void newRouter() throws KNXException {
        r = new KNXnetIPRouter(null, null);
        r.addConnectionListener(l);
    }
}
