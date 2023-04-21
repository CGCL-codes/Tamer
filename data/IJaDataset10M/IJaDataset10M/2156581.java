package org.paccman.ui.transactions.form;

import java.awt.CardLayout;
import org.paccman.ui.common.ValidateCancelListener;
import org.paccman.ui.main.Main;

/**
 *
 * @author  jfer
 */
public class TransactionFormFrame extends javax.swing.JDialog implements ValidateCancelListener {

    private static final String TRANSACTION_FORM_CARD = "TRANSACTION_FORM_CARD";

    private static final String SPLIT_FORM_CARD = "SPLIT_FORM_CARD";

    /** Creates new form TransactionFormFrame */
    public TransactionFormFrame() {
        super(Main.getMain());
        initComponents();
        setLocationRelativeTo(null);
        validateCancelPanel.addListener(this);
        ((CardLayout) transactionCardPanel.getLayout()).show(transactionCardPanel, TRANSACTION_FORM_CARD);
    }

    public void onValidateAction() {
    }

    public void onCancelAction() {
    }

    private void initComponents() {
        validateCancelPanel = new org.paccman.ui.common.ValidateCancelPanel();
        transactionCardPanel = new javax.swing.JPanel();
        transactionFormPanel = new org.paccman.ui.transactions.form.TransactionFormPanel();
        splitFormPanel = new org.paccman.ui.transactions.form.SplitFormPanel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Edit Transaction");
        setModal(true);
        getContentPane().add(validateCancelPanel, java.awt.BorderLayout.SOUTH);
        transactionCardPanel.setLayout(new java.awt.CardLayout());
        transactionCardPanel.add(transactionFormPanel, "TRANSACTION_FORM_CARD");
        javax.swing.GroupLayout splitFormPanelLayout = new javax.swing.GroupLayout(splitFormPanel);
        splitFormPanel.setLayout(splitFormPanelLayout);
        splitFormPanelLayout.setHorizontalGroup(splitFormPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 516, Short.MAX_VALUE));
        splitFormPanelLayout.setVerticalGroup(splitFormPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 259, Short.MAX_VALUE));
        transactionCardPanel.add(splitFormPanel, "SPLIT_FORM_CARD");
        getContentPane().add(transactionCardPanel, java.awt.BorderLayout.CENTER);
        pack();
    }

    private org.paccman.ui.transactions.form.SplitFormPanel splitFormPanel;

    private javax.swing.JPanel transactionCardPanel;

    private org.paccman.ui.transactions.form.TransactionFormPanel transactionFormPanel;

    private org.paccman.ui.common.ValidateCancelPanel validateCancelPanel;
}
