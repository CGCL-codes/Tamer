package GUI;

import java.awt.Color;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridBagLayout;

public class jPanelSetPaymentMethodStep3CCYear extends JPanel {

    private static final long serialVersionUID = 1L;

    private JLabel jLabelStep3 = null;

    private JLabel jLabelInstructions = null;

    private JLabel jLabelCreditCard = null;

    private JTextField jTextFieldCreditCard = null;

    private JLabel jLabelExpiration = null;

    private JTextField jTextFieldExpirationMonth = null;

    private JLabel jLabelSlash = null;

    private JTextField jTextFieldExpirationYear = null;

    private JButton jButtonNext = null;

    private JButton jButtonPrev = null;

    private JButton jButtonCancel = null;

    protected boolean isErrorMessageVisible;

    private JLabel jLabel1ErrorMassage = null;

    private JPanel jPanelBorderUp = null;

    private JPanel jPanelBorderLeft = null;

    private JPanel jPanelBorderRight = null;

    private JPanel jPanelBorderDown = null;

    /**
	 * This is the default constructor
	 */
    public jPanelSetPaymentMethodStep3CCYear() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        isErrorMessageVisible = false;
        jLabel1ErrorMassage = new JLabel();
        jLabel1ErrorMassage.setBounds(new Rectangle(180, 340, 266, 39));
        jLabel1ErrorMassage.setForeground(Color.RED);
        jLabel1ErrorMassage.setText("You need to fill all fileds");
        jLabelSlash = new JLabel();
        jLabelSlash.setBounds(new Rectangle(587, 240, 9, 17));
        jLabelSlash.setText("/");
        jLabelExpiration = new JLabel();
        jLabelExpiration.setBounds(new Rectangle(466, 240, 95, 17));
        jLabelExpiration.setText("Expiration date:");
        jLabelCreditCard = new JLabel();
        jLabelCreditCard.setBounds(new Rectangle(180, 240, 73, 17));
        jLabelCreditCard.setText("Credit card:");
        jLabelInstructions = new JLabel();
        jLabelInstructions.setBounds(new Rectangle(180, 180, 407, 17));
        jLabelInstructions.setText("Please the following fields and press \"Next\" to continue:");
        jLabelStep3 = new JLabel();
        jLabelStep3.setBounds(new Rectangle(440, 150, 240, 17));
        jLabelStep3.setText("Step 3/4: Credit card - Annual subscription");
        this.setSize(1095, 550);
        this.setBackground(new Color(122, 197, 203));
        this.setLayout(null);
        this.add(jLabelStep3, null);
        this.add(jLabelInstructions, null);
        this.add(jLabelCreditCard, null);
        this.add(getJTextFieldCreditCard(), null);
        this.add(jLabelExpiration, null);
        this.add(getJTextFieldExpirationMonth(), null);
        this.add(jLabelSlash, null);
        this.add(getJTextFieldExpirationYear(), null);
        this.add(getJButtonNext(), null);
        this.add(getJButtonPrev(), null);
        this.add(getJButtonCancel(), null);
        this.add(getJPanelBorderUp(), null);
        this.add(getJPanelBorderLeft(), null);
        this.add(getJPanelBorderRight(), null);
        this.add(getJPanelBorderDown(), null);
    }

    /**
	 * This method initializes jTextFieldCreditCard	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    JTextField getJTextFieldCreditCard() {
        if (jTextFieldCreditCard == null) {
            jTextFieldCreditCard = new JTextField();
            jTextFieldCreditCard.setBounds(new Rectangle(255, 240, 186, 17));
            jTextFieldCreditCard.addFocusListener(new java.awt.event.FocusAdapter() {

                public void focusGained(java.awt.event.FocusEvent e) {
                    if (isErrorMessageVisible == true) {
                        remove(jLabel1ErrorMassage);
                        isErrorMessageVisible = false;
                        updateUI();
                    }
                }
            });
        }
        return jTextFieldCreditCard;
    }

    /**
	 * This method initializes jTextFieldExpirationMonth	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    JTextField getJTextFieldExpirationMonth() {
        if (jTextFieldExpirationMonth == null) {
            jTextFieldExpirationMonth = new JTextField();
            jTextFieldExpirationMonth.setBounds(new Rectangle(564, 240, 22, 17));
            jTextFieldExpirationMonth.setText("");
            jTextFieldExpirationMonth.addFocusListener(new java.awt.event.FocusAdapter() {

                public void focusGained(java.awt.event.FocusEvent e) {
                    if (isErrorMessageVisible == true) {
                        remove(jLabel1ErrorMassage);
                        isErrorMessageVisible = false;
                        updateUI();
                    }
                }
            });
        }
        return jTextFieldExpirationMonth;
    }

    /**
		 * This method initializes jTextFieldExpirationYear	
		 * 	
		 * @return javax.swing.JTextField	
		 */
    JTextField getJTextFieldExpirationYear() {
        if (jTextFieldExpirationYear == null) {
            jTextFieldExpirationYear = new JTextField();
            jTextFieldExpirationYear.setBounds(new Rectangle(597, 240, 22, 17));
            jTextFieldExpirationYear.setText("");
            jTextFieldExpirationYear.addFocusListener(new java.awt.event.FocusAdapter() {

                public void focusGained(java.awt.event.FocusEvent e) {
                    if (isErrorMessageVisible == true) {
                        remove(jLabel1ErrorMassage);
                        isErrorMessageVisible = false;
                        updateUI();
                    }
                }
            });
        }
        return jTextFieldExpirationYear;
    }

    /**
	 * This method initializes jButtonNext	
	 * 	
	 * @return javax.swing.JButton	
	 */
    JButton getJButtonPrev() {
        if (jButtonPrev == null) {
            jButtonPrev = new JButton();
            jButtonPrev.setBounds(new Rectangle(750, 406, 80, 30));
            jButtonPrev.setText("<- Prev");
            jButtonPrev.setActionCommand("pressed prev");
        }
        return jButtonPrev;
    }

    /**
	 * This method initializes jButtonCancel	
	 * 	
	 * @return javax.swing.JButton	
	 */
    JButton getJButtonNext() {
        if (jButtonNext == null) {
            jButtonNext = new JButton();
            jButtonNext.setBounds(new Rectangle(840, 406, 80, 30));
            jButtonNext.setText("Next ->");
            jButtonNext.setActionCommand("step3 Next button presesd");
        }
        return jButtonNext;
    }

    /**
	/**
	 * This method initializes jButtonCancel	
	 * 	
	 * @return javax.swing.JButton	
	 */
    JButton getJButtonCancel() {
        if (jButtonCancel == null) {
            jButtonCancel = new JButton();
            jButtonCancel.setBounds(new Rectangle(660, 406, 80, 30));
            jButtonCancel.setText("Cancel");
            jButtonCancel.setActionCommand("Cancel the payment method");
        }
        return jButtonCancel;
    }

    public void showErrorMassage() {
        isErrorMessageVisible = true;
        this.add(jLabel1ErrorMassage, null);
        this.updateUI();
    }

    /**
	 * This method initializes jPanelBorderUp	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanelBorderUp() {
        if (jPanelBorderUp == null) {
            jPanelBorderUp = new JPanel();
            jPanelBorderUp.setLayout(new GridBagLayout());
            jPanelBorderUp.setBounds(new Rectangle(150, 100, 795, 8));
        }
        return jPanelBorderUp;
    }

    /**
	 * This method initializes jPanelBorderLeft	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanelBorderLeft() {
        if (jPanelBorderLeft == null) {
            jPanelBorderLeft = new JPanel();
            jPanelBorderLeft.setLayout(new GridBagLayout());
            jPanelBorderLeft.setBounds(new Rectangle(150, 100, 8, 351));
        }
        return jPanelBorderLeft;
    }

    /**
	 * This method initializes jPanelBorderRight	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanelBorderRight() {
        if (jPanelBorderRight == null) {
            jPanelBorderRight = new JPanel();
            jPanelBorderRight.setLayout(new GridBagLayout());
            jPanelBorderRight.setBounds(new Rectangle(937, 100, 8, 351));
        }
        return jPanelBorderRight;
    }

    /**
	 * This method initializes jPanelBorderDown	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanelBorderDown() {
        if (jPanelBorderDown == null) {
            jPanelBorderDown = new JPanel();
            jPanelBorderDown.setLayout(new GridBagLayout());
            jPanelBorderDown.setBounds(new Rectangle(150, 450, 795, 8));
        }
        return jPanelBorderDown;
    }
}
