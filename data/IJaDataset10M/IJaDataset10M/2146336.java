package net.sf.jml.protocol.incoming;

import net.sf.jml.Email;
import net.sf.jml.MsnProtocol;
import net.sf.jml.impl.BasicMessenger;
import net.sf.jml.protocol.MsnIncomingMessage;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.util.NumberUtils;
import net.sf.jml.util.StringUtils;

/**
 * Someone invited you to join a SB server.
 * <p>
 * Supported Protocol: All
 * <p>
 * Syntax: RNG sessionId SBIP:SBPort CKI authStr inviterEmail inviterDisplayName
 * 
 * @author Roger Chen
 */
public class IncomingRNG extends MsnIncomingMessage {

    public IncomingRNG(MsnProtocol protocol) {
        super(protocol);
    }

    @Override
    protected boolean isSupportTransactionId() {
        return false;
    }

    public int getSessionId() {
        return NumberUtils.stringToInt(getParam(0));
    }

    public String getConnectHost() {
        String reconnectInfo = getParam(1);
        return reconnectInfo.substring(0, reconnectInfo.indexOf(":"));
    }

    public int getConnectPort() {
        String reconnectInfo = getParam(1);
        return NumberUtils.stringToInt(reconnectInfo.substring(reconnectInfo.indexOf(":") + 1));
    }

    public String getAuthStr() {
        return getParam(3);
    }

    public Email getEmail() {
        return Email.parseStr(getParam(4));
    }

    public String getDisplayName() {
        return StringUtils.urlDecode(getParam(5));
    }

    @Override
    protected void messageReceived(MsnSession session) {
        super.messageReceived(session);
        ((BasicMessenger) session.getMessenger()).newSwitchboard(getConnectHost(), getConnectPort(), false, getAuthStr(), getSessionId(), null);
    }
}
