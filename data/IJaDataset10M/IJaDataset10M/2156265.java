package chsec.gui.app;

import javax.swing.JPanel;
import java.awt.Frame;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import java.awt.Insets;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import org.springframework.stereotype.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@Component
public class LoginDlgImp extends JDialog implements LoginDlg {

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    private JPanel centerPan = null;

    private JPanel buttPan = null;

    private JButton okBT = null;

    private JLabel unameL = null;

    private JTextField unameTF = null;

    private JLabel upwdL = null;

    private JPasswordField upwdTF = null;

    private LoginDlgCtrl control;

    public String getUserName() {
        return getUnameTF().getText();
    }

    public String getUserPassword() {
        return new String(getUpwdTF().getPassword());
    }

    public void run() {
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Login Error", JOptionPane.ERROR_MESSAGE);
    }

    public void stop() {
        dispose();
    }

    public void setControl(LoginDlgCtrl control) {
        this.control = control;
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent ev) {
                LoginDlgImp.this.control.cancelNotify();
            }
        });
        getOkBT().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                LoginDlgImp.this.control.loginNotify();
            }
        });
    }

    /**
	 * @param owner
	 */
    public LoginDlgImp() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(274, 200);
        this.setModal(true);
        this.setTitle("Login");
        this.setContentPane(getJContentPane());
        this.getRootPane().setDefaultButton(getOkBT());
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getCenterPan(), BorderLayout.CENTER);
            jContentPane.add(getButtPan(), BorderLayout.SOUTH);
        }
        return jContentPane;
    }

    /**
	 * This method initializes centerPan	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getCenterPan() {
        if (centerPan == null) {
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints3.gridy = 1;
            gridBagConstraints3.weightx = 1.0;
            gridBagConstraints3.anchor = GridBagConstraints.WEST;
            gridBagConstraints3.insets = new Insets(5, 0, 5, 5);
            gridBagConstraints3.gridx = 1;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.anchor = GridBagConstraints.EAST;
            gridBagConstraints2.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints2.gridy = 1;
            upwdL = new JLabel();
            upwdL.setText("Password");
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.weightx = 1.0;
            gridBagConstraints1.anchor = GridBagConstraints.WEST;
            gridBagConstraints1.insets = new Insets(5, 0, 5, 5);
            gridBagConstraints1.gridx = 1;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.anchor = GridBagConstraints.EAST;
            gridBagConstraints.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints.gridy = 0;
            unameL = new JLabel();
            unameL.setText("User Name");
            centerPan = new JPanel();
            centerPan.setLayout(new GridBagLayout());
            centerPan.add(unameL, gridBagConstraints);
            centerPan.add(getUnameTF(), gridBagConstraints1);
            centerPan.add(upwdL, gridBagConstraints2);
            centerPan.add(getUpwdTF(), gridBagConstraints3);
        }
        return centerPan;
    }

    /**
	 * This method initializes buttPan	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getButtPan() {
        if (buttPan == null) {
            buttPan = new JPanel();
            buttPan.setLayout(new FlowLayout());
            buttPan.add(getOkBT(), null);
        }
        return buttPan;
    }

    /**
	 * This method initializes okBT	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getOkBT() {
        if (okBT == null) {
            okBT = new JButton();
            okBT.setText("Login");
        }
        return okBT;
    }

    /**
	 * This method initializes unameTF	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getUnameTF() {
        if (unameTF == null) {
            unameTF = new JTextField();
            unameTF.setColumns(15);
        }
        return unameTF;
    }

    /**
	 * This method initializes upwdTF	
	 * 	
	 * @return javax.swing.JPasswordField	
	 */
    private JPasswordField getUpwdTF() {
        if (upwdTF == null) {
            upwdTF = new JPasswordField();
            upwdTF.setColumns(15);
        }
        return upwdTF;
    }
}
