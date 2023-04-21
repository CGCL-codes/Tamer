package gui.client;

import gui.GuiCommon;
import gui.client.listener.ClientMenuEventListener;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class LogOut extends JDialog {

    private static final long serialVersionUID = 1L;

    private int width = 300;

    ;

    private int height = 200;

    Frame parent = null;

    JTextField userText = new JTextField();

    JTextField pwdText = new JTextField();

    public LogOut(Frame frame) {
        parent = frame;
        this.setTitle("Logout");
        initlogout();
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void initlogout() {
        GuiCommon.setWindowSize(this, width, height);
        JLabel user = new JLabel("Are you want logout?(you will auto join the current group next login)");
        JButton login = new JButton("OK");
        login.addActionListener(new ClientMenuEventListener());
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(3, 1));
        contentPane.add(user);
        contentPane.add(login);
        contentPane.add(cancel);
    }
}
