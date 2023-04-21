package net.sf.jml.protocol.incoming;

import net.sf.jml.MsnProtocol;
import net.sf.jml.impl.MsnContactListImpl;
import net.sf.jml.impl.MsnGroupImpl;
import net.sf.jml.impl.AbstractMessenger;
import net.sf.jml.protocol.MsnIncomingMessage;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.util.NumberUtils;

/**
 * The response message of OutgoingRMG, indicate delete the
 * group successfully.
 * <p>
 * Supported Protocol: All
 * <p>
 * MSNP8/MSNP9 Syntax: RMG trId versionNum groupId
 * <p>
 * MSNP10 Syntax: RMG trId groupId
 * 
 * @author Roger Chen
 */
public class IncomingRMG extends MsnIncomingMessage {

    public IncomingRMG(MsnProtocol protocol) {
        super(protocol);
    }

    public int getVersion() {
        return NumberUtils.stringToInt(getParam(0));
    }

    public String getGroupId() {
        if (protocol.before(MsnProtocol.MSNP10)) {
            return getParam(1);
        }
        return getParam(0);
    }

    @Override
    protected void messageReceived(MsnSession session) {
        super.messageReceived(session);
        MsnContactListImpl contactList = (MsnContactListImpl) session.getMessenger().getContactList();
        MsnGroupImpl group = (MsnGroupImpl) contactList.getGroup(getGroupId());
        if (group != null) {
            group.clear();
            contactList.removeGroup(getGroupId());
            ((AbstractMessenger) session.getMessenger()).fireGroupRemoveCompleted(group);
        }
    }
}
