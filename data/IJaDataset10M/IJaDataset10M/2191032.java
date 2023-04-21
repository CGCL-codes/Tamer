package net.sf.jml.protocol.incoming;

import net.sf.jml.Email;
import net.sf.jml.MsnContactList;
import net.sf.jml.MsnMessenger;
import net.sf.jml.MsnProtocol;
import net.sf.jml.MsnSwitchboard;
import net.sf.jml.impl.AbstractMessenger;
import net.sf.jml.impl.AbstractSwitchboard;
import net.sf.jml.impl.MsnContactImpl;
import net.sf.jml.protocol.MsnIncomingMessage;
import net.sf.jml.protocol.MsnSession;

/**
 * When the friend which you invited have join a switchboard, 
 * you will received that notice.
 * <p>
 * Supported Protocl: All
 * <p>
 * Syntax: JOI email displayName
 * 
 * @author Roger Chen
 */
public class IncomingJOI extends MsnIncomingMessage {

    public IncomingJOI(MsnProtocol protocol) {
        super(protocol);
    }

    @Override
    protected boolean isSupportTransactionId() {
        return false;
    }

    public Email getEmail() {
        return Email.parseStr(getParam(0));
    }

    public String getDisplayName() {
        return getParam(1);
    }

    @Override
    protected void messageReceived(MsnSession session) {
        super.messageReceived(session);
        MsnSwitchboard switchboard = session.getSwitchboard();
        if (switchboard != null) {
            MsnMessenger messenger = session.getMessenger();
            MsnContactList contactList = messenger.getContactList();
            MsnContactImpl contact = (MsnContactImpl) contactList.getContactByEmail(getEmail());
            if (contact == null) {
                contact = new MsnContactImpl(contactList);
                contact.setEmail(getEmail());
                contact.setDisplayName(getDisplayName());
            }
            ((AbstractSwitchboard) switchboard).addContact(contact);
            ((AbstractMessenger) messenger).fireContactJoinSwitchboard(switchboard, contact);
        }
    }
}
