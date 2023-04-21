package net.sf.mailsomething.gui.contacts;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import net.sf.jpim.contact.gui.ContactPropertiesFrame;
import net.sf.jpim.contact.model.Contact;
import net.sf.mailsomething.contact.ContactList;
import net.sf.mailsomething.gui.GuiUser;
import net.sf.mailsomething.gui.event.GuiAction;

/**
 * @author Stig Tanggaard
 * @created 09-06-2003
 * 
 */
public class NewContactAction extends GuiAction {

    private ContactList list;

    public NewContactAction(JComponent parent, ContactList list) {
        super(parent, "New...", KeyStroke.getKeyStroke(KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK), "ctrl + n");
        this.list = list;
    }

    /**
	 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
	 */
    public void actionPerformed(ActionEvent arg0) {
        final ContactPropertiesFrame frame = new ContactPropertiesFrame("New Contact");
        frame.setSize(600, 425);
        frame.allowEdit(true);
        GuiUser.getInstance().showFrameCentered(frame, true);
        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                Contact contact = frame.getContact();
                list.addContact(contact);
            }
        });
    }
}
