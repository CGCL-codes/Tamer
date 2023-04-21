package AppFrame;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import AppFrame.CreateDataBaseFrame.CancelMouseAdapter;
import AppFrame.CreateDataBaseFrame.SubmitMouseAdapter;
import StudentMS.JdbcTest;

public class LoginFrame {

    /**
	 * @param args
	 */
    JFrame jFrame = new JFrame("�û���½");

    JButton loginButton = new JButton();

    JButton cancelButton = new JButton();

    JLabel userNameLabel = new JLabel("�û���:");

    JLabel passwordLabel = new JLabel("��    ��:");

    JLabel iamgeLabel = new JLabel();

    JTextField userNameJTextField = new JTextField(50);

    JPasswordField passwordJTextField = new JPasswordField(50);

    public LoginFrame() {
        jFrame.setLayout(null);
        iamgeLabel.setIcon(new ImageIcon(getClass().getResource("/image/land.png")));
        iamgeLabel.setSize(426, 122);
        iamgeLabel.setLocation(0, 0);
        jFrame.add(iamgeLabel);
        userNameLabel.setSize(100, 30);
        userNameLabel.setLocation(120, 144);
        jFrame.add(userNameLabel);
        userNameJTextField.setSize(130, 20);
        userNameJTextField.setLocation(200, 147);
        jFrame.add(userNameJTextField);
        passwordLabel.setSize(100, 30);
        passwordLabel.setLocation(120, 170);
        jFrame.add(passwordLabel);
        passwordJTextField.setSize(130, 20);
        passwordJTextField.setLocation(200, 177);
        passwordJTextField.setEchoChar('*');
        jFrame.add(passwordJTextField);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setIcon(new ImageIcon(getClass().getResource("/image/land_sub.png")));
        loginButton.setSize(80, 30);
        loginButton.setLocation(170, 202);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setMargin(new Insets(0, 0, 0, 0));
        loginButton.addMouseListener(new LoginMouseAdapter());
        jFrame.add(loginButton);
        cancelButton.setSize(80, 30);
        cancelButton.setIcon(new ImageIcon(getClass().getResource("/image/land_res.png")));
        cancelButton.setLocation(260, 202);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.setContentAreaFilled(false);
        cancelButton.setMargin(new Insets(0, 0, 0, 0));
        cancelButton.addMouseListener(new ResetMouseAdapter());
        jFrame.add(cancelButton);
        jFrame.setSize(426, 300);
        jFrame.setLocation(512, 360);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setBackground(Color.BLACK);
        jFrame.setResizable(false);
        jFrame.show();
    }

    public static void main(String[] args) throws Exception {
        LoginFrame jFrame = new LoginFrame();
    }

    class ResetMouseAdapter extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            userNameJTextField.setText("");
            passwordJTextField.setText("");
        }
    }

    class LoginMouseAdapter extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            String userNameString;
            String passwordString;
            String md5String = "";
            MessageDigest md;
            userNameString = userNameJTextField.getText();
            passwordString = passwordJTextField.getText();
            try {
                md = MessageDigest.getInstance("MD5");
                md.update(passwordString.getBytes());
                md5String = new String(md.digest());
                md5String = Base64.encode(md5String.getBytes());
                System.out.println(md5String);
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
                System.out.println("ʧ��");
            }
            if (userNameString.equals("zouwulingde") && md5String.equals("aKdtP09kSTkWCD/cylk=")) {
                JOptionPane.showMessageDialog(null, "��ӭʹ��ѧ���ڹ���ϵͳ��");
            } else {
                JOptionPane.showMessageDialog(null, "�û�������벻��ȷ!!!!");
            }
        }
    }
}
