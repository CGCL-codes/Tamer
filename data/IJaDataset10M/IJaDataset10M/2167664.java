package com.jiaho.xmpp.core.chat.multiuserchat;

import com.jiaho.xmpp.core.connection.JiahoConnection;
import com.jiaho.xmpp.core.connection.gateways.transports.JiahoRoomConfiguration;
import com.jiaho.xmpp.core.connection.gateways.transports.JiahoRoomConfiguration.CONFIG;
import com.jiaho.xmpp.core.pojos.JiahoMessage;
import com.jiaho.xmpp.core.pojos.JiahoMessageCollection;
import com.jiaho.xmpp.core.util.log.JiahoLog;
import com.jiaho.xmpp.core.util.messages.JiahoGeneralProperties;
import com.jiaho.xmpp.core.util.messages.JiahoMessageUtils;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromContainsFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.Affiliate;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.Occupant;
import org.jivesoftware.smackx.muc.ParticipantStatusListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A GroupChat is a conversation that takes place among many users in a virtual
 * room. When joining a group chat, you specify a nickname, which is the
 * identity that other chat room users see.
 *
 * @author Manuel Martins
 */
public class JiahoMultiUserChatManager {

    private final List<JiahoMessageCollection> messageList = new ArrayList<JiahoMessageCollection>();

    private final Hashtable<String, MultiUserChat> chatList = new Hashtable<String, MultiUserChat>();

    private final Hashtable<String, List<String>> participants = new Hashtable<String, List<String>>();

    private final JiahoConnection connection;

    /**
     * Constructor
     *
     * @param connection the xmpp connection.
     */
    public JiahoMultiUserChatManager(final JiahoConnection connection) {
        this.connection = connection;
    }

    /**
     * Gets the Multi-User Chat Address: at_address
     *
     * @return <code>List</code> the List containing the MUC Addresses
     */
    public List<String> getMUCAddresses() {
        final List<String> temp = new ArrayList<String>();
        try {
            for (final String name : MultiUserChat.getServiceNames(this.getConnection().getConnection())) {
                if (name.contains("conference") || name.contains("chat") || name.contains("muc")) {
                    temp.add(name);
                }
            }
        } catch (final XMPPException e) {
            JiahoLog.Warn(JiahoMultiUserChatManager.class, this.getConnection().getConnection().getUser() + " : " + JiahoGeneralProperties.WarnMUCAddress, e);
        }
        return temp;
    }

    /**
     * This Method Creates or joins the user to a MUC.
     *
     * @param mucAddress the MUC Address
     * @param room       the room name
     * @param nickname   the user nickname
     * @return true if user is Owner
     */
    public Boolean createJoinMultiUserChat(final String mucAddress, final String room, final String nickname) {
        final StringBuffer roomAddress = new StringBuffer(room);
        roomAddress.append("@");
        roomAddress.append(mucAddress);
        final MultiUserChat muc = new MultiUserChat(this.getConnection().getConnection(), roomAddress.toString());
        this.chatList.put(room, muc);
        this.addListeners(this.getConnection().getConnection(), room);
        this.join(room, nickname);
        return this.isOwner(room);
    }

    /**
     * This Method is used to check if user is Owner.
     *
     * @param room the room name
     * @return true if user is Owner
     */
    public Boolean isOwner(final String room) {
        Boolean isOwner = false;
        if (this.chatList.containsKey(room)) {
            try {
                final MultiUserChat muc = this.chatList.get(room);
                final Collection<Affiliate> owners = muc.getOwners();
                for (final Affiliate name : owners) {
                    isOwner = name.getJid().equalsIgnoreCase(this.getConnection().getJID());
                }
            } catch (final XMPPException e) {
                JiahoLog.Warn(JiahoMultiUserChatManager.class, this.getConnection().getConnection().getUser() + " : " + JiahoGeneralProperties.WarnCheckOwner, e);
            }
        }
        return isOwner;
    }

    private void addListeners(final XMPPConnection connection, final String room) {
        PacketFilter messageFilter = new AndFilter(new FromContainsFilter(room), new PacketTypeFilter(Message.class));
        messageFilter = new AndFilter(messageFilter, new PacketFilter() {

            @Override
            public boolean accept(final Packet packet) {
                final Message msg = (Message) packet;
                return msg.getType() == Message.Type.groupchat;
            }
        });
        connection.addPacketListener(new PacketListener() {

            @Override
            public void processPacket(final Packet packet) {
                processMUCMessages(room);
            }
        }, messageFilter);
        this.chatList.get(room).addParticipantStatusListener(new ParticipantStatusListener() {

            @Override
            public void joined(final String arg0) {
                final String temp = StringUtils.parseResource(arg0);
                sendMUCMessage(room, temp + " has joined the room");
            }

            @Override
            public void left(final String arg0) {
                sendMUCMessage(room, arg0 + " has left the room");
            }

            @Override
            public void kicked(final String arg0, final String arg1, final String arg2) {
                sendMUCMessage(room, arg0 + " has been kicked");
            }

            @Override
            public void voiceGranted(final String arg0) {
            }

            @Override
            public void voiceRevoked(final String arg0) {
            }

            @Override
            public void banned(final String arg0, final String arg1, final String arg2) {
                sendMUCMessage(room, arg0 + " has been banned");
            }

            @Override
            public void membershipGranted(final String arg0) {
            }

            @Override
            public void membershipRevoked(final String arg0) {
            }

            @Override
            public void moderatorGranted(final String arg0) {
                sendMUCMessage(room, arg0 + " is now a moderator");
            }

            @Override
            public void moderatorRevoked(final String arg0) {
                sendMUCMessage(room, arg0 + " has been revoked from moderator");
            }

            @Override
            public void ownershipGranted(final String arg0) {
                sendMUCMessage(room, arg0 + " is now owner");
            }

            @Override
            public void ownershipRevoked(final String arg0) {
                sendMUCMessage(room, arg0 + " has been revoked from ownership");
            }

            @Override
            public void adminGranted(final String arg0) {
                sendMUCMessage(room, arg0 + " is now an admin");
            }

            @Override
            public void adminRevoked(final String arg0) {
                sendMUCMessage(room, arg0 + " has been revoked from admin");
            }

            @Override
            public void nicknameChanged(final String arg0, final String arg1) {
                sendMUCMessage(room, arg0 + " changed his nickname to " + arg1);
            }
        });
    }

    /**
     * This Method is used to join MUCs.
     *
     * @param room     the room name
     * @param nickname the user nickname
     */
    public void join(final String room, final String nickname) {
        if (this.chatList.containsKey(room)) {
            try {
                final MultiUserChat muc = this.chatList.get(room);
                muc.join(nickname);
            } catch (final XMPPException e) {
                JiahoLog.Warn(JiahoMultiUserChatManager.class, this.getConnection().getConnection().getUser() + " : " + JiahoGeneralProperties.WarnJoinMUC, e);
            }
        }
    }

    /**
     * This Method is used to join MUCs using password.
     *
     * @param room     the room name
     * @param nickname the user nickname
     * @param password the room password
     */
    public void join(final String room, final String nickname, final String password) {
        if (!this.chatList.containsKey(room)) {
            try {
                final MultiUserChat muc = this.chatList.get(room);
                muc.join(nickname, password);
            } catch (final XMPPException e) {
                JiahoLog.Warn(JiahoMultiUserChatManager.class, this.getConnection().getConnection().getUser() + " : " + JiahoGeneralProperties.WarnJoinMUC, e);
            }
        }
    }

    private void processMUCMessages(final String room) {
        if (this.chatList.containsKey(room)) {
            Message m = this.chatList.get(room).pollMessage();
            List<JiahoMessage> temp;
            JiahoMessage msg;
            JiahoMessageCollection col;
            while (m != null) {
                msg = JiahoMessageUtils.transformMessageInJiahoMessage(m);
                if (this.messageList.isEmpty()) {
                    temp = new ArrayList<JiahoMessage>();
                    temp.add(msg);
                    col = new JiahoMessageCollection(room, temp);
                    this.messageList.add(col);
                } else {
                    String participantAddress;
                    String participantBareAddress;
                    for (final JiahoMessageCollection aMessageList : this.messageList) {
                        participantAddress = aMessageList.getParticipant();
                        participantBareAddress = StringUtils.parseResource(aMessageList.getParticipant());
                        if (participantAddress.equals(room) || participantBareAddress.equals(room)) {
                            aMessageList.addMessage(msg);
                        }
                    }
                }
                m = this.chatList.get(room).pollMessage();
            }
        }
    }

    /**
     * This Method is used to set the room configurations. Only works if user is
     * Owner of the room.
     *
     * @param room           the room name
     * @param configurations the Map containing the configurations
     */
    public void setRoomConfiguration(final String room, final Map<CONFIG, JiahoRoomConfiguration> configurations) {
        if (this.chatList.containsKey(room)) {
            try {
                final MultiUserChat muc = this.chatList.get(room);
                final Form form = JiahoMultiChatFormConfiguration.configure(configurations);
                muc.sendConfigurationForm(form);
            } catch (final XMPPException e) {
                JiahoLog.Warn(JiahoMultiUserChatManager.class, this.getConnection().getConnection().getUser() + " : " + JiahoGeneralProperties.WarnMUCRoomConfiguration, e);
            }
        }
    }

    /**
     * Destroy a MUC.
     *
     * @param room         the room name
     * @param reason       the reason for destruction
     * @param alternateJID an alternate new room address
     */
    public void destroy(final String room, final String reason, final String alternateJID) {
        if (this.chatList.containsKey(room)) {
            try {
                this.chatList.get(room).destroy(reason, alternateJID);
            } catch (final XMPPException e) {
                JiahoLog.Warn(JiahoMultiUserChatManager.class, this.getConnection().getConnection().getUser() + " : " + JiahoGeneralProperties.WarnDestroingMUC, e);
            }
        }
    }

    /**
     * Returns a List containing all the New MUC messages.
     *
     * @param room the room name
     * @return <code>List</Code> the List of messages
     */
    public List<JiahoMessage> getAllMUCNewMessages(final String room) {
        final List<JiahoMessage> temp = new ArrayList<JiahoMessage>();
        for (final JiahoMessageCollection aMessageList : this.messageList) {
            for (final JiahoMessage msg : aMessageList.getNewMessages()) {
                temp.add(msg);
            }
        }
        return temp;
    }

    /**
     * Returns a List containing all the MUC messages.
     *
     * @param room the room name
     * @return <code>List</Code> the List of messages
     */
    public List<JiahoMessage> getAllMUCMessages(final String room) {
        this.processMUCMessages(room);
        final List<JiahoMessage> temp = new ArrayList<JiahoMessage>();
        for (final JiahoMessageCollection aMessageList : this.messageList) {
            for (final JiahoMessage msg : aMessageList.getAllMessages()) {
                temp.add(msg);
            }
        }
        return temp;
    }

    /**
     * This Method is used to send invitation to user.
     *
     * @param room    the room name
     * @param jid     the invited jid
     * @param message the message
     */
    public void sendInvitation(final String room, final String jid, final String message) {
        if (this.chatList.containsKey(room)) {
            final MultiUserChat muc = this.chatList.get(room);
            muc.invite(jid, message);
        }
    }

    /**
     * This Method is used to send invitations to users.
     *
     * @param room    the room name
     * @param jids    the invited jids
     * @param message the message
     */
    public void sendInvitations(final String room, final List<String> jids, final String message) {
        if (this.chatList.containsKey(room)) {
            final MultiUserChat muc = this.chatList.get(room);
            for (final String listJid : jids) {
                muc.invite(listJid, message);
            }
        }
    }

    /**
     * This Method is used to send message to MUC.
     *
     * @param room    the room name
     * @param message the message
     */
    public void sendMUCMessage(final String room, final String message) {
        if (this.chatList.containsKey(room)) {
            try {
                this.chatList.get(room).sendMessage(message);
            } catch (final XMPPException e) {
                JiahoLog.Warn(JiahoMultiUserChatManager.class, this.getConnection().getConnection().getUser() + " : " + JiahoGeneralProperties.WarnSendMessageMUC, e);
            }
        }
    }

    /**
     * This Method is used to ban an user.
     *
     * @param room   the room name
     * @param reason the reason message
     */
    public void banUser(final String room, final String reason) {
        if (this.chatList.containsKey(room)) {
            try {
                final MultiUserChat muc = this.chatList.get(room);
                muc.banUser(room, reason);
            } catch (XMPPException e) {
                JiahoLog.Warn(JiahoMultiUserChatManager.class, this.getConnection().getConnection().getUser() + " : " + JiahoGeneralProperties.WarnBanUserMUC, e);
            }
        }
    }

    /**
     * This Method is used to ban a list of users.
     *
     * @param room the room name
     * @param jids the List of jids
     */
    public void banUsers(final String room, final Collection<String> jids) {
        if (this.chatList.containsKey(room)) {
            try {
                final MultiUserChat muc = this.chatList.get(room);
                muc.banUsers(jids);
            } catch (final XMPPException e) {
                JiahoLog.Warn(JiahoMultiUserChatManager.class, this.getConnection().getConnection().getUser() + " : " + JiahoGeneralProperties.WarnBanUsersMUC, e);
            }
        }
    }

    /**
     * Gets the Owners of the room.
     *
     * @param room the room name
     */
    public List<Affiliate> getOwners(final String room) {
        final List<Affiliate> temp = new ArrayList<Affiliate>();
        try {
            temp.addAll(this.chatList.get(room).getOwners());
        } catch (XMPPException e) {
            JiahoLog.Warn(JiahoMultiUserChatManager.class, this.getConnection().getConnection().getUser() + " : " + JiahoGeneralProperties.WarnMUCOwners, e);
        }
        return temp;
    }

    /**
     * Gets the Moderators of the room.
     *
     * @param room the room name
     */
    public List<Occupant> getModerators(final String room) {
        final List<Occupant> temp = new ArrayList<Occupant>();
        try {
            temp.addAll(this.chatList.get(room).getModerators());
        } catch (XMPPException e) {
            JiahoLog.Warn(JiahoMultiUserChatManager.class, this.getConnection().getConnection().getUser() + " : " + JiahoGeneralProperties.WarnMUCOwners, e);
        }
        return temp;
    }

    /**
     * Gets the Users of the room.
     *
     * @param room the room name
     */
    public List<Occupant> getParticipants(final String room) {
        final List<Occupant> temp = new ArrayList<Occupant>();
        try {
            temp.addAll(this.chatList.get(room).getParticipants());
        } catch (XMPPException e) {
            JiahoLog.Warn(JiahoMultiUserChatManager.class, this.getConnection().getConnection().getUser() + " : " + JiahoGeneralProperties.WarnMUCOwners, e);
        }
        return temp;
    }

    /**
     * Gets the Users of the room.
     *
     * @return List<String> a List of rooms
     */
    public List<String> getJoinedRooms() {
        final List<String> temp = new ArrayList<String>();
        final Iterator<String> it = MultiUserChat.getJoinedRooms(this.getConnection().getConnection(), this.getConnection().getJID());
        while (it.hasNext()) {
            temp.add(it.next());
        }
        return temp;
    }

    /**
     * Gets the public Hosted Rooms.
     *
     * @param serviceAddress the service address
     */
    public List<HostedRoom> getHostedRooms(final String serviceAddress) {
        final List<HostedRoom> temp = new ArrayList<HostedRoom>();
        try {
            temp.addAll(MultiUserChat.getHostedRooms(this.getConnection().getConnection(), serviceAddress));
        } catch (final XMPPException ex) {
            Logger.getLogger(JiahoMultiUserChatManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return temp;
    }

    public JiahoConnection getConnection() {
        return connection;
    }
}
