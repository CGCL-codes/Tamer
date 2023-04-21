package sk.sigp.aobot.swinggui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import sk.sigp.aobot.Colors;
import sk.sigp.aobot.Credentials;
import sk.sigp.aobot.Servers;
import sk.sigp.aobot.Utils;
import sk.sigp.aobot.base.HaveListeners;
import sk.sigp.aobot.base.LoginListener;

public class LoginFrame extends JDialog {

    private static final long serialVersionUID = 3246809457251169877L;

    private JTextField loginField;

    private JTextField nameField;

    private JTextField pwdField;

    private JComboBox dimensionBox;

    HaveListeners<LoginListener> listener = null;

    private boolean doExit = true;

    ActionListener action = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            doExit = false;
            fireLogonInit();
        }
    };

    public HaveListeners<LoginListener> getListener() {
        if (listener == null) {
            listener = new HaveListeners<LoginListener>();
        }
        return listener;
    }

    public JTextField getLoginField() {
        if (loginField == null) {
            loginField = new JTextField(Credentials.LOGIN);
            loginField.setForeground(Colors.FOREGROUND);
            loginField.setBackground(Colors.BACKGROUND);
            loginField.setCaretColor(Colors.CARET);
            loginField.setPreferredSize(new Dimension(80, 25));
            loginField.addActionListener(action);
        }
        return loginField;
    }

    public JTextField getNameField() {
        if (nameField == null) {
            nameField = new JTextField(Credentials.NICK_NAME);
            nameField.setForeground(Colors.FOREGROUND);
            nameField.setBackground(Colors.BACKGROUND);
            nameField.setCaretColor(Colors.CARET);
            nameField.setPreferredSize(new Dimension(80, 25));
            nameField.addActionListener(action);
        }
        return nameField;
    }

    public JTextField getPwdField() {
        if (pwdField == null) {
            pwdField = new JPasswordField(Credentials.PWD);
            pwdField.setForeground(Colors.FOREGROUND);
            pwdField.setBackground(Colors.BACKGROUND);
            pwdField.setCaretColor(Colors.CARET);
            pwdField.setPreferredSize(new Dimension(80, 25));
            pwdField.addActionListener(action);
        }
        return pwdField;
    }

    public JComboBox getDimensionBox() {
        if (dimensionBox == null) {
            dimensionBox = new JComboBox();
            dimensionBox.setForeground(Colors.FOREGROUND);
            dimensionBox.setBackground(Colors.BACKGROUND);
            dimensionBox.setPreferredSize(new Dimension(120, 25));
            dimensionBox.setEditable(false);
            Servers.validate();
            DefaultComboBoxModel model = new DefaultComboBoxModel(Servers.getServers().toArray());
            dimensionBox.setModel(model);
        }
        return dimensionBox;
    }

    protected void fireLogonInit() {
        getListener().fireEvent("logonInit", getLoginField().getText(), getPwdField().getText(), getNameField().getText(), getDimensionBox().getSelectedItem());
    }

    private void initialize() {
        setResizable(false);
        JPanel corePanel = new JPanel();
        JPanel downPanel = new JPanel();
        downPanel.setMinimumSize(new Dimension(0, 50));
        JButton send = new JButton("Connect");
        corePanel.setLayout(new FlowLayout());
        corePanel.add(new JLabel("Login"));
        corePanel.add(getLoginField());
        corePanel.add(new JLabel("Pwd"));
        corePanel.add(getPwdField());
        corePanel.add(new JLabel("Charname"));
        corePanel.add(getNameField());
        corePanel.add(new JLabel("Dimension"));
        corePanel.add(getDimensionBox());
        downPanel.add(send);
        send.addActionListener(action);
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        setContentPane(contentPanel);
        setSize(400, 120);
        setLocation((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - getWidth() / 2, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - getHeight() / 2);
        getContentPane().add(corePanel, BorderLayout.CENTER);
        getContentPane().add(downPanel, BorderLayout.SOUTH);
        setTitle("Brave Oest's Relay Chat v" + Utils.VERSION + " - Connect");
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                if (doExit) System.exit(0);
            }
        });
    }

    public LoginFrame() {
        initialize();
    }
}
