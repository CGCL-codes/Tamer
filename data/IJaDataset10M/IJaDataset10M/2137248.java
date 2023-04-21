package busticketvendingsystem;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import busticketvendingsystem.*;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;

/**
 *
 * @author  Samik Saha
 */
public class PaymentForm extends javax.swing.JDialog {

    Frame parent;

    private static UserData userData;

    /** Creates new form PaymentForm */
    public PaymentForm(Frame parentDialog, boolean modal, UserData ud) {
        super(parentDialog, modal);
        parent = parentDialog;
        userData = new UserData();
        if (ud != null) userData = ud;
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        topPanel = new javax.swing.JPanel();
        amountLabel = new javax.swing.JLabel();
        payAmountLabel = new javax.swing.JLabel();
        bottomPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        helpButton1 = new javax.swing.JButton();
        currencyPanel = new javax.swing.JPanel();
        amtPaidLabel = new javax.swing.JLabel();
        enterField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Please pay");
        amountLabel.setFont(new java.awt.Font("Tahoma", 1, 11));
        amountLabel.setText("Rs." + userData.getAmount());
        payAmountLabel.setFont(new java.awt.Font("Tahoma", 1, 11));
        payAmountLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/busticketvendingsystem/resources/images/pay_money.gif")));
        payAmountLabel.setText("PLEASE PAY");
        org.jdesktop.layout.GroupLayout topPanelLayout = new org.jdesktop.layout.GroupLayout(topPanel);
        topPanel.setLayout(topPanelLayout);
        topPanelLayout.setHorizontalGroup(topPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(topPanelLayout.createSequentialGroup().add(payAmountLabel).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(amountLabel).addContainerGap(97, Short.MAX_VALUE)));
        topPanelLayout.setVerticalGroup(topPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(topPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(payAmountLabel).add(amountLabel)));
        getContentPane().add(topPanel, java.awt.BorderLayout.NORTH);
        bottomPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 10));
        okButton.setAction(new OkAction());
        okButton.setText("OK");
        bottomPanel.add(okButton);
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        bottomPanel.add(cancelButton);
        helpButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/busticketvendingsystem/resources/images/help_icon_small.gif")));
        helpButton1.setText("Help");
        helpButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpButton1ActionPerformed(evt);
            }
        });
        bottomPanel.add(helpButton1);
        getContentPane().add(bottomPanel, java.awt.BorderLayout.SOUTH);
        currencyPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        amtPaidLabel.setText("You are paying Rs.");
        currencyPanel.add(amtPaidLabel);
        enterField.setAction(new OkAction());
        enterField.setPreferredSize(new java.awt.Dimension(60, 20));
        currencyPanel.add(enterField);
        jLabel1.setText("(Example: 5.50, 13.00 etc)");
        currencyPanel.add(jLabel1);
        getContentPane().add(currencyPanel, java.awt.BorderLayout.CENTER);
        pack();
    }

    private void helpButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        new HelpWindow(PaymentForm.this, true, "payment.htm").setVisible(true);
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new PaymentForm(null, true, null).setVisible(true);
            }
        });
    }

    private javax.swing.JLabel amountLabel;

    private javax.swing.JLabel amtPaidLabel;

    private javax.swing.JPanel bottomPanel;

    private javax.swing.JButton cancelButton;

    private javax.swing.JPanel currencyPanel;

    private javax.swing.JTextField enterField;

    private javax.swing.JButton helpButton1;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JButton okButton;

    private javax.swing.JLabel payAmountLabel;

    private javax.swing.JPanel topPanel;

    class OkAction extends AbstractAction {

        public OkAction() {
            super();
        }

        public void actionPerformed(ActionEvent e) {
            String amtStr = PaymentForm.this.enterField.getText();
            if (amtStr.isEmpty()) JOptionPane.showMessageDialog(PaymentForm.this, "You cannot leave the field empty!", "ERROR", JOptionPane.ERROR_MESSAGE); else {
                float amt;
                float balance;
                try {
                    amt = Float.parseFloat(amtStr);
                    if (amt < userData.getAmount()) JOptionPane.showMessageDialog(PaymentForm.this, "Entered amount is not sufficient.", "ERROR", JOptionPane.ERROR_MESSAGE); else {
                        PaymentForm.this.dispose();
                        balance = amt - userData.getAmount();
                        if (balance != 0) {
                            JOptionPane.showMessageDialog(parent, "Please collect the balance amount Rs." + balance, "Your Balance", JOptionPane.INFORMATION_MESSAGE);
                        }
                        ConnectDatabase cd = new ConnectDatabase();
                        for (int i = 1; i <= userData.getNoTickets(); i++) {
                            SimpleDateFormat datetime = new SimpleDateFormat("yyyyMMddHHmmss");
                            String dt = datetime.format(new java.util.Date());
                            DecimalFormat threeDigits = new DecimalFormat("000");
                            String ids = threeDigits.format(cd.getStoppage_id(userData.getStartingStop())) + threeDigits.format(cd.getStoppage_id(userData.getDestination()));
                            String id = "T" + dt + ids + userData.getDirection();
                            userData.setTicketId(id);
                            boolean type = cd.set_Transaction(id, userData.getBusNo(), userData.getStartingStop(), userData.getDestination(), userData.getAmount(), userData.getDate());
                        }
                        int up = 0, down = 0;
                        if (userData.getDirection() == 1) {
                            up = userData.getNoTickets();
                        } else {
                            down = userData.getNoTickets();
                        }
                        cd.set_Transaction_per_day(userData.getDate(), userData.getBusNo(), up, down, userData.getAmount());
                        new PrintTicketsDialog(parent, true, userData).setVisible(true);
                    }
                } catch (java.lang.NumberFormatException exception) {
                    JOptionPane.showMessageDialog(PaymentForm.this, "Invalid Data!", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
