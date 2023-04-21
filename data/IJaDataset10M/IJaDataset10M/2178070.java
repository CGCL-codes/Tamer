package gui.salescreen.popup;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Rectangle;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.event.ActionListener;

public class CreditCartValidaterPopup extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    private JLabel jLabel = null;

    private JButton jButtonYes = null;

    private JButton jButtonNo = null;

    /**
	 * This is the default constructor
	 */
    public CreditCartValidaterPopup() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(290, 124);
        this.setContentPane(getJContentPane());
        this.setTitle("Credit Card Validation");
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jLabel = new JLabel();
            jLabel.setBounds(new Rectangle(29, 16, 229, 22));
            jLabel.setText("               Is The Credit Card Valid?");
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(jLabel, null);
            jContentPane.add(getJButtonYes(), null);
            jContentPane.add(getJButtonNo(), null);
        }
        return jContentPane;
    }

    /**
	 * This method initializes jButtonYes	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButtonYes() {
        if (jButtonYes == null) {
            jButtonYes = new JButton();
            jButtonYes.setBounds(new Rectangle(28, 61, 100, 17));
            jButtonYes.setText("YES");
        }
        return jButtonYes;
    }

    /**
	 * This method initializes jButtonNo	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButtonNo() {
        if (jButtonNo == null) {
            jButtonNo = new JButton();
            jButtonNo.setBounds(new Rectangle(156, 61, 100, 17));
            jButtonNo.setText("No");
        }
        return jButtonNo;
    }

    public void noButtonActionListener(ActionListener listener) {
        jButtonNo.addActionListener(listener);
    }

    public void yesButtonActionListener(ActionListener listener) {
        jButtonYes.addActionListener(listener);
    }
}
