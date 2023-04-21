package net.jabbra.core.listeners;

import org.jivesoftware.smack.packet.*;
import org.jivesoftware.smack.PacketListener;
import net.jabbra.core.utils.JabbraUtils;
import net.jabbra.core.interfaces.JabbraPacketHandler;
import net.jabbra.core.roster.JabbraRoster;
import net.jabbra.core.JabbraConnectionContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Suprun
 * Date: 12.10.2007
 * Time: 11:15:43
 * To change this template use File | Settings | File Templates.
 */
public class JabbraPacketListener implements PacketListener {

    private JabbraPacketHandler messageHandler;

    public JabbraPacketListener(JabbraPacketHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public void processPacket(Packet packet) {
        Message message = (Message) packet;
        String toJID = JabbraUtils.getJIDWithoutResource(message.getTo());
        String fromJID = JabbraUtils.getJIDWithoutResource(message.getFrom());
        JabbraRoster roster = JabbraConnectionContainer.getConnectionByName(toJID).getRoster();
        messageHandler.processMessage(toJID, roster.findContact(fromJID), message.getBody());
    }
}
