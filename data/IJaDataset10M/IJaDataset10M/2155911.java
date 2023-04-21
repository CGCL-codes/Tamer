package Interface.Dialog;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class Login extends JFrame {

    private static final long serialVersionUID = 1;

    private Interface.GUI gui = null;

    private JButton jButton1;

    private JLabel jLabel1;

    private JLabel jLabel2;

    private JLabel jLabel3;

    private JLabel jLabel4;

    private JPasswordField jPasswordField1;

    private JTextField jTextField1;

    private JTextField jTextField2;

    private JTextField jTextField3;

    public Login(Interface.GUI gui, String host, int port) {
        this.gui = gui;
        initComponents();
        jTextField1.setText(host);
        jTextField2.setText("" + port);
        setSize(305, 215);
        setTitle("SunnyChat::Client::Login");
        setResizable(false);
        setVisible(true);
    }

    private void initComponents() {
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        jTextField1 = new JTextField();
        jTextField2 = new JTextField();
        jTextField3 = new JTextField();
        jPasswordField1 = new JPasswordField();
        jButton1 = new JButton();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        jLabel1.setText(gui.translate("login.port"));
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 45, 80, 17);
        jLabel2.setText(gui.translate("login.host"));
        getContentPane().add(jLabel2);
        jLabel2.setBounds(10, 15, 80, 17);
        jLabel3.setText(gui.translate("login.user"));
        getContentPane().add(jLabel3);
        jLabel3.setBounds(10, 75, 80, 17);
        jLabel4.setText(gui.translate("login.pass"));
        getContentPane().add(jLabel4);
        jLabel4.setBounds(10, 105, 80, 17);
        getContentPane().add(jTextField1);
        jTextField1.setBounds(90, 10, 200, 25);
        getContentPane().add(jTextField2);
        jTextField2.setBounds(90, 40, 200, 25);
        getContentPane().add(jTextField3);
        jTextField3.setBounds(90, 70, 200, 25);
        getContentPane().add(jPasswordField1);
        jPasswordField1.setBounds(90, 100, 200, 25);
        jButton1.setText(gui.translate("login.login"));
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(10, 150, 280, 29);
        pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String host = jTextField1.getText();
            int port = Integer.valueOf(jTextField2.getText()).intValue();
            String user = jTextField3.getText();
            @SuppressWarnings("deprecation") String pass = jPasswordField1.getText();
            gui.setLoginData(host, port, user, pass);
            setVisible(false);
            dispose();
        } catch (Exception e) {
        }
    }
}
