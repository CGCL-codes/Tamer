package OpenOfficeNow.gui;

import OpenOfficeNow.OpenOfficeNow;
import OpenOfficeNow.resources.ConfigManager;
import OpenOfficeNow.resources.SHA1DigestGenerator;
import OpenOfficeNow.resources.ReadingUtils;
import com.sun.star.lang.XComponent;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class SettingsWindow extends JFrame implements ActionListener {

    private ConfigManager configManager;

    private JTextField userField;

    private JPasswordField passwordField;

    private JTextField browserField;

    private JCheckBox checkBox;

    private HashMap keys;

    private ResultsWindow parentWindow;

    private String reposPath;

    private OpenOfficeNow extension;

    private XComponent component;

    public SettingsWindow(ResultsWindow parentWin, OpenOfficeNow ext, XComponent comp, ConfigManager confMan, HashMap winKeys, String repPath) {
        this.configManager = confMan;
        this.keys = winKeys;
        this.parentWindow = parentWin;
        this.reposPath = repPath;
        this.extension = ext;
        this.component = comp;
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/link.png"));
        setIconImage(image);
        setTitle("Desk.Now settings");
        setLayout(null);
        JLabel userLabel = new JLabel("User");
        JLabel passwordLabel = new JLabel("Password");
        JLabel browserLabel = new JLabel("Browser path");
        userField = new JTextField(configManager.getUserName());
        String fakePass = "";
        if (!configManager.getPassword().equals("")) {
            fakePass = "_DeskNow_";
        }
        passwordField = new JPasswordField(fakePass);
        browserField = new JTextField(configManager.getBrowser());
        browserField.setToolTipText("You cannot edit this if you are on Windows platform. It's enabled for GNU/Linux users only");
        browserField.setEnabled(false);
        if (System.getProperty("os.name").equals("Linux")) {
            browserField.setEnabled(true);
        }
        checkBox = new JCheckBox("Check for document updates on startup", configManager.getUpdate());
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        add(userLabel);
        add(userField);
        add(passwordLabel);
        add(passwordField);
        add(browserLabel);
        add(browserField);
        add(checkBox);
        add(saveBtn);
        add(cancelBtn);
        Insets insets = getInsets();
        Dimension userLabelSize = userLabel.getPreferredSize();
        userLabel.setBounds(10 + insets.left, 12 + insets.top, userLabelSize.width, userLabelSize.height);
        Dimension userFieldSize = userField.getPreferredSize();
        userField.setBounds(100 + insets.left, 10 + insets.top, 185, userFieldSize.height);
        Dimension passwordLabelSize = passwordLabel.getPreferredSize();
        passwordLabel.setBounds(10 + insets.left, 42 + insets.top, passwordLabelSize.width, passwordLabelSize.height);
        Dimension passwordFieldSize = passwordField.getPreferredSize();
        passwordField.setBounds(100 + insets.left, 40 + insets.top, 185, passwordFieldSize.height);
        Dimension browserLabelSize = browserLabel.getPreferredSize();
        browserLabel.setBounds(10 + insets.left, 72 + insets.top, 90, browserLabelSize.height);
        Dimension browserFieldSize = browserField.getPreferredSize();
        browserField.setBounds(100 + insets.left, 70 + insets.top, 185, browserFieldSize.height);
        Dimension checkBoxSize = checkBox.getPreferredSize();
        checkBox.setBounds(10, 102 + insets.top, checkBoxSize.width, checkBoxSize.height);
        Dimension saveBtnSize = saveBtn.getPreferredSize();
        saveBtn.setBounds(140 + insets.left, 135 + insets.top, saveBtnSize.width, saveBtnSize.height);
        saveBtn.addActionListener(this);
        Dimension cancelBtnSize = cancelBtn.getPreferredSize();
        cancelBtn.setBounds(210 + insets.left, 135 + insets.top, cancelBtnSize.width, cancelBtnSize.height);
        cancelBtn.addActionListener(this);
        getRootPane().setDefaultButton(saveBtn);
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        Action escapeAction = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                extension.setRunning(false);
                dispose();
            }
        };
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", escapeAction);
        setSize(305, 200);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int xPos = ((int) screenSize.getWidth() / 2) - (getWidth() / 2);
        int yPos = ((int) screenSize.getHeight() / 2) - (getHeight() / 2);
        setBounds(xPos, yPos, getWidth(), getHeight());
        setResizable(false);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Save")) {
            String user = userField.getText();
            String password = new String(passwordField.getPassword());
            String browser = browserField.getText();
            String update = "no";
            if (checkBox.isSelected()) {
                update = "yes";
            }
            try {
                if (user.equals("") && (password.equals("") || password.equals("_DeskNow_"))) {
                    configManager.writeConfig("", "", browser, update);
                    passwordField.setText("");
                    dispose();
                } else if (!user.equals("") && !password.equals("")) {
                    if (password.equals("_DeskNow_")) {
                        password = configManager.getPassword();
                        configManager.writeConfig(user, password, browser, update);
                        dispose();
                    } else {
                        password = SHA1DigestGenerator.getSHA1Digest(password);
                        HashMap winResults = ReadingUtils.analizeResult(ReadingUtils.queryWS(keys, user, password, reposPath));
                        configManager.writeConfig(user, password, browser, update);
                        parentWindow.dispose();
                        dispose();
                        ResultsWindow resultsWindow = new ResultsWindow(extension, component, winResults, keys, false, reposPath, configManager);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Wrong credentials! Please, check them before saving settings.", "Login error", JOptionPane.WARNING_MESSAGE);
                userField.setText("");
                passwordField.setText("");
            }
        } else if (e.getActionCommand().equals("Cancel")) {
            dispose();
        }
    }
}
