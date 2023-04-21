package org.tcpfile.xmpp;

import org.jivesoftware.smack.packet.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XMPPContact {

    private static Logger log = LoggerFactory.getLogger(XMPPContact.class);

    private final XMPPAccount acc;

    private final String name;

    private XMPPCon con;

    public XMPPContact(XMPPAccount acc, String name) {
        super();
        this.acc = acc;
        this.name = name;
    }

    public XMPPCon getConnection() {
        if (con != null) return con;
        con = new XMPPCon(acc.getConnection(), this);
        return con;
    }

    public String getName() {
        return name;
    }

    public void sendMessage(String text) {
        getConnection().sendMessage(text);
    }

    public void sendMessage(Message text) {
        getConnection().sendMessage(text);
    }
}
