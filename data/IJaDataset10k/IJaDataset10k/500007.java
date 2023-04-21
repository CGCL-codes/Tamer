package mathgame.login;

import mathgame.common.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class NewUserDialog extends JDialog {

    private Frame parent;

    private boolean isTeacher;

    private boolean closeAfterCreate = false;

    private NewUserPanel newUserPanel;

    public NewUserDialog(Frame parent) {
        this(parent, false);
        this.closeAfterCreate = true;
    }

    public NewUserDialog(Frame parent, boolean isTeacher) {
        super(parent, true);
        this.parent = parent;
        this.isTeacher = isTeacher;
        newUserPanel = new NewUserPanel(this, isTeacher);
        if (!isTeacher) setTitle("Ny anv�ndare (Elev)"); else setTitle("Ny anv�ndare (Lärare)");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().add(newUserPanel, BorderLayout.CENTER);
        pack();
        setResizable(false);
        setLocation(parent.getSize().width / 2 - getSize().width / 2, parent.getSize().height / 2 - getSize().height / 2);
        setLocationRelativeTo(parent);
    }

    public void reset() {
        newUserPanel.reset();
        setLocation(parent.getSize().width / 2 - getSize().width / 2, parent.getSize().height / 2 - getSize().height / 2);
        setLocationRelativeTo(parent);
    }

    private class NewUserPanel extends JPanel {

        private boolean isTeacher;

        private JDialog invoker;

        private JLabel firstNameLabel;

        private JLabel lastNameLabel;

        private JLabel loginNrLabel;

        private JLabel classLabel;

        private JLabel usernameLabel;

        private JLabel passwordLabel;

        private JLabel confirmPasswordLabel;

        private JTextField firstNameField;

        private JTextField lastNameField;

        private JTextField loginNrField;

        private JTextField classField;

        private JTextField usernameField;

        private JPasswordField passwordField;

        private JPasswordField confirmPasswordField;

        private JButton createUserButton;

        private JButton cancelButton;

        public NewUserPanel(JDialog invoker, boolean isTeacher) {
            this.invoker = invoker;
            this.isTeacher = isTeacher;
            firstNameLabel = new JLabel();
            lastNameLabel = new JLabel();
            loginNrLabel = new JLabel();
            classLabel = new JLabel();
            usernameLabel = new JLabel();
            passwordLabel = new JLabel();
            confirmPasswordLabel = new JLabel();
            firstNameField = new JTextField(new TextLengthChecker(Database.SIZE_PERSON_FORNAMN), "", 10);
            lastNameField = new JTextField(new TextLengthChecker(Database.SIZE_PERSON_EFTERNAMN), "", 10);
            loginNrField = new JTextField(new TextLengthChecker(Database.SIZE_PERSON_PERSONNUMMER, true), "", 10);
            classField = new JTextField(new TextLengthChecker(Database.SIZE_PERSON_KLASS), "", 10);
            usernameField = new JTextField(new TextLengthChecker(Database.SIZE_PERSON_ANVANDARNAMN), "", 10);
            passwordField = new JPasswordField(new TextLengthChecker(Database.SIZE_PERSON_LOSENORD), "", 10);
            confirmPasswordField = new JPasswordField(new TextLengthChecker(Database.SIZE_PERSON_LOSENORD), "", 10);
            createUserButton = new JButton();
            cancelButton = new JButton();
            setLayout(null);
            if (!isTeacher) setBorder(new TitledBorder("Skapa ny elev")); else setBorder(new TitledBorder("Skapa ny lärare"));
            createUserButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    createUserButtonActionPerformed(evt);
                }
            });
            cancelButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    cancelButtonActionPerformed(evt);
                }
            });
            firstNameLabel.setHorizontalAlignment(SwingConstants.TRAILING);
            lastNameLabel.setHorizontalAlignment(SwingConstants.TRAILING);
            loginNrLabel.setHorizontalAlignment(SwingConstants.TRAILING);
            classLabel.setHorizontalAlignment(SwingConstants.TRAILING);
            usernameLabel.setHorizontalAlignment(SwingConstants.TRAILING);
            passwordLabel.setHorizontalAlignment(SwingConstants.TRAILING);
            confirmPasswordLabel.setHorizontalAlignment(SwingConstants.TRAILING);
            firstNameLabel.setText("F�rnamn");
            lastNameLabel.setText("Efternamn");
            loginNrLabel.setText("Personnummer");
            classLabel.setText("Klass");
            usernameLabel.setText("Anv�ndarnamn");
            passwordLabel.setText("L�senord");
            confirmPasswordLabel.setText("Bekr�fta l�senord");
            passwordField.setFont(firstNameField.getFont());
            confirmPasswordField.setFont(firstNameField.getFont());
            createUserButton.setText("Skapa anv�ndare");
            cancelButton.setText("Avbryt");
            Dimension labelSize = new Dimension(Math.max(firstNameLabel.getPreferredSize().width, Math.max(lastNameLabel.getPreferredSize().width, Math.max(loginNrLabel.getPreferredSize().width, Math.max(classLabel.getPreferredSize().width, Math.max(usernameLabel.getPreferredSize().width, Math.max(passwordLabel.getPreferredSize().width, confirmPasswordLabel.getPreferredSize().width)))))), Math.max(firstNameLabel.getPreferredSize().height, Math.max(lastNameLabel.getPreferredSize().height, Math.max(loginNrLabel.getPreferredSize().height, Math.max(classLabel.getPreferredSize().height, Math.max(usernameLabel.getPreferredSize().height, Math.max(passwordLabel.getPreferredSize().height, confirmPasswordLabel.getPreferredSize().height)))))));
            firstNameLabel.setSize(labelSize);
            lastNameLabel.setSize(labelSize);
            loginNrLabel.setSize(labelSize);
            classLabel.setSize(labelSize);
            usernameLabel.setSize(labelSize);
            passwordLabel.setSize(labelSize);
            confirmPasswordLabel.setSize(labelSize);
            firstNameField.setSize(firstNameField.getPreferredSize());
            lastNameField.setSize(lastNameField.getPreferredSize());
            loginNrField.setSize(loginNrField.getPreferredSize());
            classField.setSize(classField.getPreferredSize());
            usernameField.setSize(usernameField.getPreferredSize());
            passwordField.setSize(passwordField.getPreferredSize());
            confirmPasswordField.setSize(confirmPasswordField.getPreferredSize());
            createUserButton.setSize(createUserButton.getPreferredSize());
            cancelButton.setSize(cancelButton.getPreferredSize());
            int leftShift = 0;
            if (labelSize.width + 10 + confirmPasswordField.getSize().width < createUserButton.getSize().width + 10 + cancelButton.getSize().width) leftShift = (createUserButton.getSize().width + 10 + cancelButton.getSize().width) - (labelSize.width + 10 + confirmPasswordField.getSize().width);
            firstNameLabel.setLocation(leftShift + getInsets().left, getInsets().top + (firstNameLabel.getSize().height < firstNameField.getSize().height ? firstNameField.getSize().height - firstNameLabel.getSize().height : 0));
            lastNameLabel.setLocation(firstNameLabel.getLocation().x, firstNameLabel.getLocation().y + firstNameLabel.getSize().height + (lastNameLabel.getSize().height < lastNameField.getSize().height ? lastNameField.getSize().height - lastNameLabel.getSize().height : 0) + 10);
            loginNrLabel.setLocation(lastNameLabel.getLocation().x, lastNameLabel.getLocation().y + lastNameLabel.getSize().height + (loginNrLabel.getSize().height < loginNrField.getSize().height ? loginNrField.getSize().height - loginNrLabel.getSize().height : 0) + 10);
            classLabel.setLocation(loginNrLabel.getLocation().x, loginNrLabel.getLocation().y + loginNrLabel.getSize().height + (classLabel.getSize().height < classField.getSize().height ? classField.getSize().height - classLabel.getSize().height : 0) + 10);
            usernameLabel.setLocation(classLabel.getLocation().x, classLabel.getLocation().y + classLabel.getSize().height + (usernameLabel.getSize().height < usernameField.getSize().height ? usernameField.getSize().height - usernameLabel.getSize().height : 0) + 10);
            passwordLabel.setLocation(usernameLabel.getLocation().x, usernameLabel.getLocation().y + usernameLabel.getSize().height + (passwordLabel.getSize().height < passwordField.getSize().height ? passwordField.getSize().height - passwordLabel.getSize().height : 0) + 10);
            confirmPasswordLabel.setLocation(passwordLabel.getLocation().x, passwordLabel.getLocation().y + passwordLabel.getSize().height + (confirmPasswordLabel.getSize().height < confirmPasswordField.getSize().height ? confirmPasswordField.getSize().height - confirmPasswordLabel.getSize().height : 0) + 10);
            firstNameField.setLocation(firstNameLabel.getLocation().x + firstNameLabel.getSize().width + 10, (firstNameLabel.getLocation().y + firstNameLabel.getSize().height / 2 - firstNameField.getSize().height / 2));
            lastNameField.setLocation(lastNameLabel.getLocation().x + lastNameLabel.getSize().width + 10, (lastNameLabel.getLocation().y + lastNameLabel.getSize().height / 2 - lastNameField.getSize().height / 2));
            loginNrField.setLocation(loginNrLabel.getLocation().x + loginNrLabel.getSize().width + 10, (loginNrLabel.getLocation().y + loginNrLabel.getSize().height / 2 - loginNrField.getSize().height / 2));
            classField.setLocation(classLabel.getLocation().x + classLabel.getSize().width + 10, (classLabel.getLocation().y + classLabel.getSize().height / 2 - classField.getSize().height / 2));
            usernameField.setLocation(usernameLabel.getLocation().x + usernameLabel.getSize().width + 10, (usernameLabel.getLocation().y + usernameLabel.getSize().height / 2 - usernameField.getSize().height / 2));
            passwordField.setLocation(passwordLabel.getLocation().x + passwordLabel.getSize().width + 10, (passwordLabel.getLocation().y + passwordLabel.getSize().height / 2 - passwordField.getSize().height / 2));
            confirmPasswordField.setLocation(confirmPasswordLabel.getLocation().x + confirmPasswordLabel.getSize().width + 10, (confirmPasswordLabel.getLocation().y + confirmPasswordLabel.getSize().height / 2 - confirmPasswordField.getSize().height / 2));
            createUserButton.setLocation(confirmPasswordLabel.getLocation().x + (confirmPasswordField.getLocation().x + confirmPasswordField.getSize().width - confirmPasswordLabel.getLocation().x) / 2 - (createUserButton.getSize().width + 10 + cancelButton.getSize().width) / 2, Math.max(passwordLabel.getLocation().y + passwordLabel.getSize().height, confirmPasswordLabel.getLocation().y + confirmPasswordLabel.getSize().height) + 10);
            cancelButton.setLocation(createUserButton.getLocation().x + createUserButton.getSize().width + 10, createUserButton.getLocation().y);
            add(firstNameLabel);
            add(lastNameLabel);
            add(loginNrLabel);
            add(classLabel);
            add(usernameLabel);
            add(passwordLabel);
            add(confirmPasswordLabel);
            add(firstNameField);
            add(lastNameField);
            add(loginNrField);
            add(classField);
            add(usernameField);
            add(passwordField);
            add(confirmPasswordField);
            add(createUserButton);
            add(cancelButton);
            Dimension preferredSize = new Dimension((Math.max(cancelButton.getLocation().x + cancelButton.getSize().width, confirmPasswordField.getLocation().x + confirmPasswordField.getSize().width) + Math.max(confirmPasswordLabel.getLocation().x, createUserButton.getLocation().x) - getInsets().left + getInsets().right), (cancelButton.getLocation().y + cancelButton.getSize().height + getInsets().bottom));
            setPreferredSize(preferredSize);
            setMinimumSize(preferredSize);
        }

        public void reset() {
            firstNameField.setText("");
            lastNameField.setText("");
            loginNrField.setText("");
            classField.setText("");
            usernameField.setText("");
            passwordField.setText("");
            confirmPasswordField.setText("");
            removeAll();
            add(firstNameLabel);
            add(lastNameLabel);
            add(loginNrLabel);
            add(classLabel);
            add(usernameLabel);
            add(passwordLabel);
            add(confirmPasswordLabel);
            add(firstNameField);
            add(lastNameField);
            add(loginNrField);
            add(classField);
            add(usernameField);
            add(passwordField);
            add(confirmPasswordField);
            add(createUserButton);
            add(cancelButton);
        }

        private boolean requiredFieldsNotEmpty() {
            return !(usernameField.getText().trim().equals("") || passwordField.getPassword().length == 0 || confirmPasswordField.getPassword().length == 0);
        }

        private void createUserButtonActionPerformed(ActionEvent evt) {
            if (requiredFieldsNotEmpty()) {
                boolean passwordsEqual = true;
                char[] password1 = passwordField.getPassword();
                char[] password2 = confirmPasswordField.getPassword();
                passwordField.setText("");
                confirmPasswordField.setText("");
                if (password1.length != password2.length) passwordsEqual = false; else {
                    for (int i = 0; i < password1.length; i++) {
                        if (password1[i] != password2[i]) {
                            passwordsEqual = false;
                            break;
                        }
                        password2[i] = 0;
                    }
                }
                password2 = null;
                if (passwordsEqual) {
                    String username = usernameField.getText();
                    String lastName = lastNameField.getText();
                    String firstName = firstNameField.getText();
                    String loginNr = loginNrField.getText();
                    String className = classField.getText();
                    boolean insertSucceeded;
                    if (!isTeacher) insertSucceeded = Database.getInstance().insertStudent(username, password1, firstName, lastName, loginNr, className); else insertSucceeded = Database.getInstance().insertTeacher(username, password1, firstName, lastName, loginNr, className);
                    for (int i = 0; i < password1.length; i++) password1[i] = 0;
                    password1 = null;
                    if (insertSucceeded) {
                        JOptionPane.showMessageDialog(invoker, "Registrering lyckades!", "Registrering lyckades!", JOptionPane.INFORMATION_MESSAGE);
                        if (closeAfterCreate) invoker.dispose(); else reset();
                    } else JOptionPane.showMessageDialog(invoker, "Registrering misslyckades!", "Registrering misslyckades!", JOptionPane.ERROR_MESSAGE);
                } else {
                    for (int i = 0; i < password1.length; i++) password1[i] = 0;
                    password1 = null;
                    JOptionPane.showMessageDialog(invoker, "De tv� inmatade l�senorden �verensst�mmer inte!", "Registrering misslyckades!", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                passwordField.setText("");
                confirmPasswordField.setText("");
                JOptionPane.showMessageDialog(invoker, "Du har gl�mt att fylla i n�got av de obligatoriska f�lten!", "Registrering misslyckades!", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void cancelButtonActionPerformed(ActionEvent evt) {
            invoker.dispose();
        }
    }
}
