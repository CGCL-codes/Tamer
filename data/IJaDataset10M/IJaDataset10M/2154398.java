package org.paccman.ui.transactions;

import org.paccman.ui.transactions.CategoryComboBoxCellRenderer;

/**
 *
 * @author  jfer
 */
public class AccountSelectorComboBox extends javax.swing.JComboBox {

    /** Creates new form BeanForm */
    public AccountSelectorComboBox() {
        setModel(accountSelectorComboModel);
        initComponents();
    }

    private void initComponents() {
    }

    private org.paccman.ui.transactions.AccountSelectorComboModel accountSelectorComboModel = new AccountSelectorComboModel();
}
