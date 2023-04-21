package client.actions;

import client.Contact;
import client.ContactsFrame;
import client.Main;
import java.awt.event.ActionEvent;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;

/**
 *
 * @author Olivier
 */
public class RemoveContactAction extends AbstractAction {

    private ContactsFrame frame;

    public RemoveContactAction(ContactsFrame frame) {
        this.frame = frame;
    }

    public void actionPerformed(ActionEvent e) {
        Contact contact = frame.getSelectedContact();
        if (contact != null) {
            String message = "<message type=\"removebuddy\" account=\"" + contact.getName() + "\">\n";
            try {
                Main.getNetworkThread().send(Main.getNetworkThread().getSocketToServer(), message.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(NewContactAction.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
