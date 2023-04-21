package de.mud.login;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import de.mud.login.LoginPlugin;
import java.awt.event.FocusAdapter;

/**
 *  <p>A Panel containing a Login and a Password widget.  This is the Swing
 *  version of the awt LoginPanel</p>
 *
 *  <p> $Id: LoginJPanel.java,v 1.4 2008/03/03 03:00:40 kostaservis Exp $</p>
 *
 *  <p>Revision History</p>
 *
 *  <p>20050904 alison tidied up and imported into to KTA source<br>
 *  20040517 alison use LoginPlugin instead of TLogin<br>
 *  20030825 alison add tab and mousing for wesley<br>
 *  20030725 arranny fixed font and color setting with new UIManager
 *                   method<br>
 *  20030417 arranny fixed statuspanel look<br>
 *  20030310 arranny added setFontSize and setForegroundColor<br>
 *  20030304 arranny added listening for font changes in the
 *                   configuration listener<br>
 *  20021120 arranny set the caret to ThickCaret to make it more
 *                   visible<br>
 *  20021015 arranny added empty border for aesthetics and changed font
 *                   size<br>
 *  20020917 arranny created based on LoginPanel</p>
 */
public class LoginJPanel extends JPanel implements ActionListener {

    private JButton iConnectButton;

    private JLabel iLoginLabel;

    private JLabel iPasswordLabel;

    private JTextField iLoginFld;

    private JPasswordField iPasswdFld;

    private LoginPlugin iPLogin;

    private boolean pass = false;

    static final long serialVersionUID = -5043196341895343378L;

    /**
     *  <p>Construct a new LoginPanel.</p>
     *
     *  @param aPLogin the telnet filter used to capture login messages.
     */
    public LoginJPanel(LoginPlugin aPLogin) {
        super(new BorderLayout());
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        iPLogin = aPLogin;
        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(1, 2, 1, 1);
        iLoginLabel = new JLabel("Login: ", JLabel.RIGHT);
        p.add(iLoginLabel, c);
        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = 3;
        c.gridx = 1;
        c.insets = new Insets(1, 1, 1, 2);
        iLoginFld = new JTextField("", 20);
        iLoginFld.setEditable(false);
        iLoginFld.setEnabled(false);
        iLoginFld.addActionListener(this);
        iLoginFld.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_TAB) actionPerformed(new ActionEvent(iLoginFld, 0, ""));
            }
        });
        iLoginFld.setFocusTraversalKeysEnabled(false);
        p.add(iLoginFld, c);
        c.anchor = GridBagConstraints.EAST;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(1, 2, 1, 1);
        iPasswordLabel = new JLabel("Password: ", JLabel.RIGHT);
        p.add(iPasswordLabel, c);
        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = 3;
        c.gridx = 1;
        c.insets = new Insets(1, 1, 1, 2);
        iPasswdFld = new JPasswordField("", 20);
        iPasswdFld.setEchoChar('*');
        iPasswdFld.setEditable(false);
        iPasswdFld.setEnabled(false);
        iPasswdFld.addActionListener(this);
        iPasswdFld.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_TAB || e.getKeyCode() == KeyEvent.VK_ENTER) actionPerformed(new ActionEvent(iPasswdFld, 0, ""));
            }
        });
        iPasswdFld.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (iLoginFld.isEditable()) actionPerformed(new ActionEvent(iLoginFld, 0, ""));
            }
        });
        iPasswdFld.setFocusTraversalKeysEnabled(false);
        p.add(iPasswdFld, c);
        add(p, BorderLayout.NORTH);
        iConnectButton = new JButton("Connect");
        iConnectButton.addActionListener(this);
        add(iConnectButton, BorderLayout.EAST);
    }

    /**
     *  <p>Activate the login field.</p>
     */
    public void getLogin() {
        iConnectButton.setEnabled(true);
        iLoginFld.setText("");
        iLoginFld.setSelectionStart(0);
        iLoginFld.setSelectionEnd(iLoginFld.getText().length());
        iLoginFld.getCaret().setVisible(true);
        iLoginFld.setEditable(true);
        iLoginFld.setEnabled(true);
        iLoginFld.requestFocusInWindow();
    }

    /**
     *  <p>Activate the password field.</p>
     */
    public void getPassword() {
        iConnectButton.setEnabled(true);
        iPasswdFld.setText("");
        iPasswdFld.getCaret().setVisible(true);
        iPasswdFld.setEditable(true);
        iPasswdFld.setEnabled(true);
        iPasswdFld.requestFocusInWindow();
    }

    /**
     * <p>Sets the editability of the login field.</p>
     * @param isOnline set online status.
     */
    public void setOnline(boolean isOnline) {
        iConnectButton.setText(isOnline ? "Disconnect" : "Connect");
        iConnectButton.setEnabled(true);
        iLoginFld.setText("");
        iLoginFld.setEditable(!isOnline);
        iLoginFld.setEnabled(!isOnline);
        iPasswdFld.setText("");
        iPasswdFld.getCaret().setVisible(false);
        iPasswdFld.setEditable(!isOnline);
        iPasswdFld.setEnabled(!isOnline);
    }

    /**
     *  <p>Implementation of ActionListener.</p>
     *  @param e the ActionEvent that occured.
     */
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        JTextField jtf;
        JButton jbt;
        if (o instanceof JTextField) {
            jtf = (JTextField) o;
            jtf.setSelectionStart(0);
            jtf.setSelectionEnd(0);
            if (jtf.isEditable()) {
                jtf.setEditable(false);
                jtf.setEnabled(false);
                if (jtf == iLoginFld) iPLogin.setLogin(jtf.getText()); else if (jtf == iPasswdFld) {
                    iPLogin.setPasswd(jtf.getText());
                    pass = true;
                    iPLogin.connect();
                }
            }
            jtf.getCaret().setVisible(false);
            jtf.setEditable(false);
            jtf.setEnabled(false);
            return;
        }
        if (o instanceof JButton) {
            jbt = (JButton) o;
            if (jbt != iConnectButton) {
                return;
            } else {
                if (iConnectButton.getText().equals("Disconnect")) iPLogin.disconnect(); else {
                    if (!pass) {
                        iPLogin.setPasswd(iPasswdFld.getText());
                        pass = false;
                    }
                    iPLogin.connect();
                }
            }
        }
    }
}
