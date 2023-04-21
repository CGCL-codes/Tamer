package hermes.main;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class UIAddRecord extends JFrame {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private JPanel contentPane;

    private JTextField txtFirstName;

    private JTextField txtEmail;

    private JTextField txtLastName;

    public String[] AddedRecord;

    /**
	 * Create the frame.
	 */
    public UIAddRecord() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 640, 183);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        JLabel lblFirstName = new JLabel("First Name");
        lblFirstName.setBounds(12, 12, 99, 15);
        contentPane.add(lblFirstName);
        JLabel lblLastName = new JLabel("Last Name");
        lblLastName.setBounds(290, 12, 99, 15);
        contentPane.add(lblLastName);
        JLabel lblEmail = new JLabel("Email");
        lblEmail.setBounds(12, 43, 99, 15);
        contentPane.add(lblEmail);
        txtFirstName = new JTextField();
        txtFirstName.setBounds(103, 10, 169, 19);
        contentPane.add(txtFirstName);
        txtFirstName.setColumns(10);
        txtEmail = new JTextField();
        txtEmail.setBounds(103, 41, 169, 19);
        contentPane.add(txtEmail);
        txtEmail.setColumns(10);
        txtLastName = new JTextField();
        txtLastName.setColumns(10);
        txtLastName.setBounds(379, 10, 233, 19);
        contentPane.add(txtLastName);
        JButton btnApply = new JButton("Apply");
        btnApply.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                AddNewRecord();
            }
        });
        btnApply.setBounds(388, 116, 75, 25);
        contentPane.add(btnApply);
        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            }
        });
        btnClose.setBounds(551, 116, 75, 25);
        contentPane.add(btnClose);
        JButton btnOK = new JButton("OK");
        btnOK.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                AddNewRecord();
                setVisible(false);
                dispose();
            }
        });
        btnOK.setBounds(475, 116, 75, 25);
        contentPane.add(btnOK);
    }

    private void AddNewRecord() {
        String[] contactInfo = new String[3];
        contactInfo[0] = txtFirstName.getText();
        contactInfo[1] = txtLastName.getText();
        contactInfo[2] = txtEmail.getText();
        AddedRecord = contactInfo;
    }
}
