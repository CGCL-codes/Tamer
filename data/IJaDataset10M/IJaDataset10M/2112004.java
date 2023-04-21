package it.unibo.deis.collaudo.p2p;

import it.unibo.deis.interaction.ConnectionFactory;
import it.unibo.deis.interaction.IServiceConnection;
import it.unibo.deis.interaction.messages.IMessage;
import it.unibo.deis.interaction.p2p.DiscoveryUtility;
import it.unibo.deis.interaction.p2p.MonitorService;
import it.unibo.deis.interaction.p2p.P2PFactory;
import it.unibo.deis.interaction.p2p.tucson.loader.TupleSpaceLoader;
import junit.framework.TestCase;

/**
 * @author Miguel
 *
 */
public class TestMonitorService extends TestCase {

    IServiceConnection conn;

    IMessage messaggio;

    MonitorService monitor = null;

    int contatore = -1;

    /**
	 * @param name
	 */
    public TestMonitorService(String name) {
        super(name);
        String[] temp = { "", "" };
        new TupleSpaceLoader(temp).run();
        try {
            conn = ConnectionFactory.getConn("p2p");
        } catch (Exception e) {
            e.printStackTrace();
        }
        monitor = P2PFactory.createMonitorService();
        monitor.setIntervalTime(1);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSimulaSuperPeer() {
        String tempString;
        int tempInt;
        contatore = 100;
        boolean test = false;
        try {
            for (int i = 0; i < 5; i++) {
                Thread.sleep(1000);
                if (conn.checkForMsg(DiscoveryUtility.getControlMonitorServiceMessage(), null)) {
                    messaggio = conn.receiveMsg(DiscoveryUtility.getControlMonitorServiceMessage(), null);
                    tempString = (String) messaggio.getPayload();
                    tempString = tempString.substring(1, tempString.length() - 1);
                    tempInt = Integer.parseInt(tempString);
                    assertTrue(tempInt == contatore);
                    assertFalse(conn.checkForMsg(DiscoveryUtility.getControlMonitorServiceMessage(), null));
                    tempInt++;
                    contatore++;
                    conn.sendMsg(DiscoveryUtility.getControlSuperPeerMessage() + tempInt, "");
                    assertTrue(tempInt == contatore);
                    test = true;
                    System.out.println("TMS: " + contatore);
                    contatore++;
                    Thread.yield();
                }
                assertTrue(test);
                test = false;
            }
            for (int i = 0; i < 5; i++) {
                Thread.sleep(1000);
                conn.sendMsg("", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
