package it.unibo.deis.interaction.bth;

import javax.microedition.io.StreamConnection;
import it.unibo.deis.infrastructure.IServerInfrastructure;
import it.unibo.deis.infrastructure.ITransportReceiver;
import it.unibo.deis.infrastructure.ServerInfrastructure;

/**
  *  
 */
public class BthTransportReceiver implements ITransportReceiver {

    private IBluetoothConnection con;

    private boolean goon = true;

    private boolean debug = true;

    /**
	 * This reads from a connection to receive answer messages only.
	 * @param con
	 * @throws Exception 
	 */
    public BthTransportReceiver(IBluetoothConnection con) {
        this.con = con;
        println(" *** BthTransportReceiver created with con= " + con);
    }

    public void terminate() {
        goon = false;
    }

    private int count = 0;

    public void run() {
        while (goon && (count++ < 4)) {
            try {
                println(" *** BthTransportReceiver ATTEMPTS to READ a request " + con);
                String message = con.readMsg();
                println(" *** BthTransportReceiver received \n" + message);
                if (message == null) break;
                new Thread(new Worker(con.getStreamConnection(), message)).start();
            } catch (Exception e) {
                println(" *** BthTransportReceiver E= " + e);
            }
        }
        System.out.println(" *** BthTransportReceiver ENDS ");
        System.exit(1);
    }

    protected void print(String msg) {
        if (debug) System.out.print(msg);
    }

    protected void println(String msg) {
        if (debug) System.out.println(msg);
    }

    private class Worker implements Runnable {

        String message = "";

        StreamConnection conn;

        public Worker(StreamConnection conn, String message) {
            this.conn = conn;
            this.message = message;
        }

        public void run() {
            IServerInfrastructure infraStructure = new ServerInfrastructure();
            infraStructure.handle(new BthTransportSender(conn), message);
            System.out.println("*** EDNS worker for " + message);
        }
    }
}
