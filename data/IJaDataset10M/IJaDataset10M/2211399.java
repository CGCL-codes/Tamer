package net.sf.jml.protocol.incoming;

import net.sf.jml.MsnClientId;
import net.sf.jml.MsnProtocol;
import net.sf.jml.MsnUserStatus;
import net.sf.jml.impl.AbstractMessenger;
import net.sf.jml.impl.MsnOwnerImpl;
import net.sf.jml.protocol.MsnIncomingMessage;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.protocol.outgoing.OutgoingCHG;
import net.sf.jml.protocol.outgoing.OutgoingPNG;
import net.sf.jml.util.NumberUtils;
import net.sf.jml.util.StringUtils;

/**
 * OutgoingCHG's response message. User changes status.
 * <p>
 * Supported Protocol: All
 * <p>
 * MSNP8 Syntax: CHG trId userStatus clientId
 * <p>
 * MSNP9/MSNP10 Syntax: CHG trId userStatus clientId( msnObject)
 * 
 * @author Roger Chen
 */
public class IncomingCHG extends MsnIncomingMessage {

    public IncomingCHG(MsnProtocol protocol) {
        super(protocol);
    }

    public MsnUserStatus getStatus() {
        return MsnUserStatus.parseStr(getParam(0));
    }

    public int getClientId() {
        return NumberUtils.stringToInt(getParam(1));
    }

    public String getMsnObject() {
        String object = getParam(2);
        if (object != null) {
            return StringUtils.urlDecode(object);
        }
        return null;
    }

    @Override
    protected void messageReceived(MsnSession session) {
        super.messageReceived(session);
        MsnOwnerImpl owner = (MsnOwnerImpl) session.getMessenger().getOwner();
        owner.fSetClientId(MsnClientId.parseInt(getClientId()));
        owner.fSetStatusF(getStatus());
        ((AbstractMessenger) session.getMessenger()).fireOwnerStatusChanged();
        if ((getOutgoingMessage() instanceof OutgoingCHG) && ((OutgoingCHG) getOutgoingMessage()).isFirstSend()) {
            OutgoingPNG message = new OutgoingPNG(protocol);
            message.setForCheckContactListInitCompleted(true);
            session.sendAsynchronousMessage(message);
        }
    }
}
