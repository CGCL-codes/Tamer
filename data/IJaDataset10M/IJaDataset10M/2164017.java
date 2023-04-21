package it.unibo.deis.collaudo.p2p;

import java.net.InetSocketAddress;
import it.unibo.deis.interaction.ConnectionFactory;
import it.unibo.deis.interaction.p2p.P2PFactory;
import it.unibo.deis.interaction.p2p.P2pDiscoveryService;
import it.unibo.deis.interaction.p2p.tucson.loader.TupleSpaceLoader;
import junit.framework.TestCase;

public class TestGlobal extends TestCase {

    TupleSpaceLoader tsl;

    P2pDiscoveryService discovery;

    public TestGlobal() {
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGlobal() {
        String[] temp = { "", "" };
        tsl = new TupleSpaceLoader(temp);
        Thread t1 = new Thread(tsl);
        t1.start();
        P2PFactory.createMonitorService();
        P2PFactory.createSuperPeer(true);
        Thread.yield();
        discovery = P2pDiscoveryService.getInstance();
        discovery.offerForService("provaServizioGlobale", "10.0.0.0", "10");
        Thread.yield();
        int j = 0;
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Thread.yield();
            j++;
            System.out.println("gira gira .." + j);
        }
        InetSocketAddress address = discovery.askForService("provaServizioGlobale");
        System.out.println(address);
    }
}
