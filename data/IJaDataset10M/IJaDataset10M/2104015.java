package start.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import start.StrongBoxClient;
import start.gui.jaxb.Config;
import virtualHD.VirtualHDException;

public class LoginPanel extends ImagePanel implements ActionListener {

    private static final String CONFIG_FILE = "client.xml";

    private static final int SERVER_PORT = 1234;

    private static final String LOGIN_CMD = "LOGIN";

    private static final String SUBMIT_CMD = "SUBMIT";

    public LoginPanel(String imagePath, StrongBoxClient hd) {
        super(imagePath, hd);
        if (!(new File(CONFIG_FILE).exists())) initConfig();
        configuration = Config.getFromFile(CONFIG_FILE);
        sbc = new StrongBoxClient(configuration.getServer(), SERVER_PORT, configuration.getService());
        setLayout(new BorderLayout());
        add(paintLoginPane(), BorderLayout.CENTER);
        add(paintServerPanel(), BorderLayout.PAGE_END);
    }

    private JPanel paintServerPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setOpaque(false);
        Font lFont = new Font(Font.SANS_SERIF, Font.BOLD, 15);
        JLabel serverL1 = new JLabel("SERVER: ");
        serverL1.setFont(lFont);
        serverL = new JLabel(sbc.getServerHost());
        panel.add(serverL, FlowLayout.LEFT);
        panel.add(serverL1, FlowLayout.LEFT);
        JLabel serviceL1 = new JLabel("SERVICE: ");
        serviceL1.setFont(lFont);
        serviceL = new JLabel(sbc.getService());
        panel.add(serviceL, FlowLayout.RIGHT);
        panel.add(serviceL1, FlowLayout.RIGHT);
        return panel;
    }

    private void initConfig() {
        cf = new ConfigDialog(this);
        cf.setVisible(true);
    }

    private JPanel paintLoginPane() {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 10, 20);
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setOpaque(false);
        usernameF = new JTextField(20);
        JLabel usernameL = new JLabel("Username");
        usernameL.setFont(ImagePanel.LABELFONT);
        passwordF = new JPasswordField(20);
        JLabel passwordL = new JLabel("Password");
        passwordL.setFont(ImagePanel.LABELFONT);
        c.gridx = 0;
        c.gridy = 0;
        panel.add(usernameL, c);
        c.gridx = 1;
        c.gridy = 0;
        panel.add(usernameF, c);
        c.gridx = 0;
        c.gridy = 1;
        panel.add(passwordL, c);
        c.gridx = 1;
        c.gridy = 1;
        panel.add(passwordF, c);
        rememberLogin = new JCheckBox("remember");
        rememberLogin.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
        if (configuration.getUsername() != null) {
            rememberLogin.setSelected(true);
            passwordF.setText(configuration.getPassword());
            usernameF.setText(configuration.getUsername());
        }
        c.gridy = 2;
        c.gridx = 1;
        panel.add(rememberLogin, c);
        c.gridy = 3;
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        connectButton = new JButton("ACCESS!");
        connectButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        connectButton.setIcon(createIcon("images/cassaforte_maniglia2.png"));
        connectButton.setForeground(new Color(0, 0, 192));
        connectButton.setActionCommand(LOGIN_CMD);
        connectButton.addActionListener(this);
        panel.add(connectButton, c);
        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getActionCommand().equals(LOGIN_CMD)) {
            String username = usernameF.getText();
            String password = new String(passwordF.getPassword());
            if (username.equals("") || password.equals("")) showPopup("Warning", "You're missing username, password or both!", JOptionPane.WARNING_MESSAGE); else {
                try {
                    sbc.openHD(username, password);
                    if (rememberLogin.isSelected()) {
                        configuration.setUsername(username);
                        configuration.setPassword(password);
                        configuration.saveToFile(CONFIG_FILE);
                    } else {
                        configuration.removeUsername();
                        configuration.removePassword();
                        configuration.saveToFile(CONFIG_FILE);
                    }
                    JFrame frame = (JFrame) SwingUtilities.getRoot(this);
                    frame.setContentPane(new BoxPanel("images/treasure3.jpg", sbc));
                    frame.validate();
                } catch (VirtualHDException e) {
                    showPopup("Login error", e.getMessage(), JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (evt.getActionCommand().equals(SUBMIT_CMD)) {
            configuration = new Config();
            configuration.setServer(cf.serverHost.getText());
            configuration.setService(cf.serviceName.getText());
            configuration.saveToFile(CONFIG_FILE);
            cf.setVisible(false);
        }
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private JTextField usernameF;

    private JPasswordField passwordF;

    private ConfigDialog cf;

    private JLabel serviceL;

    private JLabel serverL;

    private JCheckBox rememberLogin;

    private JButton connectButton;

    private Config configuration;
}
