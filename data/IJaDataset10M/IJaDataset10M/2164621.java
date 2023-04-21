package net.sf.kraken.muc;

import org.xmpp.packet.JID;

/**
 * @author Daniel Henninger
 */
public class MUCTransportRoomMember {

    public MUCTransportRoomMember(JID memberjid) {
        this.jid = memberjid;
    }

    public JID jid;

    public JID getJid() {
        return jid;
    }

    public void setJid(JID jid) {
        this.jid = jid;
    }
}
