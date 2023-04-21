package org.ms150hams.trackem.network;

import java.util.Hashtable;
import org.ms150hams.trackem.model.Event;
import org.ms150hams.trackem.network.opentrac.OpenTracProtocolHandler;

public class TrackemTCPIPServer implements EventHandler {

    TCPIPServer server;

    OpenTracProtocolHandler ot;

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        TrackemTCPIPServer ts;
        ts = new TrackemTCPIPServer();
        ts.setup();
    }

    public void setup() {
        System.out.println("setup");
        server = new TCPIPServer();
        ot = new OpenTracProtocolHandler();
        server.setUpperLayer(ot);
        ot.setEventHandler(this);
        ot.setLowerLayer(server);
        server.startReceive();
    }

    public void receiveEvent(Event evt, Hashtable ht) {
        System.out.println("Received event " + evt);
    }
}
