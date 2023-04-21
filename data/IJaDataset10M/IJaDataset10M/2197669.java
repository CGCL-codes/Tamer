package chanukah.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import chanukah.pd.Email;
import chanukah.ui.interfaces.BasicInteractions;

/**
 * <p>This class is the Controller for EmailEditView and EmailDetailView, steering
 * display and editing of an E-Mail Address</p>
 *
 * <p>$Id: EmailView.java,v 1.12 2004/02/26 22:39:08 phuber Exp $</p>
 *
 * @author Patrick Huber  | <a href="mailto:phuber@users.sf.net">phuber@users.sf.net</a>
 * @version $Revision: 1.12 $
 */
public class EmailView extends JPanel {

    private Email email = null;

    private EmailDetailView emailDetailView = null;

    private EmailEditView emailEditView = null;

    /**
	 * @param email
	 */
    public EmailView(Email email) {
        this();
        this.email = email;
        this.setLayout(new GridLayout(1, 1));
        emailDetailView = new EmailDetailView(this.email);
        emailDetailView.addEditButtonActionListener(new editButtonActionListener());
        emailEditView = new EmailEditView(this.email);
        emailEditView.addSaveButtonActionListener(new saveButtonActionListener());
        emailEditView.addCancelButtonActionListener(new cancelButtonActionListener());
        emailEditView.addDeleteButtonActionListener(new deleteButtonActionListener());
        this.add(emailDetailView, emailDetailView.getClass().getName());
    }

    /**
	 *
	 */
    private EmailView() {
        super();
    }

    /**
	 * Repaint the PersonTotalView
	 */
    private void repaintAll() {
        ((BasicInteractions) this.getParent().getParent()).fireDataModified(this);
    }

    /**
	 * Switch to the Detail view
	 */
    private void switchToDetailView() {
        this.remove(emailEditView);
        this.add(emailDetailView);
        this.repaintAll();
    }

    /**
	 * Switch to the Edit View
	 */
    private void switchToEditView() {
        this.remove(emailDetailView);
        this.add(emailEditView);
        this.repaintAll();
    }

    /**
	 * <p>Cancel all changes - display EmailDetailView</p>
	 * @author Patrick Huber  | <a href="mailto:phuber@users.sf.net">phuber@users.sf.net</a>
	 */
    private class cancelButtonActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            switchToDetailView();
        }
    }

    /**
	 * <p></p>
	 * @author Patrick Huber  | <a href="mailto:phuber@users.sf.net">phuber@users.sf.net</a>
	 */
    private class deleteButtonActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            BasicInteractions parent = (BasicInteractions) getParent().getParent();
            email.deleteObserver(emailDetailView);
            parent.getEntity().removeData(email);
            email = null;
            emailDetailView.removeEditButtonActionListeners();
            emailEditView.removeDeleteButtonActionListeners();
            emailEditView.removeSaveButtonActionListeners();
            emailEditView.removeCancelButtonActionListeners();
            removeAll();
            emailDetailView = null;
            emailEditView = null;
            parent.fireDataRemoved(EmailView.this);
        }
    }

    /**
	 * <p>Provide the EmailEditView with the data from the ProblemDomain class and
	 * display the EmailEditView</p>
	 * @author Patrick Huber  | <a href="mailto:phuber@users.sf.net">phuber@users.sf.net</a>
	 */
    private class editButtonActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            emailEditView.setEmailData(email);
            switchToEditView();
        }
    }

    /**
	 * <p>Assign Data From EmailEditView to the ProblemDomain class and display
	 * the EmailDetailView</p>
	 * @author Patrick Huber  | <a href="mailto:phuber@users.sf.net">phuber@users.sf.net</a>
	 */
    private class saveButtonActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            email.setEmail(emailEditView.getEmailData().getEmail());
            email.setDataType(EmailView.this.emailEditView.getEmailData().getDataType());
            switchToDetailView();
        }
    }
}
