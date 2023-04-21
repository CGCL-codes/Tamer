package sk.fiit.mitandao.modules.inputs.dbreader.firststep;

import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;

/**
 * This class is a graphical representation of the first step in the DB Reader module.
 * 
 * @author Tomas Jelinek (Refactored from the code by Tomas Konecny)
 *
 */
public class FirstStepPanel extends JPanel {

    private static final long serialVersionUID = -3707290693598313353L;

    private JTextField hostField = null;

    private JTextField portField = null;

    private JTextField userField = null;

    private JPasswordField passwordField = null;

    private JTextField dbNameField = null;

    private JTextField schemaField = null;

    private JLabel hostLB = null;

    private JLabel portLB = null;

    private JLabel userLB = null;

    private JLabel passwordLB = null;

    private JLabel dbNameLB = null;

    private JLabel schemaLB = null;

    private JButton firstStepBT = null;

    private Dimension preferredSize = new Dimension(300, 20);

    private FirstStepController firstStepController = null;

    public FirstStepPanel(FirstStepController firstStepController) {
        this.firstStepController = firstStepController;
        initialize();
        firstStepController.setPanel(this);
    }

    private void initialize() {
        removeAll();
        setLayout(new MigLayout("wrap 1"));
        add(getHostLB());
        add(getHostField());
        add(getPortLB());
        add(getPortField());
        add(getUserLB());
        add(getUserField());
        add(getPasswordLB());
        add(getPasswordField());
        add(getDbNameLB());
        add(getDBNameField());
        add(getSchemaLB());
        add(getSchemaField());
        add(getFirstStepBT());
    }

    public JTextField getHostField() {
        if (hostField == null) {
            hostField = new JTextField();
            hostField.setPreferredSize(preferredSize);
        }
        return hostField;
    }

    public JTextField getPortField() {
        if (portField == null) {
            portField = new JTextField();
            portField.setPreferredSize(preferredSize);
        }
        return portField;
    }

    public JTextField getUserField() {
        if (userField == null) {
            userField = new JTextField();
            userField.setPreferredSize(preferredSize);
        }
        return userField;
    }

    public JPasswordField getPasswordField() {
        if (passwordField == null) {
            passwordField = new JPasswordField();
            passwordField.setPreferredSize(preferredSize);
        }
        return passwordField;
    }

    public JTextField getDBNameField() {
        if (dbNameField == null) {
            dbNameField = new JTextField();
            dbNameField.setPreferredSize(preferredSize);
        }
        return dbNameField;
    }

    public JTextField getSchemaField() {
        if (schemaField == null) {
            schemaField = new JTextField();
            schemaField.setPreferredSize(preferredSize);
        }
        return schemaField;
    }

    private JLabel getHostLB() {
        if (hostLB == null) {
            hostLB = new JLabel("DB Host:");
        }
        return hostLB;
    }

    private JLabel getPortLB() {
        if (portLB == null) {
            portLB = new JLabel("DB Port: ");
        }
        return portLB;
    }

    private JLabel getUserLB() {
        if (userLB == null) {
            userLB = new JLabel("DB User: ");
        }
        return userLB;
    }

    private JLabel getPasswordLB() {
        if (passwordLB == null) {
            passwordLB = new JLabel("DB Password: ");
        }
        return passwordLB;
    }

    private JLabel getDbNameLB() {
        if (dbNameLB == null) {
            dbNameLB = new JLabel("DB Name: ");
        }
        return dbNameLB;
    }

    private JLabel getSchemaLB() {
        if (schemaLB == null) {
            schemaLB = new JLabel("DB Schema: ");
        }
        return schemaLB;
    }

    public JButton getFirstStepBT() {
        if (firstStepBT == null) {
            firstStepBT = new JButton("1st STEP");
            firstStepBT.addActionListener(firstStepController);
        }
        return firstStepBT;
    }
}
