package net.sf.jml.protocol.incoming;

import net.sf.jml.MsnMessenger;
import net.sf.jml.MsnProtocol;
import net.sf.jml.impl.AbstractMessenger;
import net.sf.jml.impl.MsnContactListImpl;
import net.sf.jml.protocol.MsnIncomingMessage;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.protocol.outgoing.OutgoingCHG;
import net.sf.jml.protocol.outgoing.OutgoingUUX;
import net.sf.jml.util.NumberUtils;

/**
 * The response of OutgoingSYN, indicate the contact list size
 * and group count.
 * <p>
 * Supported Protocl: All
 * <p>
 * MSNP8/MSNP9 Syntax: SYN trId currentVersion( contactCount groupCount)
 * <p>
 * MSNP10 Syntax: SYN trId lastTimeChanged lastBeChangedTime( contactCount groupCount)
 * 
 * @author Roger Chen
 */
public class IncomingSYN extends MsnIncomingMessage {

    public IncomingSYN(MsnProtocol protocol) {
        super(protocol);
    }

    public String getVersion() {
        if (protocol.before(MsnProtocol.MSNP10)) {
            return getParam(0);
        }
        return getParam(0) + " " + getParam(1);
    }

    public int getContactCount() {
        String contactCount;
        if (protocol.before(MsnProtocol.MSNP10)) {
            contactCount = getParam(1);
        } else {
            contactCount = getParam(2);
        }
        if (contactCount == null) {
            return -1;
        }
        return NumberUtils.stringToInt(contactCount);
    }

    public int getGroupCount() {
        String groupCount;
        if (protocol.before(MsnProtocol.MSNP10)) {
            groupCount = getParam(2);
        } else {
            groupCount = getParam(3);
        }
        if (groupCount == null) {
            return -1;
        }
        return NumberUtils.stringToInt(groupCount);
    }

    @Override
    protected void messageReceived(MsnSession session) {
        super.messageReceived(session);
        MsnMessenger messenger = session.getMessenger();
        MsnContactListImpl contactList = (MsnContactListImpl) messenger.getContactList();
        String version = getVersion();
        if (version.equals(contactList.getVersion())) {
            ((AbstractMessenger) messenger).fireContactListSyncCompleted();
        } else {
            int groupCount = getGroupCount();
            if (!protocol.before(MsnProtocol.MSNP10)) {
                groupCount++;
            }
            contactList.setVersion(version);
            contactList.setGroupCount(groupCount);
            contactList.setContactCount(getContactCount());
        }
        OutgoingCHG message = new OutgoingCHG(protocol);
        message.setStatus(messenger.getOwner().getInitStatus());
        message.setClientId(messenger.getOwner().getClientId());
        message.setDisplayPicture(messenger.getOwner().getDisplayPicture());
        message.setFirstSend(true);
        messenger.send(message);
        OutgoingUUX uuxmessage = new OutgoingUUX(protocol);
        uuxmessage.setPersonalMessage(messenger.getOwner().getPersonalMessage());
        messenger.send(uuxmessage);
    }
}
