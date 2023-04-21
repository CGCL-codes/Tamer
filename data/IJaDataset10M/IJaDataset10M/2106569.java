package jkad.controller.handlers.request;

import java.math.BigInteger;
import jkad.controller.handlers.Controller;
import jkad.controller.handlers.RequestHandler;
import jkad.protocol.KadProtocolException;
import jkad.protocol.rpc.request.StoreRPC;
import jkad.protocol.rpc.response.StoreResponse;
import jkad.structures.buffers.RPCBuffer;
import jkad.structures.kademlia.KadNode;
import jkad.structures.kademlia.RPCInfo;

public class StoreHandler extends RequestHandler<StoreResponse> {

    private KadNode node;

    private BigInteger key;

    private BigInteger rpcID;

    private BigInteger value;

    private Status actualStatus;

    private RPCBuffer outputBuffer;

    public StoreHandler() {
        this.node = null;
        this.key = null;
        this.value = null;
        this.rpcID = null;
        this.outputBuffer = RPCBuffer.getSentBuffer();
        this.actualStatus = Status.NOT_STARTED;
    }

    public BigInteger getKey() {
        return key;
    }

    public void setKey(BigInteger key) {
        this.key = key;
    }

    public BigInteger getRpcID() {
        return rpcID;
    }

    public void setRpcID(BigInteger rpcID) {
        this.rpcID = rpcID;
    }

    public KadNode getNode() {
        return node;
    }

    public void setNode(KadNode node) {
        this.node = node;
    }

    public BigInteger getValue() {
        return value;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }

    public Status getStatus() {
        return actualStatus;
    }

    public void run() {
        actualStatus = Status.PROCESSING;
        BigInteger nodeID = this.node.getNodeID();
        String ipAndPort = this.node.getIpAndPort();
        linea = (Thread.currentThread().getThreadGroup().getName() + " : Salvando valor " + value + " con clave " + key + " en el nodo " + nodeID + " con ip y puerto " + ipAndPort);
        fich.writelog(linea);
        System.out.println(linea);
        StoreRPC rpc = new StoreRPC();
        try {
            rpc.setDestinationNodeID(node.getNodeID());
            rpc.setSenderNodeID(Controller.getMyID());
            rpc.setRPCID(rpcID);
            rpc.setKey(this.key);
            rpc.setValue(value);
            rpc.setPiece((byte) 1);
            rpc.setPieceTotal((byte) 1);
            RPCInfo<StoreRPC> rpcInfo = new RPCInfo<StoreRPC>(rpc, node.getIpAddress().getHostAddress(), node.getPort());
            outputBuffer.add(rpcInfo);
        } catch (KadProtocolException e) {
            linea = ("WARN. KADPROTOCOL EXCEPION : " + e);
            fich.writelog(linea);
        }
        actualStatus = Status.ENDED;
    }

    public void addResult(RPCInfo<StoreResponse> rpcInfo) {
        BigInteger rpcID = rpcInfo.getRPC().getRPCID();
        linea = ("WARN : " + Thread.currentThread().getThreadGroup().getName() + " : Respuesta a un comando Store no esperada. Respuesta de " + rpcInfo.getIPAndPort() + " con rpcID " + rpcID);
        fich.writelog(linea);
    }

    public void clear() {
        this.node = null;
        this.key = null;
        this.value = null;
        this.rpcID = null;
        this.outputBuffer = RPCBuffer.getSentBuffer();
        this.actualStatus = Status.NOT_STARTED;
    }
}
