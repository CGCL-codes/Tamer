package chanukah.ui;

import chanukah.pd.Person;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.toedter.calendar.JCalendar;

/**
 * <p></p>
 *
 * <p>$Id: PersonEditView.java,v 1.6 2004/01/30 11:03:00 mbaumgartner Exp $</p>
 *
 * @author Manuel Baumgartner  | <a href="mailto:m1baumga@hsr.ch">m1baumga@hsr.ch</a>
 * @version $Revision: 1.6 $
 */
public class PersonEditView extends JPanel {

    private JButton cancelButton = new JButton();

    private JButton saveButton = new JButton();

    private JLabel additionalnamesLabel = new JLabel();

    private JLabel birthdateLabel = new JLabel();

    private JLabel firstnameLabel = new JLabel();

    private JLabel lastnameLabel = new JLabel();

    private JLabel middlenameLabel = new JLabel();

    private JLabel titleLabel = new JLabel();

    private JTextField additionalnamesTextField = new JTextField();

    private JCalendar birthdateCalendar = new JCalendar();

    private JTextField firstnameTextField = new JTextField();

    private JTextField lastnameTextField = new JTextField();

    private JTextField middlenameTextField = new JTextField();

    private JTextField titleTextField = new JTextField();

    /**
	 * Creates a new PersonEditView object.
	 *
	 * @param person TODO documentation
	 */
    public PersonEditView(Person person) {
        this();
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.decode("#999999"));
        Font font = cancelButton.getFont().deriveFont(Font.PLAIN);
        cancelButton.setText("Cancel");
        cancelButton.setToolTipText("Cancel and discard unsaved data");
        saveButton.setText("Save");
        saveButton.setToolTipText("Save changed Person");
        titleLabel.setText("Title");
        titleTextField.setToolTipText("Enter Title (Dr, Prof, ...)");
        firstnameLabel.setText("First name");
        firstnameTextField.setToolTipText("Enter First name");
        middlenameLabel.setText("Middle name");
        middlenameTextField.setToolTipText("Enter Middle name");
        lastnameLabel.setText("Last name");
        lastnameTextField.setToolTipText("Enter Last name");
        additionalnamesLabel.setText("Additional names");
        additionalnamesTextField.setToolTipText("Enter additional names (Jr, Junior, ....)");
        birthdateLabel.setText("Birthdate");
        birthdateCalendar.setToolTipText("Birthdate");
        birthdateCalendar.setBackground(this.getBackground());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(1, 1, 1, 1);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 5;
        this.add(titleLabel, constraints);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weightx = 80;
        this.add(titleTextField, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 5;
        this.add(firstnameLabel, constraints);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.weightx = 80;
        this.add(firstnameTextField, constraints);
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.weightx = 5;
        this.add(middlenameLabel, constraints);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.weightx = 80;
        this.add(middlenameTextField, constraints);
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.weightx = 5;
        this.add(lastnameLabel, constraints);
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.weightx = 80;
        this.add(lastnameTextField, constraints);
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.weightx = 5;
        this.add(additionalnamesLabel, constraints);
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.weightx = 80;
        this.add(additionalnamesTextField, constraints);
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.weightx = 0;
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        this.add(birthdateLabel, constraints);
        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.weightx = 25;
        this.add(birthdateCalendar, constraints);
        constraints.gridx = 4;
        constraints.gridy = 5;
        constraints.weightx = 0;
        constraints.anchor = GridBagConstraints.LAST_LINE_START;
        this.add(saveButton, constraints);
        constraints.gridx = 5;
        constraints.gridy = 5;
        constraints.weightx = 0;
        this.add(cancelButton, constraints);
    }

    /**
	 * Default Constructor
	 */
    private PersonEditView() {
        super();
    }

    /**
	 *
	 * @param email
	 */
    void setPersonData(Person person) {
        Calendar birthdate = new GregorianCalendar();
        birthdate.setTime(person.getBirthDate());
        titleTextField.setText(person.getTitle());
        firstnameTextField.setText(person.getFirstName());
        middlenameTextField.setText(person.getMiddleName());
        lastnameTextField.setText(person.getLastName());
        additionalnamesTextField.setText(person.getAdditionalNames());
        birthdateCalendar.setCalendar(birthdate);
    }

    /**
	 *
	 * @return
	 */
    Person getPersonData() {
        return new Person(titleTextField.getText(), firstnameTextField.getText(), middlenameTextField.getText(), lastnameTextField.getText(), additionalnamesTextField.getText(), birthdateCalendar.getCalendar().getTime());
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
