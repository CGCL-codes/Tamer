package netsfmsnmessengernb.gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import net.sf.jml.MsnMessenger;
import net.sf.jml.impl.MsnMessengerFactory;
import netsfmsnmessengernb.event.JMSNAdapter;
import netsfmsnmessengernb.global.JMSN;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author paulo
 */
public class BackgroundJPanelLogin extends JPanel implements ActionListener {

    private static final Log log = LogFactory.getLog(BackgroundJPanelLogin.class);

    private Image fundo = new ImageIcon(getClass().getResource("/netsfmsnmessengernb/resources/jmsnPrototipo.png")).getImage();

    private JButton btnLogin;

    private JLabel lblUser, lblPass;

    private JTextField txtUser;

    private JPasswordField txtPass;

    public BackgroundJPanelLogin() {
        lblUser = new JLabel();
        lblUser.setText("Usuário:");
        lblUser.setBounds(75, 210, 80, 14);
        txtUser = new JTextField(40);
        txtUser.setBounds(75, 229, 200, 22);
        lblPass = new JLabel("Senha:");
        lblPass.setBounds(75, 255, 80, 14);
        txtPass = new JPasswordField(40);
        txtPass.setBounds(75, 274, 200, 22);
        btnLogin = new JButton("Login");
        btnLogin.setBounds(202, 310, btnLogin.getPreferredSize().width, btnLogin.getPreferredSize().height);
        btnLogin.addActionListener(this);
        add(lblUser);
        add(txtUser);
        add(lblPass);
        add(txtPass);
        add(btnLogin);
        txtUser.setText("msnmessengernb@hotmail.com");
        txtPass.setText("123456");
    }

    public BackgroundJPanelLogin(BorderLayout borderLayout) {
        super(borderLayout);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fundo, 0, 0, this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            if (txtUser.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Usuário não pode ser vazio", "Erro Login", JOptionPane.ERROR_MESSAGE);
            } else if (new String(txtPass.getPassword()).equals("")) {
                JOptionPane.showMessageDialog(this, "Senha não pode ser vazia", "Erro Login", JOptionPane.ERROR_MESSAGE);
            } else {
                MsnMessenger msnMessengerTemp = MsnMessengerFactory.createMsnMessenger(txtUser.getText(), new String(txtPass.getPassword()));
                msnMessengerTemp.setLogIncoming(true);
                msnMessengerTemp.setLogOutgoing(true);
                initMessenger(msnMessengerTemp);
                JMSN.setMsnMessenger(msnMessengerTemp);
                JMSN.getMsnMessenger().login();
            }
        }
    }

    protected void initMessenger(MsnMessenger messenger) {
        JMSNAdapter jMSNAdapter = new JMSNAdapter();
        messenger.addListener(jMSNAdapter);
    }
}
