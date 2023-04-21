package jkad.controller.handlers.response;

import java.math.BigInteger;
import java.util.List;
import jkad.controller.handlers.Controller;
import jkad.controller.handlers.Handler;
import jkad.facades.storage.DataManagerFacade;
import jkad.protocol.KadProtocolException;
import jkad.protocol.rpc.request.FindValueRPC;
import jkad.protocol.rpc.response.FindNodeResponse;
import jkad.protocol.rpc.response.FindValueResponse;
import jkad.structures.buffers.RPCBuffer;
import jkad.structures.kademlia.KadNode;
import jkad.structures.kademlia.ContactsKAD;
import jkad.structures.kademlia.RPCInfo;

public class FindValueResponseHandler extends Handler<FindValueRPC> {

    private Status actualStatus;

    private ContactsKAD contacts;

    public FindValueResponseHandler() {
        actualStatus = Status.NOT_STARTED;
    }

    public Status getStatus() {
        return actualStatus;
    }

    public ContactsKAD getContacts() {
        return contacts;
    }

    public void setContacts(ContactsKAD contacts) {
        this.contacts = contacts;
    }

    @SuppressWarnings("unchecked")
    public void run() {
        try {
            actualStatus = Status.PROCESSING;
            RPCInfo<FindValueRPC> rpcInfo = getRPCInfo();
            FindValueRPC rpc = rpcInfo.getRPC();
            String key = rpc.getKey().toString(16);
            linea = (Thread.currentThread().getThreadGroup().getName() + " : Procesando FindValue request para la clave " + key + " procedente del nodo con ID " + rpc.getSenderNodeID() + " y con ip y puerto " + rpcInfo.getIPAndPort());
            fich.writelog(linea);
            System.out.println(linea);
            DataManagerFacade<String> storage = DataManagerFacade.getDataManager();
            String value = storage.get(rpc.getKey());
            if (value != null) {
                linea = (Thread.currentThread().getThreadGroup().getName() + " : Encontrado valor " + value + " para la clave " + key);
                fich.writelog(linea);
                System.out.println(linea);
                FindValueResponse response = new FindValueResponse();
                response.setSenderNodeID(Controller.getMyID());
                response.setDestinationNodeID(rpc.getSenderNodeID());
                response.setRPCID(rpc.getRPCID());
                response.setValue(new BigInteger(value.getBytes()));
                response.setPiece((byte) 1);
                response.setPieceTotal((byte) 1);
                RPCInfo<FindValueResponse> responseInfo = new RPCInfo<FindValueResponse>(response, rpcInfo.getIP(), rpcInfo.getPort());
                linea = (Thread.currentThread().getThreadGroup().getName() + " : Enviando FindValueResponse al nodo con ID  " + rpc.getSenderNodeID() + " y con IP y puerto " + rpcInfo.getIPAndPort());
                fich.writelog(linea);
                System.out.println(linea);
                RPCBuffer.getSentBuffer().add(responseInfo);
            } else {
                int amount = Integer.parseInt(System.getProperty("jkad.contacts.findamount"));
                linea = (Thread.currentThread().getThreadGroup().getName() + " : No encontrado valor para la clave " + key + ", buscando " + amount + " nodos cercanos a la clave " + key);
                fich.writelog(linea);
                System.out.println(linea);
                BigInteger keyb = rpc.getKey();
                BigInteger ID = Controller.getMyID();
                BigInteger aux = keyb.xor(ID);
                int cont = 159;
                while (aux.compareTo(BigInteger.ONE) != 0) {
                    aux = aux.shiftRight(1);
                    cont--;
                }
                List<KadNode> nodes = contacts.findClosestContacts(rpc.getKey(), amount, cont);
                linea = (Thread.currentThread().getThreadGroup().getName() + " : Encontrados " + nodes.size() + " nodos cercanos a  " + key + " para que el nodo " + rpc.getSenderNodeID() + " prosiga la b�squeda");
                fich.writelog(linea);
                System.out.println(linea);
                BigInteger myID = Controller.getMyID();
                RPCBuffer buffer = RPCBuffer.getSentBuffer();
                linea = (Thread.currentThread().getThreadGroup().getName() + " : Enviando " + nodes.size() + " nodos al nodo " + rpc.getSenderNodeID() + " con ip y puerto " + rpcInfo.getIPAndPort());
                fich.writelog(linea);
                System.out.println(linea);
                for (KadNode node : nodes) {
                    FindNodeResponse response = new FindNodeResponse();
                    response.setSenderNodeID(myID);
                    response.setDestinationNodeID(rpc.getSenderNodeID());
                    response.setRPCID(rpc.getRPCID());
                    response.setFoundNodeID(node.getNodeID());
                    response.setIpAddress(node.getIpAddress());
                    response.setPort(node.getPort());
                    RPCInfo<FindNodeResponse> responseInfo = new RPCInfo<FindNodeResponse>(response, rpcInfo.getIP(), rpcInfo.getPort());
                    linea = (Thread.currentThread().getThreadGroup().getName() + " : Enviando findNode response con el nodo encontrado " + node + " al nodo con IP y puerto " + rpcInfo.getIPAndPort());
                    fich.writelog(linea);
                    System.out.println(linea);
                    buffer.add(responseInfo);
                }
            }
            actualStatus = Status.ENDED;
        } catch (KadProtocolException e) {
            linea = ("WARN. KADPROTOCOL EXCEPION : " + e);
            fich.writelog(linea);
        }
    }

    public void clear() {
        this.actualStatus = Status.NOT_STARTED;
        this.setRPCInfo(null);
    }
}
