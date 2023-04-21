package net.sourceforge.jcoupling.bus.server.callout;

import net.sourceforge.jcoupling.peer.property.RequestKey;
import net.sourceforge.jcoupling.peer.interaction.MessageID;
import net.sourceforge.jcoupling.bus.server.CommunicatorID;
import net.sourceforge.jcoupling.peer.ReceiveRequest;
import net.sourceforge.jcoupling.peer.Request;
import net.sourceforge.jcoupling.peer.KeyedRequest;
import net.sourceforge.jcoupling.bus.server.mql.GarbageCRequest;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

/**
 * @author Lachlan Aldred
 */
public class MemoryRequestDao implements RequestDao {

    private Map<MessageID, CommunicatorID> _sendRequests;

    private Map<RequestKey, KeyedRequest> _pendingReceives;

    public MemoryRequestDao() {
        _sendRequests = new HashMap<MessageID, CommunicatorID>();
        _pendingReceives = new HashMap<RequestKey, KeyedRequest>();
    }

    public void storeSend(MessageID messageID, CommunicatorID communicatorID) {
        System.out.println("MemoryRequestDao::adding messageID = " + messageID);
        System.out.println("MemoryRequestDao::communicatorID = " + communicatorID);
        _sendRequests.put(messageID, communicatorID);
    }

    public void storeReceive(ReceiveRequest request) {
        _pendingReceives.put(request.getKey(), request);
    }

    public CommunicatorID lookupSend(MessageID messageID) {
        return _sendRequests.get(messageID);
    }

    public Request lookupReceive(RequestKey requestKey) {
        return _pendingReceives.get(requestKey);
    }

    public Collection<KeyedRequest> getReceiveRequests() {
        return _pendingReceives.values();
    }

    public void removeReceiveRequest(RequestKey key) {
        _pendingReceives.remove(key);
    }

    public void storeGarbageC(GarbageCRequest req) {
        _pendingReceives.put(req.getKey(), req);
    }
}
