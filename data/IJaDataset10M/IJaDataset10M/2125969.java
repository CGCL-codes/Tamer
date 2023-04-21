package com.jot.system.visitors;

import org.apache.log4j.Logger;
import com.jot.system.JotContext;
import com.jot.system.RecurringAutoRpc;
import com.jot.system.bytemessages.ClientEntityRpc;
import com.jot.system.bytemessages.ClientRpc;
import com.jot.system.bytemessages.Get;
import com.jot.system.bytemessages.ObjectRpc;
import com.jot.system.bytemessages.Reply;
import com.jot.system.bytemessages.Save;
import com.jot.system.nio.JotSocket;
import com.jot.system.pjson.Guid;

public class SessionServerVisitor extends ByteMessageVisitorImpl {

    public static Logger logger = Logger.getLogger(SessionServerVisitor.class);

    public SessionServerVisitor(JotContext ggg) {
        super(ggg);
    }

    public void visitGet(byte[] bytes, JotSocket sock) throws Exception {
        g.sessionServers.gets.add(1);
        Get get = new Get(bytes, sock);
        UserSessionSocket uss = (UserSessionSocket) sock.attachment;
        if (uss == null) {
            uss = new UserSessionSocket();
            uss.session = get.getGuid(Get.Field.session);
            uss.socket = sock;
            g.sessionServers.session2Uss.put(uss.session, uss);
            sock.attachment = uss;
        }
        Guid widget = get.getGuid(Get.Field.widget);
        uss.addAndWrite(g, widget, get.bytes);
    }

    /**
     * this comes from a sock that belongs to a WidgetServer, not a client
     * 
     * @throws Exception
     * 
     * 
     */
    public void visitReply(byte[] bytes, JotSocket sock) throws Exception {
        Reply reply = new Reply(bytes, sock);
        Guid sess = reply.getGuid(Reply.Field.session);
        UserSessionSocket uss = g.sessionServers.session2Uss.get(sess);
        RecurringAutoRpc rpc = null;
        if (uss != null) {
            if (logger.isTraceEnabled()) logger.trace("reply to " + uss.session);
            if (uss.socket == null) {
                rpc = g.getRecurring(uss.session);
                if (rpc != null) rpc.handleReply(reply); else logger.error("visitReply no sock and no RecurringAutoRpc " + sess);
            } else uss.socket.write(reply.bytes);
        } else {
            logger.debug("ConnectVisitor bad session in reply from " + sock);
        }
    }

    public void visitSave(byte[] bytes, JotSocket sock) throws Exception {
        g.sessionServers.saves.add(1);
        Save save = new Save(bytes, sock);
        UserSessionSocket uss = (UserSessionSocket) sock.attachment;
        if (uss == null) {
            uss = new UserSessionSocket();
            uss.session = save.getGuid(Save.Field.session);
            uss.socket = sock;
            g.sessionServers.session2Uss.put(uss.session, uss);
            sock.attachment = uss;
        }
        Guid widget = save.getGuid(Save.Field.widget);
        uss.addAndWrite(g, widget, save.bytes);
    }

    public void visitObjectRpc(byte[] bytes, JotSocket sock) throws Exception {
        g.sessionServers.changes.add(1);
        ObjectRpc rpc = new ObjectRpc(bytes, sock);
        UserSessionSocket uss = (UserSessionSocket) sock.attachment;
        if (uss != null) {
            Guid widget = rpc.getGuid(ObjectRpc.Field.widget);
            uss.addAndWrite(g, widget, rpc.bytes);
        } else logger.error("visitObjectRpc but no value in sock.attachment");
    }

    public void disconnect(JotSocket sock) {
        Object obj = sock.attachment;
        if (obj != null) {
            if (obj instanceof UserSessionSocket) {
                UserSessionSocket uss = (UserSessionSocket) obj;
                g.sessionServers.session2Uss.remove(uss.session);
                logger.info("session disconnected " + uss.session);
                try {
                    uss.shutdown(g);
                } catch (Exception e) {
                    logger.error("error in disconnect ", e);
                }
                if (logger.isTraceEnabled()) logger.trace("session disconnected " + uss.session);
            } else {
                logger.error("unknown attachment to JotSocket " + obj.getClass().getName());
                super.disconnect(sock);
            }
            sock.attachment = null;
        } else {
            logger.trace("closing socket with unknown UserSessionSocket ");
            super.disconnect(sock);
        }
    }

    @Override
    public void visitClientRpc(byte[] bytes, JotSocket sock) throws Exception {
        ClientRpc rpc = new ClientRpc(bytes, sock);
        Guid sess = rpc.getGuid(ClientRpc.Field.session);
        UserSessionSocket uss = g.sessionServers.session2Uss.get(sess);
        if (uss != null) {
            if (logger.isTraceEnabled()) logger.info("visitClientRpc to " + uss.session);
            if (uss.socket == null) {
                RecurringAutoRpc rrpc = g.getRecurring(uss.session);
                if (rrpc != null) {
                    super.visitClientRpc(rpc, sock);
                } else logger.error("visitClientRpc no sock and no RecurringAutoRpc " + sess);
            } else uss.socket.write(rpc.bytes);
        } else {
            if (logger.isDebugEnabled()) logger.debug("ConnectVisitor bad session in visitClientRpc " + sock);
        }
    }

    @Override
    public void visitClientEntityRpc(byte[] bytes, JotSocket sock) throws Exception {
        ClientEntityRpc crpc = new ClientEntityRpc(bytes, sock);
        Guid session = crpc.getGuid(ClientEntityRpc.Field.session);
        int serial = crpc.getInt(ClientEntityRpc.Field.serial.offset);
        UserSessionSocket uss = g.sessionServers.session2Uss.get(session);
        if (uss != null) {
            if (logger.isTraceEnabled()) logger.info("visitClientRpc to " + uss.session);
            if (uss.socket == null) {
                RecurringAutoRpc rpc = g.getRecurring(uss.session);
                if (rpc != null) rpc.changeAck(crpc, session, serial); else logger.warn("visitClientEntityRpc no sock and no RecurringAutoRpc sess=" + session);
            } else {
                uss.socket.write(crpc.bytes);
            }
        } else {
            if (logger.isDebugEnabled()) logger.debug("ConnectVisitor bad session in visitClientEntityRpc " + sock.peer.getName() + " " + new String(crpc.getBytes(ClientEntityRpc.Field.json)));
        }
    }
}
