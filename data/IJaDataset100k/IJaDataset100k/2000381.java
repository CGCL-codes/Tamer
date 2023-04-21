package gui.common;

import gui.GuiCommon;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginDialogue extends JDialog {

    private static final long serialVersionUID = 1L;

    private int width = 200;

    ;

    private int height = 120;

    JTextField userText = new JTextField();

    JTextField pwdText = new JPasswordField();

    JLabel user = new JLabel("User Name:");

    JLabel password = new JLabel("Password:");

    JButton login = new JButton("Login");

    JButton cancel = new JButton("Cancel");

    BaseClientFrame client = null;

    public LoginDialogue(BaseClientFrame bc) {
        this.client = bc;
        this.setTitle("Login");
        initDialogue();
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    public void close() {
        dispose();
    }

    private void initDialogue() {
        login.addActionListener(new LoginDialogueListener(this, client));
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(3, 2));
        contentPane.add(user);
        contentPane.add(userText);
        contentPane.add(password);
        contentPane.add(pwdText);
        contentPane.add(login);
        contentPane.add(cancel);
        GuiCommon.setWindowSize(this, width, height);
    }

    public String getUserName() {
        return userText.getText();
    }

    public String getPassword() {
        return pwdText.getText();
    }
}
