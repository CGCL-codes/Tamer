package ui.windows.dialogs;

import java.awt.GridLayout;
import java.awt.LayoutManager;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

public class LoginBox {

    private String userID;

    private String userPassword;

    private JPanel connectionPanel;

    private JTextField userNameField;

    private JPasswordField passwordField;

    private JFrame window;

    public LoginBox(JFrame window) {
        JLabel userNameLabel = new JLabel("Username:  ", JLabel.RIGHT);
        userNameField = new JTextField("");
        JLabel passwordLabel = new JLabel("User Password:  ", JLabel.RIGHT);
        passwordField = new JPasswordField("");
        connectionPanel = new JPanel(false);
        connectionPanel.setLayout((LayoutManager) new BoxLayout(connectionPanel, BoxLayout.X_AXIS));
        this.window = window;
        JPanel namePanel = new JPanel(false);
        namePanel.setLayout(new GridLayout(0, 1));
        namePanel.add(userNameLabel);
        namePanel.add(passwordLabel);
        JPanel fieldPanel = new JPanel(false);
        fieldPanel.setLayout(new GridLayout(0, 1));
        fieldPanel.add(userNameField);
        fieldPanel.add(passwordField);
        connectionPanel.add(namePanel);
        connectionPanel.add(fieldPanel);
    }

    private static final String[] ConnectOptionNames = { "Login", "Clear", "Cancel" };

    private static final String ConnectTitle = "Skyz Quest Login";

    public void display() {
        if (JOptionPane.showOptionDialog(window, connectionPanel, ConnectTitle, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, ConnectOptionNames, ConnectOptionNames[0]) != 0) System.exit(0);
        userID = userNameField.getText();
        userPassword = new String(passwordField.getPassword());
    }

    public String getUserID() {
        return userID;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public static void main(String[] args) {
        new LoginBox(null).display();
    }
}
