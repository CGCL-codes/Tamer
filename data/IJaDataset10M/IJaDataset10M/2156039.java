package org.paccman.ui.accounts;

import java.math.BigDecimal;
import java.text.ParseException;
import org.paccman.controller.AccountController;
import org.paccman.controller.BankController;
import org.paccman.controller.ControllerManager;
import org.paccman.paccman.Account;
import org.paccman.paccman.Bank;
import org.paccman.ui.main.Main;
import org.paccman.ui.form.BadInputException;
import org.paccman.ui.form.PaccmanForm;
import static org.paccman.ui.main.ContextMain.*;

/**
 *
 * @author  joao
 */
public class AccountFormPanel extends PaccmanForm {

    /** Creates new form AccountFormPanel */
    public AccountFormPanel() {
        initComponents();
        setEditMode(false);
    }

    public void setForm(org.paccman.controller.Controller controller) {
        Account account = ((AccountController) controller).getAccount();
        accountNameEdt.setText(account.getName());
        jBankCb.setSelectedItem(ControllerManager.getController(account.getBank()));
        initialBalanceEdt.setValue(account.getInitialBalance());
        currentBalanceEdt.setValue(account.getCurrentBalance());
        holderNameEdt.setText(account.getHolderName());
        holderAddressEdt.setText(account.getHolderAddress());
        accountNumberEdt.setText(account.getAccountNumber());
        accountKeyEdt.setText(account.getAccountNumberKey());
    }

    public void setEditMode(boolean editing) {
        accountNameEdt.setEnabled(editing);
        jBankCb.setEnabled(editing);
        initialBalanceEdt.setEnabled(editing);
        holderNameEdt.setEnabled(editing);
        holderAddressEdt.setEnabled(editing);
        accountNumberEdt.setEnabled(editing);
        accountKeyEdt.setEnabled(editing);
    }

    public void registerToDocumentCtrl() {
        jBankCb.setDocumentController(getDocumentController());
    }

    public void clearForm() {
        accountNameEdt.setText("");
        jBankCb.setSelectedItem(null);
        initialBalanceEdt.setValue(null);
        currentBalanceEdt.setValue(null);
        holderNameEdt.setText("");
        holderAddressEdt.setText("");
        accountNumberEdt.setText("");
        accountKeyEdt.setText("");
    }

    public void getForm(org.paccman.controller.Controller controller) throws BadInputException {
        AccountController accountCtrl = (AccountController) controller;
        String name = accountNameEdt.getText();
        if (editingNew) {
            if (getDocumentController().getDocument().getAccount(name) != null) {
                throw new BadInputException("An account with the same name already exists", accountNameEdt);
            }
        } else {
            if (!name.equals(accountCtrl.getAccount().getName())) {
                if (getDocumentController().getDocument().getAccount(name) != null) {
                    throw new BadInputException("An account with the same name already exists", accountNameEdt);
                }
            }
        }
        try {
            initialBalanceEdt.commitEdit();
        } catch (ParseException exception) {
            throw new BadInputException("Enter a valid amount for the initial balance.", initialBalanceEdt);
        }
        BigDecimal initialBalance = (BigDecimal) initialBalanceEdt.getValue();
        if (jBankCb.getSelectedItem() == null) {
            throw new BadInputException("You must choose a bank for this account.", jBankCb);
        }
        Bank bank = ((BankController) jBankCb.getSelectedItem()).getBank();
        String holderName = holderNameEdt.getText();
        String holderAddress = holderAddressEdt.getText();
        String accountNumber = accountNumberEdt.getText();
        String accountKey = accountKeyEdt.getText();
        accountCtrl.getAccount().setName(name);
        accountCtrl.getAccount().setInitialBalance(initialBalance);
        accountCtrl.getAccount().setBank(bank);
        accountCtrl.getAccount().setHolderName(holderName);
        accountCtrl.getAccount().setHolderAddress(holderAddress);
        accountCtrl.getAccount().setAccountNumber(accountNumber);
        accountCtrl.getAccount().setAccountNumberKey(accountKey);
    }

    public org.paccman.controller.Controller getNewController() {
        return new AccountController();
    }

    private void initComponents() {
        jNameLbl = new javax.swing.JLabel();
        accountNameEdt = new javax.swing.JTextField();
        jBankLbl = new javax.swing.JLabel();
        jBankCb = new org.paccman.ui.common.BankComboBox();
        accountNumberLbl = new javax.swing.JLabel();
        accountNumberEdt = new javax.swing.JTextField();
        accountKeyEdt = new javax.swing.JTextField();
        balancesPanel = new javax.swing.JPanel();
        initialBalanceLbl = new javax.swing.JLabel();
        initialBalanceEdt = new org.paccman.ui.common.AmountTextField();
        currentBalanceLbl = new javax.swing.JLabel();
        currentBalanceEdt = new org.paccman.ui.common.AmountTextField();
        accountHolderPanel = new javax.swing.JPanel();
        holderNameLbl = new javax.swing.JLabel();
        holderNameEdt = new javax.swing.JTextField();
        holderAddressLbl = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        holderAddressEdt = new javax.swing.JTextArea();
        jNameLbl.setText("Name");
        jBankLbl.setText("Bank");
        accountNumberLbl.setText("Account number/Key");
        balancesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Balances"));
        initialBalanceLbl.setText("Initial balance");
        initialBalanceEdt.setEnabled(false);
        currentBalanceLbl.setText("Current balance");
        currentBalanceEdt.setEnabled(false);
        javax.swing.GroupLayout balancesPanelLayout = new javax.swing.GroupLayout(balancesPanel);
        balancesPanel.setLayout(balancesPanelLayout);
        balancesPanelLayout.setHorizontalGroup(balancesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(balancesPanelLayout.createSequentialGroup().addContainerGap().addGroup(balancesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(currentBalanceLbl).addComponent(initialBalanceLbl)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(balancesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(initialBalanceEdt, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE).addComponent(currentBalanceEdt, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)).addContainerGap()));
        balancesPanelLayout.setVerticalGroup(balancesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(balancesPanelLayout.createSequentialGroup().addContainerGap().addGroup(balancesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(initialBalanceLbl).addComponent(initialBalanceEdt, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(balancesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(currentBalanceLbl).addComponent(currentBalanceEdt, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        accountHolderPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Account holder"));
        holderNameLbl.setText("Name");
        holderNameEdt.setEnabled(false);
        holderAddressLbl.setText("Address");
        jScrollPane1.setEnabled(false);
        holderAddressEdt.setColumns(20);
        holderAddressEdt.setRows(5);
        holderAddressEdt.setEnabled(false);
        jScrollPane1.setViewportView(holderAddressEdt);
        javax.swing.GroupLayout accountHolderPanelLayout = new javax.swing.GroupLayout(accountHolderPanel);
        accountHolderPanel.setLayout(accountHolderPanelLayout);
        accountHolderPanelLayout.setHorizontalGroup(accountHolderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(accountHolderPanelLayout.createSequentialGroup().addContainerGap().addGroup(accountHolderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(holderAddressLbl).addComponent(holderNameLbl)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(accountHolderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE).addComponent(holderNameEdt, javax.swing.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE)).addContainerGap()));
        accountHolderPanelLayout.setVerticalGroup(accountHolderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(accountHolderPanelLayout.createSequentialGroup().addGroup(accountHolderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(holderNameLbl).addComponent(holderNameEdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(accountHolderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(holderAddressLbl).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)).addContainerGap()));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jBankLbl, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jNameLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addComponent(accountNumberLbl)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jBankCb, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE).addComponent(accountNameEdt, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(accountNumberEdt, javax.swing.GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(accountKeyEdt, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))).addComponent(accountHolderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(balancesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jNameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBankLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addComponent(accountNameEdt, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBankCb, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(accountNumberLbl).addComponent(accountKeyEdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(accountNumberEdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(accountHolderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(balancesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(14, 14, 14)));
        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] { accountKeyEdt, accountNameEdt, accountNumberEdt, accountNumberLbl, jBankCb, jBankLbl, jNameLbl });
    }

    private javax.swing.JPanel accountHolderPanel;

    private javax.swing.JTextField accountKeyEdt;

    private javax.swing.JTextField accountNameEdt;

    private javax.swing.JTextField accountNumberEdt;

    private javax.swing.JLabel accountNumberLbl;

    private javax.swing.JPanel balancesPanel;

    private org.paccman.ui.common.AmountTextField currentBalanceEdt;

    private javax.swing.JLabel currentBalanceLbl;

    private javax.swing.JTextArea holderAddressEdt;

    private javax.swing.JLabel holderAddressLbl;

    private javax.swing.JTextField holderNameEdt;

    private javax.swing.JLabel holderNameLbl;

    private org.paccman.ui.common.AmountTextField initialBalanceEdt;

    private javax.swing.JLabel initialBalanceLbl;

    private org.paccman.ui.common.BankComboBox jBankCb;

    private javax.swing.JLabel jBankLbl;

    private javax.swing.JLabel jNameLbl;

    private javax.swing.JScrollPane jScrollPane1;
}
