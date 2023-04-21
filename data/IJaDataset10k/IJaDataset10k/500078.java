package Client;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;

@SuppressWarnings("serial")
public class Home extends JFrame {

    private JPanel contentPane;

    /**
	 * Launch the application.
	 */
    static Home frame;

    private JButton btnNewButton;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    frame = new Home();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Home() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 558, 431);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        btnNewButton = new JButton("Sign In");
        btnNewButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                Sign_in si = new Sign_in();
                si.setVisible(true);
                frame.setVisible(false);
            }
        });
        btnNewButton.setBounds(291, 264, 139, 51);
        contentPane.add(btnNewButton);
        JButton btnNewButton_1 = new JButton("Sign Up");
        btnNewButton_1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Sign_up su = new Sign_up();
                su.setVisible(true);
                frame.setVisible(false);
            }
        });
        btnNewButton_1.setBounds(76, 264, 139, 51);
        contentPane.add(btnNewButton_1);
        JLabel lblNewLabel = new JLabel("Want to Join !!");
        lblNewLabel.setBounds(89, 230, 110, 23);
        contentPane.add(lblNewLabel);
        JLabel label = new JLabel("Already A User ?");
        label.setBounds(302, 230, 110, 23);
        contentPane.add(label);
        JLabel lblAmigos = new JLabel("AMIGOS");
        lblAmigos.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblAmigos.setBounds(223, 63, 110, 23);
        contentPane.add(lblAmigos);
    }

    public JButton getBtnNewButton() {
        return btnNewButton;
    }
}
