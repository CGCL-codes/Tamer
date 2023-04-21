package gui.salescreen.popup;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.Rectangle;
import javax.swing.JTextField;
import java.awt.event.ActionListener;

public class CreditCardPaymentPopup extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    private JButton jButtonConfirm = null;

    private JTextField jTextField = null;

    /**
	 * This is the default constructor
	 */
    public CreditCardPaymentPopup() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(255, 128);
        this.setContentPane(getJContentPane());
        this.setTitle("CreditCArtPopup");
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getJButtonConfirm(), null);
            jContentPane.add(getJTextField(), null);
        }
        return jContentPane;
    }

    /**
	 * This method initializes jButtonConfirm	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButtonConfirm() {
        if (jButtonConfirm == null) {
            jButtonConfirm = new JButton();
            jButtonConfirm.setBounds(new Rectangle(73, 66, 110, 27));
            jButtonConfirm.setText("Confirm");
        }
        return jButtonConfirm;
    }

    /**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getJTextField() {
        if (jTextField == null) {
            jTextField = new JTextField();
            jTextField.setBounds(new Rectangle(63, 25, 131, 27));
            jTextField.setText("0");
        }
        return jTextField;
    }

    public String getText() {
        return getJTextField().getText();
    }

    public void addCreditCArdButtonActionListener(ActionListener listener) {
        jButtonConfirm.addActionListener(listener);
    }
}
