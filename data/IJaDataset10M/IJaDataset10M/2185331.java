package dialog;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Format;
import java.text.ParseException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.text.MaskFormatter;

public class RegisterUI extends JFrame {

    private Client client;

    private JButton registerButton, cancelButton;

    private JLabel nameLabel, lastnameLabel, emailLabel, townLabel, genderLabel, bornLabel;

    private JFormattedTextField nameTextField, lastnameTextField, emailTextField, townTextField;

    private JComboBox genderComboBox;

    private JSpinner bornSpinner;

    private JPanel registerPanel, cancelPanel;

    private MaskFormatter emailFormater = null, chars15Formater = null;

    private final String genderTypes[] = { " --- ", "Female", "Male" };

    private final DefaultComboBoxModel genderDataModel;

    public RegisterUI(Client clientIn) {
        super("Register new user");
        client = clientIn;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 2, 10, 5));
        nameLabel = new JLabel("Name:");
        lastnameLabel = new JLabel("Lastname:");
        emailLabel = new JLabel("E-mail:");
        townLabel = new JLabel("Town:");
        genderLabel = new JLabel("Gender:");
        bornLabel = new JLabel("Year of born:");
        try {
            chars15Formater = new MaskFormatter("???????????????");
            emailFormater = new MaskFormatter("***************");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        nameTextField = new JFormattedTextField(chars15Formater);
        nameTextField.setColumns(10);
        lastnameTextField = new JFormattedTextField(chars15Formater);
        emailTextField = new JFormattedTextField(emailFormater);
        townTextField = new JFormattedTextField(chars15Formater);
        genderDataModel = new DefaultComboBoxModel(genderTypes);
        genderComboBox = new JComboBox(genderTypes);
        bornSpinner = new JSpinner();
        registerPanel = new JPanel();
        registerButton = new JButton("Register");
        registerPanel.add(registerButton);
        cancelPanel = new JPanel();
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new CancelActionListener(this));
        cancelPanel.add(cancelButton);
        add(nameLabel);
        add(nameTextField);
        add(lastnameLabel);
        add(lastnameTextField);
        add(emailLabel);
        add(emailTextField);
        add(townLabel);
        add(townTextField);
        add(genderLabel);
        add(genderComboBox);
        add(bornLabel);
        add(bornSpinner);
        add(registerPanel);
        add(cancelPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private class RegisterActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
        }
    }

    private class CancelActionListener implements ActionListener {

        private RegisterUI register;

        public CancelActionListener(RegisterUI registerIn) {
            register = registerIn;
        }

        public void actionPerformed(ActionEvent e) {
            register.dispose();
            System.gc();
        }
    }
}
