package crm.client.ui;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import crm.server.data.auth;

/**
 * @author Maxim Tolstyh
 *
 */
public class TMGate extends JFrame {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3878368782108555281L;

    private JButton b_login;

    public JButton getB_login() {
        return b_login;
    }

    public void setB_login(JButton b_login) {
        this.b_login = b_login;
    }

    private JButton b_cancel;

    private JCheckBox ch_box_local;

    private JPasswordField t_pass;

    private JTextField t_uname;

    private boolean valid = false;

    private auth authData;

    public TMGate() {
        init();
    }

    public void init() {
        setVisible(false);
        setTitle("TM");
        Container gate_content = getContentPane();
        GridBagConstraints gate_gridconstr = new GridBagConstraints();
        setLayout(new GridBagLayout());
        gate_gridconstr.fill = GridBagConstraints.HORIZONTAL;
        gate_gridconstr.gridx = 0;
        gate_gridconstr.gridy = 0;
        setSize(150, 150);
        setLocation(300, 300);
        gate_content.add(new JLabel("username:"), gate_gridconstr);
        gate_gridconstr.fill = GridBagConstraints.HORIZONTAL;
        gate_gridconstr.gridx = 1;
        gate_gridconstr.gridy = 0;
        t_uname = new JTextField(25);
        gate_content.add(t_uname, gate_gridconstr);
        gate_gridconstr.fill = GridBagConstraints.VERTICAL;
        gate_gridconstr.gridx = 0;
        gate_gridconstr.gridy = 1;
        gate_content.add(new JLabel("password:"), gate_gridconstr);
        t_pass = new JPasswordField(25);
        gate_gridconstr.fill = GridBagConstraints.HORIZONTAL;
        gate_gridconstr.gridx = 1;
        gate_gridconstr.gridy = 1;
        gate_content.add(t_pass, gate_gridconstr);
        b_login = new JButton("enter");
        ch_box_local = new JCheckBox("local");
        ch_box_local.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                Object source = e.getItemSelectable();
                if (source == ch_box_local) {
                    if (ch_box_local.isSelected()) {
                        t_pass.setEnabled(false);
                        t_uname.setEnabled(false);
                    } else {
                        t_pass.setEnabled(true);
                        t_uname.setEnabled(true);
                    }
                }
            }
        });
        gate_gridconstr.fill = GridBagConstraints.VERTICAL;
        gate_gridconstr.gridx = 0;
        gate_gridconstr.gridy = 2;
        gate_content.add(ch_box_local, gate_gridconstr);
        gate_gridconstr.fill = GridBagConstraints.VERTICAL;
        gate_gridconstr.gridx = 0;
        gate_gridconstr.gridy = 3;
        gate_content.add(b_login, gate_gridconstr);
        b_cancel = new JButton("quit");
        b_cancel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }
        });
        gate_gridconstr.fill = GridBagConstraints.HORIZONTAL;
        gate_gridconstr.gridx = 1;
        gate_gridconstr.gridy = 3;
        gate_content.add(b_cancel, gate_gridconstr);
        setVisible(true);
        setResizable(false);
    }

    /**
	 * @param valid the valid to set
	 */
    public void setValidFrame(boolean valid) {
        this.valid = valid;
    }

    /**
	 * @return the valid
	 */
    public boolean isValidFrame() {
        return valid;
    }

    public void setAuthData(auth authData) {
        this.authData = authData;
    }

    public auth getAuthData() {
        return authData;
    }

    public void createAuthData() {
        authData = new auth(t_uname.getText(), t_pass.getPassword().toString(), ch_box_local.isSelected());
    }
}
