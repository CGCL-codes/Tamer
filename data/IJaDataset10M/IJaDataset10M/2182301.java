package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import common.MD5;
import common.NetworkMsg;

/**
* this is the first windows you'll see when you start the client
* @author fken
*/
public class FirstPanel extends JFrame implements ActionListener, WindowListener {

    /** the client which launched the firstpanel */
    private Client client;

    /** the ip of the distant server */
    private String ip = null;

    /** the port of the distant server */
    private int port;

    /** check if the user has started a thread or not */
    private boolean threadLaunched = false;

    /** the main panel where the components are displayed */
    private JPanel panel;

    /** the picture of the first window */
    private ImageIcon image;

    /** the place where it's written if you are up to date or not */
    private JLabel updateLabel;

    /** release number of the game you are launching */
    private JLabel releaseLabel;

    /** way to write 'server' on the windows */
    private JLabel serverLabel;

    /** place where you have to write the server ip address */
    private JTextField serverField;

    /** way to write 'port' on the windows */
    private JLabel portLabel;

    /** place where you have to write the server port number */
    private JTextField portField;

    /** way to write 'account' on the windows */
    private JLabel accountLabel;

    /** place where you have to write your account name */
    private JTextField accountField;

    /** way to write 'password' on the windows */
    private JLabel passwordLabel;

    /** place where you have to write your password */
    private JTextField passwordField;

    /** way to write 'password (again)' on the windows */
    private JLabel password2Label;

    /** place where you have to write your password once again */
    private JTextField password2Field;

    /** way to write 'real name' on the windows */
    private JLabel realNameLabel;

    /** place where you have to put your full name */
    private JTextField realNameField;

    /** way to write 'email' on the windows */
    private JLabel emailLabel;

    /** place where you have to put your email */
    private JTextField emailField;

    /** way to write 'timezone' on the windows */
    private JLabel timeZoneLabel;

    /** place where you have to put your time zone*/
    private JTextField timeZoneField;

    /** the button you have to push if you want to login */
    private JButton loginButton;

    /** the button you have to push if you want to register */
    private JButton registerButton;

    /**
* creates the first window which allow the client to be connected or the player to be registered
* @author fken
* @param release the game release
*/
    public FirstPanel(String release, Client client) {
        super("Welcome to Fearann Muin's Realm");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.client = client;
        this.updateLabel = new JLabel("Welcome to Fearann Muin Fantasy World");
        this.releaseLabel = new JLabel("v" + release);
        this.loginButton = new JButton("login");
        this.loginButton.addActionListener(this);
        this.registerButton = new JButton("register");
        this.registerButton.addActionListener(this);
        addWindowListener(this);
        createConnectionWindow();
        pack();
        setResizable(false);
        setVisible(true);
    }

    /**
* creates the connection screen
* @author fken
*/
    private void createConnectionWindow() {
        this.image = new ImageIcon("data/img/firstPanelConnection.png");
        this.serverLabel = new JLabel("server : ");
        this.portLabel = new JLabel("port : ");
        this.serverField = new JTextField("localhost", 24);
        this.portField = new JTextField("5555", 5);
        this.panel = (JPanel) this.getContentPane();
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);
        GridBagConstraints constraints;
        constraints = new GridBagConstraints(0, 0, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(new JLabel(image), constraints);
        constraints = new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(updateLabel, constraints);
        constraints = new GridBagConstraints(2, 1, 1, 1, 1.0, 1.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(releaseLabel, constraints);
        constraints = new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(serverLabel, constraints);
        constraints = new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(serverField, constraints);
        constraints = new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(portLabel, constraints);
        constraints = new GridBagConstraints(1, 3, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(portField, constraints);
        constraints = new GridBagConstraints(1, 4, 1, 1, 1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(loginButton, constraints);
        constraints = new GridBagConstraints(2, 4, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(registerButton, constraints);
    }

    /**
* creates the login screen
* @author fken
*/
    private void createLoginWindow() {
        this.image = new ImageIcon("data/img/firstPanelLogin.png");
        this.accountLabel = new JLabel("account : ");
        this.accountField = new JTextField("account name", 16);
        this.passwordLabel = new JLabel("password : ");
        this.passwordField = new JTextField("", 16);
        this.panel = (JPanel) this.getContentPane();
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);
        GridBagConstraints constraints;
        constraints = new GridBagConstraints(0, 0, 5, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(new JLabel(image), constraints);
        constraints = new GridBagConstraints(0, 1, 3, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(updateLabel, constraints);
        constraints = new GridBagConstraints(3, 1, 2, 1, 1.0, 1.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(releaseLabel, constraints);
        constraints = new GridBagConstraints(0, 3, 2, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(accountLabel, constraints);
        constraints = new GridBagConstraints(2, 3, 1, 1, 1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(accountField, constraints);
        constraints = new GridBagConstraints(0, 4, 2, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(passwordLabel, constraints);
        constraints = new GridBagConstraints(2, 4, 1, 1, 1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(passwordField, constraints);
        constraints = new GridBagConstraints(3, 3, 2, 2, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(loginButton, constraints);
    }

    /**
* creates the registration window by modifying the first panel
* @author fken
*/
    private void createRegistrationWindow() {
        this.image = new ImageIcon("data/img/firstPanelRegistration.png");
        this.accountLabel = new JLabel("account : ");
        this.accountField = new JTextField("account name", 16);
        this.passwordLabel = new JLabel("password : ");
        this.passwordField = new JTextField("", 16);
        this.password2Label = new JLabel("password (again) : ");
        this.password2Field = new JTextField("", 16);
        this.realNameLabel = new JLabel("full Name : ");
        this.realNameField = new JTextField("", 20);
        this.emailLabel = new JLabel("email : ");
        this.emailField = new JTextField("", 16);
        this.timeZoneLabel = new JLabel("timezone : ");
        this.timeZoneField = new JTextField("", 5);
        this.panel = (JPanel) this.getContentPane();
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);
        GridBagConstraints constraints;
        constraints = new GridBagConstraints(0, 0, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(new JLabel(image), constraints);
        constraints = new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(updateLabel, constraints);
        constraints = new GridBagConstraints(2, 1, 1, 1, 1.0, 1.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(releaseLabel, constraints);
        constraints = new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(accountLabel, constraints);
        constraints = new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(accountField, constraints);
        constraints = new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(realNameLabel, constraints);
        constraints = new GridBagConstraints(1, 3, 1, 1, 1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(realNameField, constraints);
        constraints = new GridBagConstraints(0, 4, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(passwordLabel, constraints);
        constraints = new GridBagConstraints(1, 4, 1, 1, 1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(passwordField, constraints);
        constraints = new GridBagConstraints(0, 5, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(password2Label, constraints);
        constraints = new GridBagConstraints(1, 5, 1, 1, 1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(password2Field, constraints);
        constraints = new GridBagConstraints(0, 6, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(emailLabel, constraints);
        constraints = new GridBagConstraints(1, 6, 1, 1, 1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(emailField, constraints);
        constraints = new GridBagConstraints(0, 7, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(timeZoneLabel, constraints);
        constraints = new GridBagConstraints(1, 7, 1, 1, 1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(timeZoneField, constraints);
        constraints = new GridBagConstraints(2, 3, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(registerButton, constraints);
    }

    /**
* set the thread status
* @author fken
* @param status the status of the thread : true = launched
*/
    public void setThreadStatus(boolean status) {
        this.threadLaunched = status;
    }

    /**
* actions to perform when the player push the send button
* @author fken
* @param event event created by the player
*/
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == this.loginButton) {
            if (this.ip == null) {
                this.ip = serverField.getText();
                this.port = Integer.parseInt(portField.getText());
                this.panel.removeAll();
                createLoginWindow();
                this.pack();
            } else {
                String userAccount = accountField.getText();
                String password = passwordField.getText();
                if (!threadLaunched) {
                    client.network = new NetworkConnection(this.ip, this.port, this.client);
                    client.network.connect();
                    client.network.start();
                }
                client.network.connectUser(userAccount, MD5.digest(password));
            }
        }
        if (event.getSource() == this.registerButton) {
            if (this.ip == null) {
                this.ip = serverField.getText();
                this.port = Integer.parseInt(portField.getText());
                this.panel.removeAll();
                createRegistrationWindow();
                this.pack();
            } else {
                String userAccount = accountField.getText();
                String password1 = passwordField.getText();
                String password2 = password2Field.getText();
                String realName = realNameField.getText();
                String email = emailField.getText();
                short timeZone = new Short(timeZoneField.getText());
                if (password1.equals(password2)) {
                    if (!threadLaunched) {
                        client.network = new NetworkConnection(this.ip, this.port, this.client);
                        client.network.connect();
                        client.network.start();
                    }
                    client.network.register(userAccount, MD5.digest(password1), realName, email, timeZone);
                    this.panel.removeAll();
                    createLoginWindow();
                    this.pack();
                }
            }
        }
    }

    public void close() {
        dispose();
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        if (threadLaunched) {
            client.network.send(NetworkMsg.close, null);
        }
        System.out.println("Closing");
        dispose();
    }

    public void windowClosed(WindowEvent e) {
        System.out.println("Closed");
    }
}
