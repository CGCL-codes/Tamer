package com.google.code.spotshout.comm;

import com.google.code.spotshout.RMIProperties;
import com.google.code.spotshout.remote.RemoteGarbageCollector;
import com.sun.spot.io.j2me.radiogram.Radiogram;
import com.sun.spot.io.j2me.radiogram.RadiogramConnection;
import com.sun.spot.peripheral.radio.RadioFactory;
import com.sun.spot.util.IEEEAddress;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.Datagram;

/**
 * This class represents the RMI Server. It uses datagram (non reliable) data
 * to receive connection request from a client to establish a reliable connection.
 *
 * This class abstract the structure of a server. It listen to connection on a
 * given {@link Thread} and dispatch the method. The class interested on serving
 * will inherit this class and implement the dispatch method of the request.
 *
 * Protocol for HandShake:
 *
 * Client HandShake Request
 * ----------------------------------------------------------------------------
 * Byte:        Opcode
 *
 *
 * Server HandShake Reply
 * ----------------------------------------------------------------------------
 * (Opt)String: Server Address
 * INT:         Connection Reliable Port
 */
public abstract class Server implements Runnable {

    /**
     * The ourPort which this Server will listen (non reliable), to establish
     * reliable connections.
     */
    protected int ourPort;

    /**
     * Our server address.
     */
    protected String ourAddr;

    public Server(int listeningPort) {
        ourPort = listeningPort;
        ourAddr = IEEEAddress.toDottedHex(RadioFactory.getRadioPolicyManager().getIEEEAddress());
        RMIProperties.log("Started this RMI Server -- I'm: " + ourAddr + ":" + ourPort);
    }

    public abstract RMIReply service(RMIRequest request);

    /**
     * Send the address of this server to a client
     * @param clientAddr - the client addr
     * @throws IOException - if there is a error on opening this connection
     */
    private void discoverRequest(String clientAddr) throws IOException, InterruptedException {
        String tempuri = "radiogram://" + clientAddr + ":" + RMIProperties.UNRELIABLE_DISCOVER_CLIENT_PORT;
        RadiogramConnection tmp = (RadiogramConnection) Connector.open(tempuri);
        Datagram tmpDg = tmp.newDatagram(20);
        Thread.sleep(RMIProperties.LITTLE_SLEEP_TIME);
        tmpDg.writeByte(ProtocolOpcode.HOST_ADDR_REPLY);
        tmp.send(tmpDg);
        tmp.close();
    }

    private void invokeRequest(String clientAddr, int connectionPort) throws IOException, InterruptedException {
        String tempuri = "radiogram://" + clientAddr + ":" + RMIProperties.UNRELIABLE_INVOKE_CLIENT_PORT;
        RadiogramConnection tmp = (RadiogramConnection) Connector.open(tempuri);
        Datagram tmpDg = tmp.newDatagram(20);
        Thread.sleep(RMIProperties.LITTLE_SLEEP_TIME);
        tmpDg.writeInt(connectionPort);
        tmp.send(tmpDg);
        tmp.close();
    }

    /**
     * Listen forever for unreliable connections and process the requests.
     */
    public void run() {
        try {
            String uri = RMIProperties.UNRELIABLE_PROTOCOL + "://:" + ourPort;
            RadiogramConnection rCon = (RadiogramConnection) Connector.open(uri);
            Datagram dg = (Datagram) rCon.newDatagram(rCon.getMaximumLength());
            Datagram dgReply = (Datagram) rCon.newDatagram(rCon.getMaximumLength());
            while (true) {
                dg.reset();
                try {
                    rCon.receive(dg);
                    byte operation = dg.readByte();
                    if (operation == ProtocolOpcode.HOST_ADDR_REQUEST) {
                        discoverRequest(dg.getAddress());
                    } else {
                        int connectionPort = RemoteGarbageCollector.getFreePort();
                        if (operation == ProtocolOpcode.INVOKE_REQUEST) {
                            invokeRequest(dg.getAddress(), connectionPort);
                        } else {
                            dgReply.reset();
                            dgReply.setAddress(dg.getAddress());
                            dgReply.writeInt(connectionPort);
                            rCon.send(dgReply);
                        }
                        Tunnel tunnel = new Tunnel(dg.getAddress(), connectionPort);
                        (new Thread(tunnel)).start();
                    }
                } catch (Exception ex) {
                    dg.reset();
                    dgReply.reset();
                    rCon.close();
                    rCon = (RadiogramConnection) Connector.open(uri);
                    dg = (Radiogram) rCon.newDatagram(rCon.getMaximumLength());
                    dgReply = (Datagram) rCon.newDatagram(rCon.getMaximumLength());
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Fatal error!");
        }
    }

    /**
     * This tunnel represent a reliable connection which will be used to
     * treat requests. The classes that inherit Server will need to treat the
     * dispatch method.
     */
    public class Tunnel implements Runnable {

        private String tunnelAddress;

        private int tunnelPort;

        private RMIUnicastConnection reliableCon;

        public Tunnel(String addr, int port) throws IOException {
            tunnelAddress = addr;
            tunnelPort = port;
            reliableCon = RMIUnicastConnection.makeServerConnection(addr, port);
        }

        public void run() {
            try {
                RMIProperties.log("Initiated Tunnel with: " + tunnelAddress + ":" + tunnelPort);
                RMIRequest req = reliableCon.readRequest();
                Thread.sleep(RMIProperties.LITTLE_SLEEP_TIME);
                RMIReply reply = service(req);
                if (reply != null) reliableCon.writeReply(reply);
                reliableCon.close();
                RMIProperties.log("Finished Tunnel with: " + tunnelAddress + ":" + tunnelPort);
            } catch (Exception ex) {
            }
        }
    }
}
