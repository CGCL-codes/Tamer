package action.login;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import main.ClientMain;
import tools.MainChannel;
import tools.Message;

public class LoginAction extends MouseAdapter {

    private JTextField name;

    private JPasswordField pwd;

    public LoginAction(JTextField name, JPasswordField pwd) {
        this.name = name;
        this.pwd = pwd;
    }

    public void mouseReleased(MouseEvent e) {
        String userName = name.getText();
        String userPwd = new String(pwd.getPassword());
        ClientMain client = new ClientMain("localhost", 8800);
        client.startClient();
        String loginMsg = "<type>login</type><name>" + userName + "</name><pwd>" + userPwd + "</pwd>";
        Message.sendMessage(loginMsg, MainChannel.getChannel());
    }
}
