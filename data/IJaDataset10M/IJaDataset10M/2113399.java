package com.objectwave.uiWidget;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
* @author Dave Hoag
* @date 12/2/1997
*/
public class LoginDialog extends JDialog implements ActionListener {

    String userName;

    String password;

    LoginList loginListenerList = new LoginList();

    JButton pbOk, pbCancel;

    JTextField tfUserName;

    TextField tfPassword;

    class WindowCloser extends java.awt.event.WindowAdapter {

        public void windowClosing(java.awt.event.WindowEvent e) {
            Window source = (Window) e.getSource();
            source.setVisible(false);
            source.dispose();
        }
    }

    class LoginList {

        LoginList next = null;

        LoginListener listener = null;

        public void fireLoginEvent(LoginEvent alert) throws Exception {
            if (next != null) {
                listener.loginRequest(alert);
                next.fireLoginEvent(alert);
            }
        }

        public LoginList addLoginListener(LoginListener list) {
            LoginList result = new LoginList();
            result.next = this;
            result.listener = list;
            return result;
        }

        public LoginList removeLoginListener(LoginListener list) {
            LoginList head = this;
            if (listener == list) {
                return next;
            }
            if (next == null) next = next.removeLoginListener(list);
            return head;
        }
    }

    public LoginDialog(Frame parent, boolean modal) {
        super(parent, "Logon", modal);
        setBackground(Color.lightGray);
        JPanel p = initialize();
        p.setBorder(BorderFactory.createEmptyBorder());
        userName = System.getProperty("user.name");
        if (userName != null) tfUserName.setText(userName);
        getContentPane().add(p);
        addWindowListener(new WindowCloser());
        pbOk.addActionListener(this);
        pbCancel.addActionListener(this);
        setBounds(200, 200, 200, 150);
        KeyListener l = getKeyListener();
        tfUserName.addKeyListener(l);
        tfPassword.addKeyListener(l);
        getRootPane().setDefaultButton(pbOk);
    }

    /**
	*/
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == pbOk) loginRequest(); else cancelRequest();
    }

    public void addLoginListener(LoginListener list) {
        loginListenerList = loginListenerList.addLoginListener(list);
    }

    /**
	*/
    public void addNotify() {
        super.addNotify();
        tfUserName.requestFocus();
    }

    protected void cancelRequest() {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    public void fireLoginEvent(LoginEvent alert) throws Exception {
        loginListenerList.fireLoginEvent(alert);
    }

    public JPanel getButtonPanel() {
        JPanel p = new JPanel();
        pbOk = new JButton("Ok");
        pbOk.setMnemonic('O');
        pbCancel = new JButton("Cancel");
        pbCancel.setMnemonic('C');
        p.setLayout(new BorderLayout());
        p.add(pbOk, BorderLayout.CENTER);
        p.add("East", pbCancel);
        return p;
    }

    /**
	*/
    protected KeyListener getKeyListener() {
        return new KeyListener() {

            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == e.VK_ENTER) loginRequest(); else if (e.getKeyCode() == e.VK_ESCAPE) cancelRequest();
            }

            public void keyTyped(KeyEvent e) {
            }
        };
    }

    public JPanel initialize() {
        JPanel p = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        p.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = gbc.weighty = 1.0;
        JLabel lu = new JLabel("UserName");
        JLabel lp = new JLabel("Password");
        layout.setConstraints(lu, gbc);
        layout.setConstraints(lp, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        tfUserName = new JTextField(10);
        tfPassword = new TextField(10);
        tfPassword.setEchoChar('*');
        layout.setConstraints(tfUserName, gbc);
        layout.setConstraints(tfPassword, gbc);
        JPanel bp = getButtonPanel();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        layout.setConstraints(bp, gbc);
        p.add(lu);
        p.add(tfUserName);
        p.add(lp);
        p.add(tfPassword);
        p.add(bp);
        return p;
    }

    protected void loginRequest() {
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        try {
            LoginEvent evt = new LoginEvent(this);
            evt.setUserName(tfUserName.getText());
            evt.setPassword(tfPassword.getText());
            fireLoginEvent(evt);
        } catch (Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
            return;
        }
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    public static void main(String[] args) {
        Frame f = new Frame("Login");
        f.setBounds(-100, -100, 4, 4);
        f.setVisible(true);
        LoginDialog g = new LoginDialog(f, true);
        g.setVisible(true);
    }

    public void removeLoginListener(LoginListener list) {
        loginListenerList = loginListenerList.removeLoginListener(list);
    }
}
