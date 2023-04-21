package Presentation.GraphicUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyLogOnScreen extends MovieSystemPanel {

    /**
	 * this is the login screen
	 */
    private static final long serialVersionUID = 1L;

    private static JTextField username = null;

    private static JPasswordField password = null;

    public MyLogOnScreen(MainScreen mainScreen) {
        super(mainScreen);
        username = new JTextField(15);
        password = new JPasswordField(15);
        JLabel usernameLabel = new JLabel("Username: ");
        JLabel passwordLabel = new JLabel("Password: ");
        JPanel j = new JPanel();
        j.add(usernameLabel);
        j.add(username);
        add(j);
        j = new JPanel();
        j.add(passwordLabel);
        j.add(password);
        add(j);
        j = new JPanel();
        setVisible(true);
        JButton button1 = new JButton("Ok,lets Login!");
        j.add(button1);
        add(j);
        button1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    m_controller.login(username.getText(), new String(password.getPassword()));
                    m_mainScreen.setActivePanel(MainScreen.MAIN_MENU);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(m_mainScreen, ex.getMessage());
                }
            }
        });
    }

    public void setUp() {
        username.setText("");
        password.setText("");
    }
}
