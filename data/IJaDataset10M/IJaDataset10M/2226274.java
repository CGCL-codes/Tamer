package net.sf.jml.event;

import net.sf.jml.MsnContact;
import net.sf.jml.MsnSwitchboard;
import net.sf.jml.message.MsnEmailInitMessage;
import net.sf.jml.message.MsnEmailNotifyMessage;
import net.sf.jml.message.MsnEmailActivityMessage;
import net.sf.jml.message.MsnEmailInitEmailData;

/**
 * MsnEmailListener adapter.
 *
 * @author Daniel Henninger
 */
public class MsnEmailAdapter implements MsnEmailListener {

    public void initialEmailNotificationReceived(MsnSwitchboard switchboard, MsnEmailInitMessage message, MsnContact contact) {
    }

    public void initialEmailDataReceived(MsnSwitchboard switchboard, MsnEmailInitEmailData message, MsnContact contact) {
    }

    public void newEmailNotificationReceived(MsnSwitchboard switchboard, MsnEmailNotifyMessage message, MsnContact contact) {
    }

    public void activityEmailNotificationReceived(MsnSwitchboard switchboard, MsnEmailActivityMessage message, MsnContact contact) {
    }
}
