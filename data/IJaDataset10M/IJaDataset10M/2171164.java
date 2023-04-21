package aimclient;

import net.kano.joscar.ByteBlock;
import net.kano.joscar.flap.ClientFlapConn;
import net.kano.joscar.flapcmd.SnacCommand;
import net.kano.joscar.net.ClientConnEvent;
import net.kano.joscar.snac.SnacPacketEvent;
import net.kano.joscar.snaccmd.FullRoomInfo;
import net.kano.joscar.snaccmd.FullUserInfo;
import net.kano.joscar.snaccmd.chat.ChatMsg;
import net.kano.joscar.snaccmd.chat.RecvChatMsgIcbm;
import net.kano.joscar.snaccmd.chat.SendChatMsgIcbm;
import net.kano.joscar.snaccmd.chat.UsersJoinedCmd;
import net.kano.joscar.snaccmd.chat.UsersLeftCmd;
import aimclient.security.SecureSession;
import aimclient.security.SecureSessionException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ChatConn extends ServiceConn {

    protected FullRoomInfo roomInfo;

    protected List listeners = new ArrayList();

    protected boolean joined = false;

    protected Set members = new HashSet();

    private SecureSession secureSession;

    public ChatConn(String host, int port, JoscarTester tester, ByteBlock cookie, FullRoomInfo roomInfo) {
        super(host, port, tester, cookie, 0x000e);
        this.roomInfo = roomInfo;
        this.secureSession = tester.getSecureSession();
    }

    public void sendMsg(String msg) {
        request(new SendChatMsgIcbm(new ChatMsg(msg)));
    }

    public FullRoomInfo getRoomInfo() {
        return roomInfo;
    }

    public String getRoomName() {
        return roomInfo.getRoomName();
    }

    public FullUserInfo[] getMembers() {
        return (FullUserInfo[]) members.toArray(new FullUserInfo[0]);
    }

    protected void handleStateChange(ClientConnEvent e) {
        super.handleStateChange(e);
        Object state = e.getNewState();
        if (state == ClientFlapConn.STATE_CONNECTED) {
            fireConnectedEvent();
        } else if (state == ClientFlapConn.STATE_FAILED) {
            fireConnFailedEvent(e.getReason());
        } else if (state == ClientFlapConn.STATE_NOT_CONNECTED) {
            if (joined) fireLeftEvent(e.getReason()); else fireConnFailedEvent(e.getReason());
        }
    }

    protected void handleSnacPacket(SnacPacketEvent e) {
        super.handleSnacPacket(e);
        SnacCommand cmd = e.getSnacCommand();
        if (cmd instanceof UsersJoinedCmd) {
            UsersJoinedCmd ujc = (UsersJoinedCmd) cmd;
            members.addAll(Arrays.asList(ujc.getUsers()));
            if (!joined) {
                fireJoinedEvent(ujc.getUsers());
                joined = true;
            } else {
                fireUsersJoinedEvent(ujc.getUsers());
            }
        } else if (cmd instanceof UsersLeftCmd) {
            UsersLeftCmd ulc = (UsersLeftCmd) cmd;
            members.removeAll(Arrays.asList(ulc.getUsers()));
            fireUsersLeftEvent(ulc.getUsers());
        } else if (cmd instanceof RecvChatMsgIcbm) {
            RecvChatMsgIcbm icbm = (RecvChatMsgIcbm) cmd;
            fireMsgEvent(icbm.getSenderInfo(), icbm.getMessage());
        }
    }

    public void addChatListener(ChatConnListener l) {
        if (!listeners.contains(l)) listeners.add(l);
    }

    public void removeChatListener(ChatConnListener l) {
        listeners.remove(l);
    }

    protected void fireConnectedEvent() {
        for (Iterator it = listeners.iterator(); it.hasNext(); ) {
            ChatConnListener l = (ChatConnListener) it.next();
            l.connected(this);
        }
    }

    protected void fireConnFailedEvent(Object reason) {
        for (Iterator it = listeners.iterator(); it.hasNext(); ) {
            ChatConnListener l = (ChatConnListener) it.next();
            l.connFailed(this, reason);
        }
    }

    protected void fireJoinedEvent(FullUserInfo[] members) {
        for (Iterator it = listeners.iterator(); it.hasNext(); ) {
            ChatConnListener l = (ChatConnListener) it.next();
            l.joined(this, members);
        }
    }

    protected void fireLeftEvent(Object reason) {
        for (Iterator it = listeners.iterator(); it.hasNext(); ) {
            ChatConnListener l = (ChatConnListener) it.next();
            l.left(this, reason);
        }
    }

    protected void fireUsersJoinedEvent(FullUserInfo[] members) {
        for (Iterator it = listeners.iterator(); it.hasNext(); ) {
            ChatConnListener l = (ChatConnListener) it.next();
            l.usersJoined(this, members);
        }
    }

    protected void fireUsersLeftEvent(FullUserInfo[] members) {
        for (Iterator it = listeners.iterator(); it.hasNext(); ) {
            ChatConnListener l = (ChatConnListener) it.next();
            l.usersLeft(this, members);
        }
    }

    protected void fireMsgEvent(FullUserInfo sender, ChatMsg msg) {
        for (Iterator it = listeners.iterator(); it.hasNext(); ) {
            ChatConnListener l = (ChatConnListener) it.next();
            l.gotMsg(this, sender, msg);
        }
    }

    public String toString() {
        return "ChatConn: " + roomInfo.getRoomName();
    }

    public void sendEncMsg(String msg) {
        byte[] encrypted;
        try {
            encrypted = secureSession.encryptChatMsg(this.getRoomName(), msg);
            request(new SendChatMsgIcbm(new ChatMsg("application/pkcs7-mime", "binary", "us-ascii", ByteBlock.wrap(encrypted), Locale.getDefault())));
            System.out.println("sent encrypted msg..");
        } catch (SecureSessionException e) {
            e.printStackTrace();
        }
    }
}
