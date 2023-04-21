package com.jot.system;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import com.jot.system.messages.AutoRpc;
import com.jot.system.nio.JotSocket;
import com.jot.system.pjson.Entity;
import com.jot.system.pjson.Guid;
import com.jot.system.pjson.PJSONName;
import com.jot.system.pjson.PjsonFactory;
import com.jot.system.pjson.PjsonParseUtil;
import com.jot.system.pjson.PjsonWriteUtil;
import com.jot.system.transaction.ObjectServerDiedTransaction;
import com.jot.system.utils.JotTime;

@PJSONName("Ack")
public class Ack extends AutoRpc {

    public static Logger logger = Logger.getLogger(Ack.class);

    public Ack(JotContext ggg, Guid dest) {
        super(ggg, dest);
    }

    public Ack(JotSocket jport) {
        super(jport);
    }

    public Ack(JotSocket jport, Guid destsession) {
        super(jport, destsession);
    }

    Ack() {
    }

    /**
     * get one, only one, of these back for every save
     * 
     * @param what
     */
    public void saveAck(Guid what, Guid transaction, Guid session, int serial) {
        if (clientrpc("saveAck", what, transaction, session, serial)) return;
        {
            RecurringAutoRpc rpc = g.getRecurring(session);
            if (rpc != null) {
                rpc.api.saveAck(what, serial);
            } else logger.error("expecting RecurringAutoRpc " + session);
        }
    }

    public void saveFailed(Guid what, Guid transaction, Guid session, int serial) {
        if (rpc("saveAck", what, transaction, session, serial)) return;
        {
            RecurringAutoRpc rpc = g.getRecurring(session);
            if (rpc != null) {
                rpc.api.saveFailedAck(what, serial);
            } else logger.error("expecting RecurringAutoRpc " + session);
        }
    }

    public void changeFailedAck(Guid what, Guid session, String message, int serial) {
        if (rpc("changeFailedAck", what, session, message, serial)) return;
        {
            RecurringAutoRpc rpc = g.getRecurring(session);
            if (rpc != null) {
                rpc.api.changeFailedAck(what, message, serial);
            } else logger.error("expecting RecurringAutoRpc " + session);
        }
    }

    /**
     * a get failed to find an object
     * 
     * @param what
     * @param session
     */
    public void getMissing(Guid what, Guid session, int serial) {
        if (clientrpc("getMissing", what, session, serial)) return;
        {
            RecurringAutoRpc rpc = g.getRecurring(session);
            if (rpc != null) {
                rpc.api.getFailedAck(what, serial);
            } else logger.error("expecting RecurringAutoRpc " + session);
        }
    }

    public void disconnect() {
        if (rpc("disconnect")) return;
        sock.stopRunning();
    }

    public void forgetSession(Guid session) {
        if (rpc("forgetSession", session)) return;
        g.widgetServers.unwatchSession(session);
    }

    public void forgetItemWidget(Guid item, Guid widgetServername) {
        if (rpc("forgetItemWidget", item, widgetServername)) return;
        JotSocket wsock = g.getPeerPort(widgetServername);
        g.objectServers.item2widgetSock.remove(item, wsock);
    }

    public void getEntities(Integer b, List<Guid> es) {
        if (rpc("getEntities", b, es)) return;
        List<CachedObject> entities = new ArrayList<CachedObject>(es.size());
        for (Guid guid : es) {
            CachedObject eee = g.objectServers.find(guid);
            CachedObject ie = new CachedObject(eee.guid, eee.version, eee.entity);
            entities.add(ie);
        }
        Ack ack = new Ack(sock);
        ack.returnEntities(b, entities);
    }

    public void returnEntities(Integer b, List<CachedObject> entities) {
        if (rpc("returnEntities", b, entities)) return;
    }

    public void setGuidsForBucket(Integer bucket, List<EntityInfo> entities, int total) {
        if (rpc("setGuidsForBucket", bucket, entities, total)) return;
    }

    public void disconnectMe(Guid session) {
        if (rpc("disconnectMe", session)) return;
        if (logger.isTraceEnabled()) logger.trace("server sending disconnect to " + sock.peer.getName());
        sock.stopRunning();
    }

    public void xxsetObjectWidgetRelationship(Guid entity) {
        if (rpc("xxsetObjectWidgetRelationship", entity)) return;
        if (g.objectServers.objCacheBuckets.find(entity) != null) g.objectServers.item2widgetSock.add(entity, sock);
    }

    public void repairItem(Guid guid, byte[] json, int version, int old) {
        if (rpc("repairItem", guid, json, version, old)) return;
        logger.info("repairing " + guid + " to version " + version + " from " + old);
        Entity eee = JotMojasiParser.bytes2Entity(json, g);
        g.objectServers.put(guid, sock, eee, version, json);
    }

    /**
     * rather than have everyone ping everyone else every second I'm going to keep track of the replies I've
     * received so I can report to a peer, in my reply, not only that I am still up but that I have some friends
     * who I know are also still up.
     */
    static class SockReplyRecord {

        JotSocket sock;

        long when;

        SockReplyRecord(JotSocket sock) {
            this.sock = sock;
            when = JotTime.get();
        }
    }

    private static LinkedList<SockReplyRecord> recentReplies = new LinkedList<SockReplyRecord>();

    public void ping() {
        if (rpc("ping")) return;
        long last20ms = JotTime.get() - 100;
        List<Guid> freshList = null;
        synchronized (recentReplies) {
            while (recentReplies.size() > 0) {
                SockReplyRecord tmp = recentReplies.getFirst();
                if (tmp.when < last20ms) {
                    recentReplies.removeFirst();
                } else break;
            }
            freshList = new ArrayList<Guid>(recentReplies.size());
            for (SockReplyRecord tmps : recentReplies) {
                freshList.add(tmps.sock.peer.id.name);
            }
        }
        if (logger.isTraceEnabled()) logger.trace("sending ping reply to " + sock.peer.getName() + " with " + freshList.size() + " peers");
        Ack ack = new Ack(sock);
        ack.reply(freshList);
    }

    public void reply(List<Guid> freshList) {
        if (rpc("reply", freshList)) return;
        if (sock != null) synchronized (recentReplies) {
            recentReplies.addLast(new SockReplyRecord(sock));
        }
        if (logger.isTraceEnabled()) logger.info("received ping reply from " + sock.peer.getName() + " with " + freshList.size() + " peers");
        for (Guid guid : freshList) {
            JotSocket fresh = g.getPeerPort(guid);
            if (fresh != null) fresh.bump();
        }
    }

    public void toastServer(Guid toastee) {
        if (rpc("toastServer", toastee)) return;
        Guid manager = g.peer.getName();
        for (Guid server : g.getKnownGuids()) {
            setDestGuid(server);
            logger.info("sending toastServer to " + server);
            toastServer2(toastee, manager);
        }
    }

    public void toastServer2(Guid toastee, Guid manager) {
        if (rpc("toastServer2", toastee, manager)) return;
        if (g.peer.getName().equals(toastee)) {
            logger.info("context is now toasted: " + toastee);
            g.toast = true;
        } else {
            JotSocket s = g.getPeerPort(toastee);
            if (s != null) {
                logger.info("socket is now toasted: " + toastee);
                if (g.peer.getName().equals(manager)) {
                    ObjectServerDiedTransaction trans = new ObjectServerDiedTransaction(s);
                    trans.serverDied(s);
                }
                s.toast = true;
            } else logger.error("Attempt to toast unknown server " + toastee);
        }
    }

    public void startBigDownload(Guid bigbytearrayguid, Guid saverguid) {
        if (rpc("startBigDownload", bigbytearrayguid, saverguid)) return;
    }

    public void addBigDownload(Guid saverguid, byte[] data) {
        if (rpc("addBigDownload", saverguid, data)) return;
    }

    /**
     * This is the serialization interface for all objects in the object store. I got sick of debugging the
     * reflection involved with other serialization methods, and they were slow.
     * </p>
     * It is generated by <code>GenPseudoJson</code> so don't bother editing it. <code>GenPseudoJson</code>
     * will generate code to support adding and deleting instance variables by keeping state in a json file of the
     * same name as this class. To get a fresh version simply delete the json file and run
     * <code>GenPseudoJson</code>
     */
    public void writePjson(PjsonWriteUtil writer) {
    }

    @SuppressWarnings("unchecked")
    private void parseVersion1Pjson(PjsonParseUtil parser) throws Exception {
        g = parser.global;
        sock = parser.sock;
    }

    public static class AckFactory extends PjsonFactory {

        public Object make(PjsonParseUtil parser) throws Exception {
            Ack tmp = new Ack();
            tmp.parseVersion1Pjson(parser);
            return tmp;
        }
    }

    public static PjsonFactory factory = new AckFactory();

    public void forwardGet(Guid item, Guid widgetServer, Guid session, int serial) {
        if (rpc("forwardGet", item, widgetServer, session, serial)) return;
        JotSocket wsock = g.getPeerPort(widgetServer);
        try {
            g.objectServers.handleGet(item, wsock, session, serial);
        } catch (Exception e) {
            logger.error("failed forwardGet + item", e);
        }
    }
}
