package jgnash.ui.components;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import jgnash.ui.actions.DatabasePathAction;
import jgnash.ui.util.DialogUtils;
import jgnash.util.Resource;

/**
 * Open database dialog
 *
 * @author Craig Cavanaugh
 * @version $Id: OpenDatabaseDialog.java 3051 2012-01-02 11:27:23Z ccavanaugh $
 */
public class OpenDatabaseDialog extends JDialog implements ActionListener {

    private final Resource rb = Resource.get();

    private JTextField nameField = new JTextFieldEx();

    private JPasswordField passwordField = new JPasswordField();

    private JTextField hostField = new JTextFieldEx();

    private JLabel fileFieldLabel = new JLabel(rb.getString("Label.DatabaseName"));

    private JTextField fileField = new JTextFieldEx();

    private JCheckBox passwordBox = null;

    private JButton fileButton = new JButton("...");

    private JButton okButton;

    private JButton cancelButton;

    private JCheckBox remoteButton = null;

    private JIntegerField portField = new JIntegerField();

    private boolean result = false;

    public OpenDatabaseDialog(JFrame parent) {
        super(parent, true);
        setTitle(rb.getString("Title.Open"));
        layoutMainPanel();
        setMinimumSize(getSize());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        DialogUtils.addBoundsListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == fileButton) {
            String file = DatabasePathAction.databaseNameAction(this, DatabasePathAction.Type.OPEN);
            if (file.length() > 0) {
                fileField.setText(file);
            }
        } else if (e.getSource() == remoteButton) {
            updateForm();
        } else if (e.getSource() == cancelButton) {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        } else if (e.getSource() == okButton) {
            result = true;
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }

    public String getDatabasePath() {
        return fileField.getText();
    }

    public char[] getPassword() {
        return passwordField.getPassword();
    }

    public boolean getResult() {
        return result;
    }

    public String getHost() {
        return hostField.getText();
    }

    public void setHost(String host) {
        hostField.setText(host);
    }

    public void setPort(int port) {
        portField.setIntValue(port);
    }

    public String getUserName() {
        return nameField.getText();
    }

    public boolean isRemote() {
        return remoteButton.isSelected();
    }

    public int getPort() {
        return portField.intValue();
    }

    private void initComponents() {
        passwordBox = new JCheckBox(rb.getString("Button.RememberPassword"));
        remoteButton = new JCheckBox(rb.getString("Button.RemoteServer"));
        cancelButton = new JButton(rb.getString("Button.Cancel"));
        okButton = new JButton(rb.getString("Button.Ok"));
        cancelButton.addActionListener(this);
        okButton.addActionListener(this);
        fileButton.addActionListener(this);
        remoteButton.addActionListener(this);
    }

    private void layoutMainPanel() {
        initComponents();
        FormLayout layout = new FormLayout("p, 4dlu, fill:70dlu:g, 1dlu, d", "");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setDefaultDialogBorder();
        builder.append(fileFieldLabel, fileField, fileButton);
        builder.nextLine();
        builder.appendUnrelatedComponentsGapRow();
        builder.nextLine();
        builder.append(remoteButton, 4);
        builder.append(rb.getString("Label.DatabaseServer"), hostField, 3);
        builder.append(rb.getString("Label.Port"), portField, 3);
        builder.append(rb.getString("Label.UserName"), nameField, 3);
        builder.append(rb.getString("Label.Password"), passwordField, 3);
        builder.append(passwordBox, 4);
        builder.nextLine();
        builder.appendUnrelatedComponentsGapRow();
        builder.nextLine();
        builder.append(ButtonBarFactory.buildOKCancelBar(okButton, cancelButton), 5);
        getContentPane().add(builder.getPanel());
        updateForm();
        nameField.setEnabled(false);
        passwordField.setEnabled(false);
        pack();
    }

    public boolean savePassword() {
        return passwordBox.isSelected();
    }

    public void setDatabasePath(String dataBase) {
        fileField.setText(dataBase);
    }

    public void setUserName(String userName) {
        nameField.setText(userName);
    }

    public void setPassword(String password) {
        passwordField.setText(password);
    }

    public void setRemote(boolean remote) {
        remoteButton.setSelected(remote);
        updateForm();
    }

    private void updateForm() {
        boolean remote = remoteButton.isSelected();
        fileField.setEnabled(!remote);
        fileButton.setEnabled(!remote);
        hostField.setEnabled(remote);
        portField.setEnabled(remote);
        nameField.setEnabled(remote);
        passwordField.setEnabled(remote);
        passwordBox.setEnabled(remote);
    }
}
