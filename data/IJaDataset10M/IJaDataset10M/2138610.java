package it.unibo.deis.collaudo.p2p.pgrid.distributed;

import it.unibo.deis.interaction.ConnectionFactory;
import it.unibo.deis.interaction.IServiceConnection;
import it.unibo.deis.interaction.messages.IMessage;
import it.unibo.deis.interaction.p2p.Constants;
import it.unibo.deis.interaction.p2p.pgrid.ServiceConnectionPGrid;
import java.util.Random;
import java.util.Vector;
import junit.framework.TestCase;

/**
 * 
 * @author Miguel
 *
 */
public class TestServiceConnectionOnPeer2 extends TestCase {

    protected Vector<IServiceConnection> farm;

    protected IServiceConnection connection;

    protected IMessage messaggio;

    protected String testMessage;

    protected String testPayload;

    protected Random rnd;

    /**
	 * @param arg0
	 */
    public TestServiceConnectionOnPeer2(String arg0) {
        super(arg0);
    }

    /**
	 * @throws java.lang.Exception
	 */
    protected void setUp() throws Exception {
        super.setUp();
        connection = new ServiceConnectionPGrid();
        Thread.sleep(10000);
    }

    /**
	 * @throws java.lang.Exception
	 */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testMessage() throws Exception {
        int i = 1;
        connection.sendMsg("Peer2", "ProvaPayload");
        while (!connection.checkForMsg("Peer1", null)) {
            i++;
            if (Constants.isDebug()) System.out.println(i + "� giro");
            Thread.sleep(1000);
            if ((i % 60) == 0) {
                connection.sendMsg("Peer2", "ProvaPayload");
                System.out.println("Ancora nulla");
            }
        }
        messaggio = connection.receiveMsg("Peer1", null);
        System.out.println(messaggio);
    }
}
