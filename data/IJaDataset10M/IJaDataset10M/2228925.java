package jkad.controller.handlers.request;

import java.math.BigInteger;
import jkad.controller.handlers.RequestHandler;
import jkad.protocol.KadProtocolException;
import jkad.protocol.rpc.request.PingRPC;
import jkad.protocol.rpc.response.PingResponse;
import jkad.structures.buffers.RPCBuffer;
import jkad.structures.kademlia.KadNode;
import jkad.structures.kademlia.ContactsKAD;
import jkad.structures.kademlia.RPCInfo;

public class PingHandler extends RequestHandler<PingResponse> {

    private Status actualStatus;

    private KadNode pingNode;

    private BigInteger rpcID;

    private RPCBuffer outputBuffer;

    private boolean response;

    private BigInteger myID;

    private ContactsKAD contacts;

    public PingHandler(BigInteger ID) {
        actualStatus = Status.NOT_STARTED;
        outputBuffer = RPCBuffer.getSentBuffer();
        pingNode = null;
        rpcID = null;
        response = false;
        myID = ID;
    }

    public KadNode getpingNode() {
        return pingNode;
    }

    public void setpingNode(KadNode pingNode) {
        this.pingNode = pingNode;
    }

    public ContactsKAD getContacts() {
        return contacts;
    }

    public void setContacts(ContactsKAD contacts) {
        this.contacts = contacts;
    }

    public BigInteger getRpcID() {
        return rpcID;
    }

    public void setRpcID(BigInteger rpcID) {
        this.rpcID = rpcID;
    }

    public void run() {
        actualStatus = Status.PROCESSING;
        linea = (Thread.currentThread().getThreadGroup().getName() + " : Arrancando proceso Ping al nodo " + pingNode.getNodeID() + " con IP y puerto " + pingNode.getIpAndPort());
        fich.writelog(linea);
        System.out.println(linea);
        requestNode(rpcID, pingNode);
    }

    private void requestNode(BigInteger rpcID, KadNode node) {
        this.requestNode(rpcID, node.getNodeID(), node.getIpAddress().getHostAddress(), node.getPort());
    }

    private void requestNode(BigInteger rpcID, BigInteger nodeID, String ip, int port) {
        PingRPC rpc = new PingRPC();
        try {
            linea = (Thread.currentThread().getThreadGroup().getName() + " : Ping al nodo " + nodeID + " con direcci�n ip y puerto: " + ip + ":" + port);
            fich.writelog(linea);
            System.out.println(linea);
            rpc.setRPCID(rpcID);
            rpc.setDestinationNodeID(nodeID);
            rpc.setSenderNodeID(myID);
            RPCInfo<PingRPC> rpcInfo = new RPCInfo<PingRPC>(rpc, ip, port);
            outputBuffer.add(rpcInfo);
        } catch (KadProtocolException e) {
            linea = ("WARN. KADPROTOCOL EXCEPION : " + e);
            fich.writelog(linea);
        }
    }

    public Status getStatus() {
        return actualStatus;
    }

    public void clear() {
        actualStatus = Status.NOT_STARTED;
        outputBuffer = RPCBuffer.getSentBuffer();
        pingNode = null;
        rpcID = null;
    }

    public void addResult(RPCInfo<PingResponse> rpcInfo) {
        PingResponse responseRPC = rpcInfo.getRPC();
        if ((responseRPC.getSenderNodeID().compareTo(pingNode.getNodeID()) == 0) && (responseRPC.getRPCID().compareTo(rpcID) == 0)) response = true; else response = false;
        if (response == true) {
            linea = (Thread.currentThread().getThreadGroup().getName() + " : PingResponse recibida del nodo con ID " + responseRPC.getSenderNodeID() + " y con IP y puerto " + rpcInfo.getIP() + " : " + rpcInfo.getPort());
            fich.writelog(linea);
            System.out.println(linea);
        }
    }

    public boolean getResponse() {
        return response;
    }
}
