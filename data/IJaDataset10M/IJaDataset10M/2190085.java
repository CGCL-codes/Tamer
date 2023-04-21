package password;

import org.gnf.oracle.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.borland.jbcl.layout.*;
import com.borland.dx.sql.dataset.*;
import java.util.*;
import javax.swing.event.*;

public class Frame1 extends javax.swing.JDialog {

    String VERSION = "v1.3";

    Hashtable domainHash = new Hashtable();

    JPanel contentPane;

    XYLayout xYLayout1 = new XYLayout();

    JLabel titleLabel = new JLabel();

    JTextField UserjTextField = new JTextField();

    JLabel userLabel = new JLabel();

    JLabel oldPassLabel = new JLabel();

    JPasswordField OldPass = new JPasswordField();

    JLabel newPassLabel = new JLabel();

    JPasswordField NewPass = new JPasswordField();

    JLabel confirmPassLabel = new JLabel();

    JPasswordField ConfirmPass = new JPasswordField();

    JButton changePassButton = new JButton();

    JTextArea jTextArea1 = new JTextArea();

    Database database1 = new Database();

    JLabel domainLabel = new JLabel();

    JButton ExitButton = new JButton();

    JComboBox domainComboBox;

    JTextField PortjTextField = new JTextField();

    JLabel portLabel = new JLabel();

    boolean quitOnExit = true;

    public Frame1(boolean quit) {
        quitOnExit = quit;
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            this.setModal(true);
            jbInit();
        } catch (Exception e) {
            Debug.processException(e, "Oracle Password Utility");
        }
    }

    public Frame1(boolean quit, String user, String pass) {
        quitOnExit = quit;
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            this.setModal(true);
            jbInit();
            UserjTextField.setText(user);
            OldPass.setText(pass);
        } catch (Exception e) {
            Debug.processException(e, "Oracle Password Utility");
        }
    }

    private void jbInit() throws Exception {
        contentPane = (JPanel) this.getContentPane();
        database1.setUseSpacePadding(true);
        database1.setUseTransactions(false);
        database1.setDatabaseName("");
        domainHash.put("bioinf", "@faber.gnf.org");
        domainHash.put("gidev", "@chaika.gnf.org");
        Object[] obj = domainHash.keySet().toArray();
        java.util.List domainList = Arrays.asList(obj);
        Collections.sort(domainList);
        domainComboBox = new JComboBox(domainList.toArray());
        titleLabel.setFont(new java.awt.Font("SansSerif", 0, 16));
        titleLabel.setText("Oracle Password Utility " + VERSION);
        contentPane.setLayout(xYLayout1);
        this.setSize(new Dimension(364, 589));
        this.setTitle("Oracle Password Utility " + VERSION);
        userLabel.setText("User Name");
        oldPassLabel.setText("Old Password");
        newPassLabel.setText("New Password");
        confirmPassLabel.setText("Confirm Password");
        changePassButton.setText("Change Password");
        changePassButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jButton1_actionPerformed(e);
            }
        });
        domainLabel.setText("Domain");
        ExitButton.setText("Exit");
        ExitButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ExitButton_actionPerformed(e);
            }
        });
        domainComboBox.setToolTipText("");
        PortjTextField.setText("1521");
        portLabel.setText("Port");
        NewPass.addKeyListener(new KeyListener() {

            public void keyReleased(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
            }

            public void keyTyped(KeyEvent e) {
                ConfirmPass.setText("");
            }
        });
        contentPane.add(titleLabel, new XYConstraints(13, 12, 251, 36));
        contentPane.add(jTextArea1, new XYConstraints(1, 501, 361, 63));
        contentPane.add(ConfirmPass, new XYConstraints(35, 382, 200, 30));
        contentPane.add(confirmPassLabel, new XYConstraints(35, 362, -1, -1));
        contentPane.add(NewPass, new XYConstraints(35, 312, 200, 30));
        contentPane.add(newPassLabel, new XYConstraints(35, 287, -1, -1));
        contentPane.add(OldPass, new XYConstraints(35, 245, 200, 30));
        contentPane.add(oldPassLabel, new XYConstraints(35, 210, 99, 23));
        contentPane.add(userLabel, new XYConstraints(35, 145, -1, -1));
        contentPane.add(domainLabel, new XYConstraints(35, 64, -1, -1));
        contentPane.add(changePassButton, new XYConstraints(35, 445, 186, 40));
        contentPane.add(domainComboBox, new XYConstraints(35, 87, 200, 30));
        contentPane.add(UserjTextField, new XYConstraints(35, 163, 200, 30));
        contentPane.add(ExitButton, new XYConstraints(252, 445, 91, 40));
        contentPane.add(PortjTextField, new XYConstraints(280, 87, 44, 30));
        contentPane.add(portLabel, new XYConstraints(290, 64, -1, -1));
    }

    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            database1.closeConnection();
            if (quitOnExit) {
                System.exit(0);
            } else {
                this.dispose();
            }
        }
    }

    void jButton1_actionPerformed(ActionEvent e) {
        boolean debug = false;
        String Domain = domainComboBox.getSelectedItem().toString();
        String Port = PortjTextField.getText();
        String User = UserjTextField.getText();
        String Pass = new String(OldPass.getPassword());
        String New = new String(NewPass.getPassword());
        String Confirm = new String(ConfirmPass.getPassword());
        String sqlcmd = "";
        if (User.length() < 1) {
            jTextArea1.setText("Please Enter a User Name");
            return;
        }
        if (Pass.length() < 1) {
            jTextArea1.setText("Please Enter your password");
            return;
        }
        if (Port.length() < 1) {
            jTextArea1.setText("Port Value invalid");
            PortjTextField.setText("1521");
            return;
        }
        if (New.length() < 6) {
            jTextArea1.setText("New Password must be at least 6 chars long");
            ConfirmPass.setText("");
            NewPass.setText("");
            return;
        }
        if (!New.equals(Confirm)) {
            jTextArea1.setText("Passwords do not match");
            ConfirmPass.setText("");
            NewPass.setText("");
            return;
        }
        if (Login.isBadPassword(User, New)) {
            jTextArea1.setText("Password must contain letters and digits\nand not parts of the username");
            ConfirmPass.setText("");
            NewPass.setText("");
            return;
        }
        try {
            database1.setConnection(new com.borland.dx.sql.dataset.ConnectionDescriptor("jdbc:oracle:thin:" + domainHash.get(Domain).toString() + ":" + Port + ":" + Domain, User, Pass, false, "oracle.jdbc.OracleDriver"));
            sqlcmd = "alter user " + User + " identified by " + New + " replace " + Pass;
            Debug.print(sqlcmd, debug);
            database1.executeStatement(sqlcmd);
            jTextArea1.setText("Update Sucessful!");
        } catch (Exception ex) {
            jTextArea1.setText("Failed!\n" + ex.getMessage());
            show_password_hints();
        }
    }

    void ExitButton_actionPerformed(ActionEvent e) {
        database1.closeConnection();
        if (quitOnExit) {
            System.exit(0);
        } else {
            this.dispose();
        }
    }

    public void show_password_hints() {
        String mesg = "Oracle passwords qualities\n\n1. Case insensitive\n" + "2. At least 6 characters long\n" + "3. Cannot start with a digit\n" + "4. Can only contain letters or numbers\n" + "5. Must contain BOTH letter(s) and numbers(s)\n" + "6. Cannot contain parts of the username\n";
        JOptionPane.showMessageDialog(new JFrame(), mesg);
    }
}
