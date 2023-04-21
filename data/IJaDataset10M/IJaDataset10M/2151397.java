package com.das.misc.app;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import com.das.core.app.AppBase;
import com.das.core.app.GlassPanel;
import com.das.user.logic.User;
import com.das.user.logic.UserModel;
import com.das.util.AppConstants;
import com.swtdesigner.SwingResourceManager;

public class Login extends AppBase {

    private JPasswordField passwordField;

    private JTextField usernameTe;

    /**
	 * Create the panel
	 */
    public Login(Container container, Map<String, Object> params) {
        super();
        setName("Login");
        setPreferredSize(new Dimension(1200, 750));
        setParams(params);
        initPanel(this, container, getParams());
        final JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setOpaque(false);
        panel.setBounds(44, 47, 1073, 435);
        add(panel);
        usernameTe = new JTextField();
        usernameTe.setName("user_userid");
        usernameTe.setBounds(419, 189, 155, 20);
        panel.add(usernameTe);
        final JButton loginButton = new JButton();
        loginButton.setIcon(SwingResourceManager.getIcon(Login.class, "/img/Login.png"));
        loginButton.setBounds(419, 260, 85, 30);
        panel.add(loginButton);
        loginButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent arg0) {
                doAuthenticate(panel);
            }
        });
        loginButton.setText("Login");
        final JLabel usernameLabel = new JLabel();
        usernameLabel.setBounds(354, 191, 59, 16);
        panel.add(usernameLabel);
        usernameLabel.setText("Username");
        final JLabel passwordLabel = new JLabel();
        passwordLabel.setBounds(355, 227, 58, 16);
        panel.add(passwordLabel);
        passwordLabel.setText("Password");
        passwordField = new JPasswordField();
        passwordField.setName("user_passwd");
        passwordField.setBounds(419, 225, 155, 20);
        panel.add(passwordField);
    }

    private void doAuthenticate(JPanel panel) {
        User user = new UserModel();
        user = (User) setModel(panel, "user", user);
        try {
            if (user.doCountByUseridPasswd(user) > 0) {
                user = user.doRetrieveByUserid(user.getUserid());
                Map<String, Object> params = getParams();
                params.put(AppConstants.LOGIN_USER, user);
                show(container, Screen.HOME, params);
            } else {
                message(panel, "Invalid Login", "Login is invalid, please try again. Thanks.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
