package chanukah.ui;

import chanukah.pd.Address;
import chanukah.pd.Email;
import chanukah.pd.Entity;
import chanukah.pd.Group;
import chanukah.pd.Phone;
import chanukah.ui.interfaces.BasicInteractions;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

/**
 * <p>Contains all Elements to Display detailed Information about a group.</p>
 *
 * <p>$Id: GroupTotalView.java,v 1.10 2004/01/30 09:24:50 phuber Exp $</p>
 *
 * @author Manuel Baumgartner  | <a href="mailto:m1baumga@hsr.ch">m1baumga@hsr.ch</a>
 * @version $Revision: 1.10 $
 */
public class GroupTotalView extends JPanel implements BasicInteractions {

    private Group group;

    private JButton newAddressButton = new JButton();

    private JButton newEmailButton = new JButton();

    private JButton newPhoneButton = new JButton();

    private JLabel addressLabel = new JLabel();

    private JLabel emailLabel = new JLabel();

    private JLabel groupLabel = new JLabel();

    private JLabel phoneLabel = new JLabel();

    private JPanel addressPanel = new JPanel();

    private JPanel emailPanel = new JPanel();

    private JPanel phonePanel = new JPanel();

    private MainView mainView;

    /**
	 * Creates a new GroupTotalView object.
	 *
	 * @param group TODO documentation
	 */
    public GroupTotalView(Group group, MainView mainView) {
        super();
        this.group = group;
        this.mainView = mainView;
        this.setLayout(new GridBagLayout());
        Font font = emailLabel.getFont().deriveFont(Font.PLAIN);
        groupLabel.setFont(font);
        groupLabel.setText("In Groups:");
        emailLabel.setFont(font);
        emailLabel.setText("<html><font size='+1'>Email Address:</font></html>");
        phoneLabel.setFont(font);
        phoneLabel.setText("<html><font size='+1'>Phone numbers:</font></html>");
        addressLabel.setFont(font);
        addressLabel.setText("<html><font size='+1'>Address:</font></html>");
        newAddressButton.setText("(New)");
        newAddressButton.setBorder(null);
        newAddressButton.addActionListener(new newAddressButtonActionListener());
        newAddressButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        newEmailButton.setText("(New)");
        newEmailButton.setBorder(null);
        newEmailButton.addActionListener(new newEmailButtonActionListener());
        newEmailButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        newPhoneButton.setText("(New)");
        newPhoneButton.setBorder(null);
        newPhoneButton.addActionListener(new newPhoneButtonActionListener());
        newPhoneButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(1, 1, 1, 1);
        constraints.gridx = 0;
        constraints.weightx = 100;
        constraints.weighty = 0;
        constraints.gridy = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        this.add(new GroupView(group), constraints);
        constraints.gridy++;
        this.add(new JSeparator(SwingConstants.HORIZONTAL), constraints);
        constraints.gridy++;
        constraints.gridx = 0;
        this.add(new PathView(group.getParent().getPath(), mainView), constraints);
        constraints.gridy++;
        this.add(new JSeparator(SwingConstants.HORIZONTAL), constraints);
        constraints.gridy++;
        constraints.gridwidth = 2;
        this.add(addressLabel, constraints);
        constraints.gridx = 2;
        constraints.gridwidth = 1;
        constraints.weightx = 0;
        this.add(newAddressButton, constraints);
        constraints.gridx = 0;
        constraints.gridy++;
        this.add(new JLabel("    "), constraints);
        constraints.gridwidth = 2;
        constraints.weightx = 100;
        constraints.gridx = 1;
        this.add(addressPanel, constraints);
        constraints.gridy++;
        constraints.gridx = 0;
        constraints.gridwidth = 3;
        this.add(new JSeparator(SwingConstants.HORIZONTAL), constraints);
        constraints.gridy++;
        constraints.gridwidth = 2;
        this.add(phoneLabel, constraints);
        constraints.gridx = 2;
        constraints.gridwidth = 1;
        constraints.weightx = 0;
        this.add(newPhoneButton, constraints);
        constraints.gridx = 0;
        constraints.gridy++;
        this.add(new JLabel("    "), constraints);
        constraints.gridwidth = 2;
        constraints.weightx = 100;
        constraints.gridx = 1;
        this.add(phonePanel, constraints);
        constraints.gridy++;
        constraints.gridx = 0;
        constraints.gridwidth = 3;
        this.add(new JSeparator(SwingConstants.HORIZONTAL), constraints);
        constraints.gridy++;
        constraints.gridwidth = 2;
        this.add(emailLabel, constraints);
        constraints.gridx = 2;
        constraints.gridwidth = 1;
        constraints.weightx = 0;
        this.add(newEmailButton, constraints);
        constraints.gridx = 0;
        constraints.gridy++;
        this.add(new JLabel("    "), constraints);
        constraints.gridwidth = 2;
        constraints.weightx = 100;
        constraints.gridx = 1;
        this.add(emailPanel, constraints);
        constraints.gridy++;
        constraints.weighty = 100;
        constraints.gridx = 0;
        this.add(new JLabel(""), constraints);
        display();
    }

    public Entity getEntity() {
        return group;
    }

    /**
	 *
	 */
    public void display() {
        addressPanel.setLayout(new BoxLayout(addressPanel, BoxLayout.Y_AXIS));
        emailPanel.setLayout(new BoxLayout(emailPanel, BoxLayout.Y_AXIS));
        phonePanel.setLayout(new BoxLayout(phonePanel, BoxLayout.Y_AXIS));
        create_address();
        create_email();
        create_phone();
        validate();
    }

    public void fireDataAdded(Object obj) {
        if (obj.getClass() == Address.class) {
            addressPanel.add(new AddressView((Address) obj));
        } else if (obj.getClass() == Email.class) {
            emailPanel.add(new EmailView((Email) obj));
        } else if (obj.getClass() == Phone.class) {
            phonePanel.add(new PhoneView((Phone) obj));
        } else {
            System.err.println("Unkown Class");
        }
        repaintAll();
    }

    public void fireDataModified(Object obj) {
        repaintAll();
    }

    public void fireDataRemoved(Object obj) {
        if (obj.getClass() == AddressView.class) {
            addressPanel.remove((Component) obj);
        } else if (obj.getClass() == EmailView.class) {
            emailPanel.remove((Component) obj);
        } else if (obj.getClass() == PhoneView.class) {
            phonePanel.remove((Component) obj);
        } else {
            this.remove((Component) obj);
        }
        repaintAll();
    }

    /**
	 * Repaint the super containers (scrollpane)
	 */
    public void repaintAll() {
        this.getParent().getParent().validate();
        this.getParent().getParent().repaint();
    }

    /**
	 *
	 */
    private void create_address() {
        Iterator it = group.getData("chanukah.pd.Address").iterator();
        int i = 0;
        while (it.hasNext()) {
            addressPanel.add(new AddressView((Address) it.next()));
            i++;
        }
    }

    /**
	 *
	 */
    private void create_email() {
        Iterator it = group.getData("chanukah.pd.Email").iterator();
        int i = 0;
        while (it.hasNext()) {
            emailPanel.add(new EmailView((Email) it.next()));
            i++;
        }
    }

    /**
	 *
	 */
    private void create_phone() {
        Iterator it = group.getData("chanukah.pd.Phone").iterator();
        int i = 0;
        while (it.hasNext()) {
            phonePanel.add(new PhoneView((Phone) it.next()));
            i++;
        }
    }

    private class newAddressButtonActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Address address = new Address("New Address", "New Town", "New Country", "New Type");
            group.addData(address);
            fireDataAdded(address);
        }
    }

    private class newEmailButtonActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Email email = new Email("New Email", "New Type");
            group.addData(email);
            fireDataAdded(email);
        }
    }

    private class newPhoneButtonActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Phone phone = new Phone("New Phone", "new Type");
            group.addData(phone);
            fireDataAdded(phone);
        }
    }
}
