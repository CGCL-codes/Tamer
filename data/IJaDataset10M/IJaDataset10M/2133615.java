package chanukah.ui;

import chanukah.pd.Phone;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * <p>Creating/Editing an Phone numbers</p>
 *
 * <p>$Id: PhoneEditView.java,v 1.7 2004/01/23 17:04:02 mbaumgartner Exp $</p>
 *
 * @author Manuel Baumgartner  | <a href="mailto:m1baumga@hsr.ch">m1baumga@hsr.ch</a>
 * @version $Revision: 1.7 $
 */
public class PhoneEditView extends JPanel {

    private JButton cancelButton = new JButton();

    private JButton deleteButton = new JButton();

    private JButton saveButton = new JButton();

    private JComboBox phoneTypeBox = new JComboBox();

    private JLabel phoneLabel = new JLabel();

    private JLabel typeLabel = new JLabel();

    private JTextField phoneTextField = new JTextField();

    /**
	 *
	 * @param email
	 */
    public PhoneEditView(Phone phone) {
        super();
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.decode("#999999"));
        Font font = cancelButton.getFont().deriveFont(Font.PLAIN);
        cancelButton.setText("Cancel");
        cancelButton.setToolTipText("Cancel and discard unsaved data");
        saveButton.setText("Save");
        saveButton.setToolTipText("Save changed Phone number");
        deleteButton.setText("Delete");
        deleteButton.setToolTipText("Delete this Phone number");
        phoneLabel.setText("Phone Number");
        phoneTextField.setText(phone.getPhone());
        phoneTextField.setToolTipText("Enter Phone number");
        typeLabel.setText("Type");
        phoneTypeBox.setFont(font);
        phoneTypeBox.setEditable(true);
        phoneTypeBox.addItem("Work");
        phoneTypeBox.addItem("Home");
        phoneTypeBox.addItem("Mobile");
        phoneTypeBox.addItem(phone.getDataType());
        phoneTypeBox.setSelectedItem(phone.getDataType());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(1, 1, 1, 1);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 5;
        this.add(typeLabel, constraints);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weightx = 80;
        this.add(phoneTypeBox, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0;
        this.add(phoneLabel, constraints);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.weightx = 10;
        this.add(phoneTextField, constraints);
        constraints.gridx = 2;
        constraints.gridy = 1;
        constraints.weightx = 0;
        this.add(saveButton, constraints);
        constraints.gridx = 3;
        constraints.gridy = 1;
        constraints.weightx = 0;
        this.add(cancelButton, constraints);
        constraints.gridx = 4;
        constraints.gridy = 1;
        constraints.weightx = 0;
        this.add(deleteButton, constraints);
    }

    /**
	 * Default Constructor
	 */
    private PhoneEditView() {
        super();
    }

    /**
	 *
	 * @param email
	 */
    void setPhoneData(Phone phone) {
        this.phoneTextField.setText(phone.getPhone());
    }

    /**
	 *
	 * @return
	 */
    Phone getPhoneData() {
        return new Phone(this.phoneTextField.getText(), this.phoneTypeBox.getSelectedItem().toString());
    }

    /**
     *
     * @param actionListener
     */
    void addCancelButtonActionListener(ActionListener actionListener) {
        this.cancelButton.addActionListener(actionListener);
    }

    /**
     *
     */
    void removeCancelButtonActionListeners() {
        ActionListener[] list = cancelButton.getActionListeners();
        for (int i = 0; i < list.length; i++) cancelButton.removeActionListener(list[i]);
    }

    /**
     *
     * @param actionListener
     */
    void addDeleteButtonActionListener(ActionListener actionListener) {
        this.deleteButton.addActionListener(actionListener);
    }

    /**
     *
     */
    void removeDeleteButtonActionListeners() {
        ActionListener[] list = deleteButton.getActionListeners();
        for (int i = 0; i < list.length; i++) deleteButton.removeActionListener(list[i]);
    }

    /**
     *
     * @param actionListener
     */
    void addSaveButtonActionListener(ActionListener actionListener) {
        this.saveButton.addActionListener(actionListener);
    }

    /**
     *
     */
    void removeSaveButtonActionListeners() {
        ActionListener[] list = saveButton.getActionListeners();
        for (int i = 0; i < list.length; i++) saveButton.removeActionListener(list[i]);
    }
}
