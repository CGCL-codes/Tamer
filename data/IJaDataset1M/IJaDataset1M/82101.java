package org.paccman.ui.transactions;

import java.awt.CardLayout;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import org.paccman.controller.AccountController;
import org.paccman.controller.ControllerManager;
import org.paccman.controller.ScheduledTransactionController;
import org.paccman.controller.TransactionBaseController;
import org.paccman.controller.TransferController;
import org.paccman.paccman.TransactionBase;
import org.paccman.ui.main.Main;
import org.paccman.ui.scheduling.Scheduler;
import org.paccman.ui.selector.ControllerSelectionListener;
import org.paccman.ui.transactions.reconcile.ReconcileContext;
import org.paccman.ui.transactions.split.SplitForm;
import org.paccman.ui.transactions.table.TransactionTableModel;
import static org.paccman.ui.main.ContextMain.*;

/**
 *
 * @author  joao
 */
public class TransactionFormTab extends javax.swing.JPanel implements ControllerSelectionListener, ListSelectionListener {

    /** Creates new form TransactionsTab */
    public TransactionFormTab() {
        initComponents();
        accountSelectorPanel.addListener(this);
        accountSelectorPanel.addListener(transactionFormPanel);
        transactionTable.getSelectionModel().addListSelectionListener(this);
        setTransactionFormPanelVisible(true);
    }

    public void controllerSelected(org.paccman.controller.Controller accountCtrl) {
        if (selectedAccount != accountCtrl) {
            selectedAccount = (AccountController) accountCtrl;
            transactionTable.getTransactionTableModel().setAccountController(selectedAccount);
            newBtn.setEnabled(true);
            reconcileBtn.setEnabled(true);
        }
    }

    private void initComponents() {
        transactionPanelCard = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        transactionsPanel = new javax.swing.JPanel();
        formPanel = new javax.swing.JPanel();
        validateCancelPanel = new javax.swing.JPanel();
        validateBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        transactionFormPanel = new org.paccman.ui.transactions.TransactionFormPanel();
        transactionTableScrollPane = new javax.swing.JScrollPane();
        transactionTable = new org.paccman.ui.transactions.table.TransactionTable();
        toolbarPanel = new javax.swing.JPanel();
        mainToolbar = new javax.swing.JToolBar();
        newBtn = new javax.swing.JButton();
        editBtn = new javax.swing.JButton();
        removeBtn = new javax.swing.JButton();
        reconcileBtn = new javax.swing.JButton();
        showHideBtn = new javax.swing.JButton();
        leftTransactionPanel = new javax.swing.JPanel();
        accountSelectorPanel = new org.paccman.ui.accountselector.AccountSelectorPanel();
        reconcilePanel = new javax.swing.JPanel();
        reconcileDataPanel = new org.paccman.ui.transactions.reconcile.ReconcilePanel();
        finishCancelPanel = new javax.swing.JPanel();
        finishBtn = new javax.swing.JButton();
        cancelReconcileBtn = new javax.swing.JButton();
        splitPanelCard = new org.paccman.ui.transactions.split.SplitForm();
        transactionFormPanel.setSplitForm(splitPanelCard);
        setLayout(new java.awt.CardLayout());
        transactionPanelCard.setLayout(new java.awt.BorderLayout());
        transactionsPanel.setLayout(new java.awt.BorderLayout());
        formPanel.setLayout(new java.awt.BorderLayout());
        validateCancelPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        validateBtn.setText("Validate");
        validateBtn.setEnabled(false);
        validateBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                validateBtnActionPerformed(evt);
            }
        });
        validateCancelPanel.add(validateBtn);
        cancelBtn.setText("Cancel");
        cancelBtn.setEnabled(false);
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });
        validateCancelPanel.add(cancelBtn);
        formPanel.add(validateCancelPanel, java.awt.BorderLayout.SOUTH);
        formPanel.add(transactionFormPanel, java.awt.BorderLayout.NORTH);
        transactionsPanel.add(formPanel, java.awt.BorderLayout.SOUTH);
        transactionTable.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                transactionTableMouseClicked(evt);
            }
        });
        transactionTableScrollPane.setViewportView(transactionTable);
        transactionsPanel.add(transactionTableScrollPane, java.awt.BorderLayout.CENTER);
        toolbarPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        newBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/paccman/ui/resources/images/new_transaction.png")));
        newBtn.setToolTipText("Add new transaction");
        newBtn.setEnabled(false);
        newBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newBtnActionPerformed(evt);
            }
        });
        mainToolbar.add(newBtn);
        editBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/paccman/ui/resources/images/edit_transaction.png")));
        editBtn.setToolTipText("Edit current selected transaction");
        editBtn.setEnabled(false);
        editBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBtnActionPerformed(evt);
            }
        });
        mainToolbar.add(editBtn);
        removeBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/paccman/ui/resources/images/remove_transaction.png")));
        removeBtn.setToolTipText("Delete current selected transaction");
        removeBtn.setEnabled(false);
        removeBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeBtnActionPerformed(evt);
            }
        });
        mainToolbar.add(removeBtn);
        reconcileBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/paccman/ui/resources/images/reconcile.png")));
        reconcileBtn.setEnabled(false);
        reconcileBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reconcileBtnActionPerformed(evt);
            }
        });
        mainToolbar.add(reconcileBtn);
        showHideBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/paccman/ui/resources/images/transaction_hide.png")));
        showHideBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showHideBtnActionPerformed(evt);
            }
        });
        mainToolbar.add(showHideBtn);
        toolbarPanel.add(mainToolbar);
        transactionsPanel.add(toolbarPanel, java.awt.BorderLayout.NORTH);
        jSplitPane1.setRightComponent(transactionsPanel);
        leftTransactionPanel.setLayout(new java.awt.CardLayout());
        leftTransactionPanel.add(accountSelectorPanel, "ACCOUNT_SELECTOR_PANEL");
        reconcilePanel.setLayout(new java.awt.BorderLayout());
        reconcilePanel.add(reconcileDataPanel, java.awt.BorderLayout.NORTH);
        finishCancelPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        finishBtn.setText("Finish");
        finishBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finishBtnActionPerformed(evt);
            }
        });
        finishCancelPanel.add(finishBtn);
        cancelReconcileBtn.setText("Cancel");
        cancelReconcileBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelReconcileBtnActionPerformed(evt);
            }
        });
        finishCancelPanel.add(cancelReconcileBtn);
        reconcilePanel.add(finishCancelPanel, java.awt.BorderLayout.SOUTH);
        leftTransactionPanel.add(reconcilePanel, "RECONCILE");
        jSplitPane1.setLeftComponent(leftTransactionPanel);
        transactionPanelCard.add(jSplitPane1, java.awt.BorderLayout.CENTER);
        add(transactionPanelCard, "TRANSACTION_CARD");
        add(splitPanelCard, "SPLIT_CARD");
    }

    boolean transactionFormPanelVisible = true;

    private void setTransactionFormPanelVisible(boolean visible) {
        transactionFormPanelVisible = visible;
        transactionFormPanel.setVisible(transactionFormPanelVisible);
        validateCancelPanel.setVisible(transactionFormPanelVisible);
        if (transactionFormPanelVisible) {
            showHideBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/paccman/ui/resources/images/transaction_hide.png")));
        } else {
            showHideBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/paccman/ui/resources/images/transaction_show.png")));
        }
    }

    private void showHideBtnActionPerformed(java.awt.event.ActionEvent evt) {
        assert !editing : "Show/Hide button should be disable in edit mode";
        setTransactionFormPanelVisible(!transactionFormPanelVisible);
    }

    private void cancelReconcileBtnActionPerformed(java.awt.event.ActionEvent evt) {
        reconciling = false;
        reconcileBtn.setEnabled(true);
        transactionTable.getTransactionTableModel().setMode(TransactionTableModel.Mode.VIEW_MODE);
        showAccountSelector();
    }

    private void finishBtnActionPerformed(java.awt.event.ActionEvent evt) {
        ReconcileContext context = reconcileDataPanel.getData();
        if (context.diffMarkedBalance.compareTo(BigDecimal.ZERO) == 0) {
            transactionTable.getTransactionTableModel().setMode(TransactionTableModel.Mode.VIEW_MODE);
            transactionTable.getTransactionTableModel().validateReconciliation();
            selectedAccount.getAccount().setLastReconciliationDate(context.newDate);
            selectedAccount.getAccount().setLastReconciliationBalance(context.newBalance);
            selectedAccount.getAccount().setPendingReconciliation(false);
            selectedAccount.getAccount().setPendingReconciliationBalance(null);
            selectedAccount.getAccount().setPendingReconciliationDate(null);
            selectedAccount.notifyChange();
            org.paccman.ui.main.Main.setDocumentChanged(true);
            reconciling = false;
            reconcileBtn.setEnabled(true);
            showAccountSelector();
        } else {
            String[] choices = { "Keep pending", "Cancel", "Abort" };
            Object diag = JOptionPane.showInputDialog(this, "The reconciliation is not finished:TODO:make better message:", "Confirm", JOptionPane.OK_OPTION, null, choices, choices[0]);
            if (diag == choices[0]) {
                transactionTable.getTransactionTableModel().setMode(TransactionTableModel.Mode.VIEW_MODE);
                transactionTable.getTransactionTableModel().setPendingReconciliation();
                selectedAccount.getAccount().setPendingReconciliation(true);
                selectedAccount.getAccount().setPendingReconciliationBalance(context.newBalance);
                selectedAccount.getAccount().setPendingReconciliationDate(context.newDate);
                selectedAccount.notifyChange();
                org.paccman.ui.main.Main.setDocumentChanged(true);
                reconciling = false;
                reconcileBtn.setEnabled(true);
                showAccountSelector();
            } else if (diag == choices[1]) {
                return;
            } else if (diag == choices[2]) {
                cancelReconcileBtnActionPerformed(evt);
            }
        }
    }

    private void transactionTableMouseClicked(java.awt.event.MouseEvent evt) {
        if (reconciling) {
            int col = transactionTable.columnAtPoint(evt.getPoint());
            int dataIndex = transactionTable.getTransactionTableModel().getColumnDescriptors()[col].getDataIndex();
            if (dataIndex == TransactionTableModel.getStatusDataIndex()) {
                switchReconciliationStatus(selectedTransaction.getTransactionBase(), selectedTransactionIndex);
            }
        }
    }

    boolean reconciling;

    private void showReconcilePanel() {
        CardLayout cl = (CardLayout) leftTransactionPanel.getLayout();
        cl.show(leftTransactionPanel, "RECONCILE");
    }

    private void showAccountSelector() {
        CardLayout cl = (CardLayout) leftTransactionPanel.getLayout();
        cl.show(leftTransactionPanel, "ACCOUNT_SELECTOR_PANEL");
    }

    private void reconcileBtnActionPerformed(java.awt.event.ActionEvent evt) {
        assert selectedAccount != null;
        assert !reconciling : "Button should be disabled when reconciling.";
        transactionTable.getTransactionTableModel().setMode(TransactionTableModel.Mode.STATUS_EDIT_MODE);
        Calendar date = selectedAccount.getAccount().getLastReconciliationDate();
        BigDecimal balance = selectedAccount.getAccount().getLastReconciliationBalance();
        if (balance == null) {
            balance = selectedAccount.getAccount().getInitialBalance();
            if (selectedAccount.getAccount().getNumberOfTransactions() > 0) {
                date = selectedAccount.getAccount().getTransaction(0).getTransactionDate();
            } else {
                date = new GregorianCalendar();
            }
        }
        Calendar pendingDate;
        BigDecimal pendingBalance;
        BigDecimal markedAmount;
        if (selectedAccount.getAccount().isPendingReconciliation()) {
            pendingDate = selectedAccount.getAccount().getPendingReconciliationDate();
            pendingBalance = selectedAccount.getAccount().getPendingReconciliationBalance();
            markedAmount = selectedAccount.getAccount().getMarkedAmount();
        } else {
            pendingDate = new GregorianCalendar();
            pendingBalance = BigDecimal.ZERO;
            markedAmount = BigDecimal.ZERO;
        }
        reconcileDataPanel.setData(date, balance, pendingDate, pendingBalance, markedAmount);
        reconcileBtn.setEnabled(false);
        reconciling = true;
        showReconcilePanel();
    }

    private void removeBtnActionPerformed(java.awt.event.ActionEvent evt) {
        assert selectedTransaction != null;
        unselectTransaction();
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this transaction ?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            selectedAccount.getAccount().removeTransaction(selectedTransactionIndex);
            selectedAccount.notifyChange();
            org.paccman.ui.main.Main.setDocumentChanged(true);
        }
    }

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {
        setEditMode(false);
        transactionFormPanel.onCancel();
        if (expScheduledTransaction != null) {
            Main.getMain().gotoWelcomeTab();
            expScheduledTransaction = null;
        }
    }

    private void editBtnActionPerformed(java.awt.event.ActionEvent evt) {
        setEditMode(true);
        transactionFormPanel.onEdit();
    }

    AccountController selectedAccount;

    TransactionBaseController selectedTransaction;

    int selectedTransactionIndex;

    boolean editing;

    public void setEditMode(boolean editing) {
        this.editing = editing;
        newBtn.setEnabled(!editing && (selectedAccount != null));
        reconcileBtn.setEnabled(!editing && (selectedAccount != null));
        validateBtn.setEnabled(editing);
        cancelBtn.setEnabled(editing);
        transactionTable.setEnabled(!editing);
        splitPanelCard.setEditMode(editing);
    }

    public void showSplitCard() {
        CardLayout cl = (CardLayout) getLayout();
        cl.show(this, "SPLIT_CARD");
    }

    public void showTransactionCard() {
        CardLayout cl = (CardLayout) getLayout();
        cl.show(this, "TRANSACTION_CARD");
    }

    protected void addOtherTransfer(TransferController transfer) {
        TransferController destTransfer = new TransferController();
        destTransfer.getTransfer().setAmount(transfer.getTransfer().getAmount().negate());
        destTransfer.getTransfer().setValueDate(transfer.getTransfer().getValueDate());
        destTransfer.getTransfer().setTransactionDate(transfer.getTransfer().getTransactionDate());
        destTransfer.getTransfer().setToFromAccount(selectedAccount.getAccount());
        destTransfer.getTransfer().setLabel(transfer.getTransfer().getLabel());
        transfer.getTransfer().getToFromAccount().addTransfer(destTransfer.getTransfer(), true);
    }

    private void validateBtnActionPerformed(java.awt.event.ActionEvent evt) {
        TransactionBaseController validatedTransaction = null;
        validatedTransaction = (TransactionBaseController) transactionFormPanel.onValidate();
        if (validatedTransaction != null) {
            setEditMode(false);
            if (transactionFormPanel.isEditingNew()) {
                int newTransactionIx = selectedAccount.getAccount().addTransaction(validatedTransaction.getTransactionBase(), true);
                if (validatedTransaction instanceof TransferController) {
                    addOtherTransfer((TransferController) validatedTransaction);
                }
                selectedAccount.notifyChange();
                newTransactionIx = transactionTable.getTransactionTableModel().transactionIndexToTransactionRow(newTransactionIx);
                transactionTable.getSelectionModel().setSelectionInterval(newTransactionIx, newTransactionIx);
                if (expScheduledTransaction != null) {
                    ScheduledTransactionController stc = expScheduledTransaction.getScheduledTransaction();
                    Calendar scheduledNextOccurence = stc.getScheduledTransaction().getNextOccurence();
                    Calendar expiredNextOccurence = expScheduledTransaction.getOccurence();
                    Scheduler.computeNextOccurence(expiredNextOccurence, stc.getScheduledTransaction().getPeriodUnit(), stc.getScheduledTransaction().getPeriod());
                    if (scheduledNextOccurence.compareTo(expiredNextOccurence) < 0) {
                        stc.getScheduledTransaction().setNextOccurence(expiredNextOccurence);
                    }
                    expScheduledTransaction.setRegistered(true);
                    getDocumentController().notifyChange();
                    Main.getMain().gotoWelcomeTab();
                    expScheduledTransaction = null;
                }
            } else {
                assert selectedTransaction.getTransactionBase().getReconciliationState() != TransactionBase.ReconciliationState.RECONCILED;
                if (validatedTransaction != selectedTransaction) {
                    selectedAccount.getAccount().removeTransaction(selectedTransactionIndex);
                    int newTransactionIx = selectedAccount.getAccount().addTransaction(validatedTransaction.getTransactionBase(), true);
                    if (validatedTransaction instanceof TransferController) {
                        addOtherTransfer((TransferController) validatedTransaction);
                    }
                    TransactionBase oldTransactionBase = selectedTransaction.getTransactionBase();
                    TransactionBase newTransactionBase = validatedTransaction.getTransactionBase();
                    newTransactionIx = transactionTable.getTransactionTableModel().transactionIndexToTransactionRow(newTransactionIx);
                    selectedAccount.notifyChange();
                    transactionTable.getSelectionModel().setSelectionInterval(newTransactionIx, newTransactionIx);
                } else {
                    int transactionRow = transactionTable.getSelectedRow();
                    int transactionIndex = transactionTable.getTransactionTableModel().transactionRowToTransactionIndex(transactionRow);
                    int updatedTransactionIx = selectedAccount.getAccount().updateTransaction(transactionIndex);
                    int updatedTransactionRow = transactionTable.getTransactionTableModel().transactionRowToTransactionIndex(updatedTransactionIx);
                    selectedAccount.notifyChange();
                    selectedTransaction.notifyChange();
                    transactionTable.getSelectionModel().setSelectionInterval(updatedTransactionRow, updatedTransactionRow);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Failed to validate", "Validation failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void newBtnActionPerformed(java.awt.event.ActionEvent evt) {
        newTransaction(null);
    }

    public void newTransaction(TransactionBaseController transaction) {
        setEditMode(true);
        if (transaction == null) {
            transactionFormPanel.onNew();
            splitPanelCard.onNew();
        } else {
            transactionFormPanel.onNew(transaction);
        }
    }

    public void registerToDocumentCtrl() {
        accountSelectorPanel.registerToDocumentCtrl();
        transactionFormPanel.registerToDocumentCtrl();
        splitPanelCard.registerToDocumentCtrl();
    }

    public boolean selectionEnabled() {
        if (transactionFormPanel.isEditing()) {
            int diag = JOptionPane.showConfirmDialog(this, "You are editing a transaction. Abort ?", "Confirm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            if (diag == JOptionPane.OK_OPTION) {
                cancelBtnActionPerformed(null);
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void valueChanged(javax.swing.event.ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        ListSelectionModel lsm = (ListSelectionModel) e.getSource();
        boolean selectionEmpty = lsm.isSelectionEmpty();
        if (selectionEmpty) {
            unselectTransaction();
        } else {
            selectedTransactionIndex = lsm.getMinSelectionIndex();
            selectedTransactionIndex = transactionTable.getTransactionTableModel().transactionRowToTransactionIndex(selectedTransactionIndex);
            selectedTransaction = (TransactionBaseController) ControllerManager.getController(selectedAccount.getAccount().getTransaction(selectedTransactionIndex));
            transactionFormPanel.onSelect(selectedTransaction);
        }
        editBtn.setEnabled(!selectionEmpty && (selectedTransaction.getTransactionBase().getReconciliationState() != TransactionBase.ReconciliationState.RECONCILED));
        removeBtn.setEnabled(!selectionEmpty && (selectedTransaction.getTransactionBase().getReconciliationState() != TransactionBase.ReconciliationState.RECONCILED));
    }

    public void unselectTransaction() {
        transactionFormPanel.onUnselect();
    }

    public void setSelectedAccount(AccountController accountCtrl) {
        accountSelectorPanel.selectAccount(accountCtrl);
    }

    public SplitForm getSplitForm() {
        return splitPanelCard;
    }

    public void switchReconciliationStatus(TransactionBase transaction, int rowIndex) {
        switch(transactionTable.getTransactionTableModel().getStatus(transaction)) {
            case RECONCILED:
                break;
            case MARKED:
                transactionTable.getTransactionTableModel().setTransactionStatus(transaction, rowIndex, TransactionBase.ReconciliationState.UNRECONCILED);
                reconcileDataPanel.unmarkTransactionAmount(transaction.getAmount());
                break;
            case UNRECONCILED:
                transactionTable.getTransactionTableModel().setTransactionStatus(transaction, rowIndex, TransactionBase.ReconciliationState.MARKED);
                reconcileDataPanel.markTransactionAmount(transaction.getAmount());
                break;
        }
    }

    private org.paccman.ui.accountselector.AccountSelectorPanel accountSelectorPanel;

    private javax.swing.JButton cancelBtn;

    private javax.swing.JButton cancelReconcileBtn;

    private javax.swing.JButton editBtn;

    private javax.swing.JButton finishBtn;

    private javax.swing.JPanel finishCancelPanel;

    private javax.swing.JPanel formPanel;

    private javax.swing.JSplitPane jSplitPane1;

    private javax.swing.JPanel leftTransactionPanel;

    private javax.swing.JToolBar mainToolbar;

    private javax.swing.JButton newBtn;

    private javax.swing.JButton reconcileBtn;

    private org.paccman.ui.transactions.reconcile.ReconcilePanel reconcileDataPanel;

    private javax.swing.JPanel reconcilePanel;

    private javax.swing.JButton removeBtn;

    private javax.swing.JButton showHideBtn;

    private org.paccman.ui.transactions.split.SplitForm splitPanelCard;

    private javax.swing.JPanel toolbarPanel;

    private org.paccman.ui.transactions.TransactionFormPanel transactionFormPanel;

    private javax.swing.JPanel transactionPanelCard;

    private org.paccman.ui.transactions.table.TransactionTable transactionTable;

    private javax.swing.JScrollPane transactionTableScrollPane;

    private javax.swing.JPanel transactionsPanel;

    private javax.swing.JButton validateBtn;

    private javax.swing.JPanel validateCancelPanel;

    /**
     * Holds value of property expScheduledTransaction.
     */
    private org.paccman.ui.scheduling.ExpiredScheduledTransaction expScheduledTransaction;

    /**
     * Getter for property expScheduledTransaction.
     * 
     * @return Value of property expScheduledTransaction.
     */
    public org.paccman.ui.scheduling.ExpiredScheduledTransaction getScheduledTransaction() {
        return this.expScheduledTransaction;
    }

    /**
     * Setter for property expScheduledTransaction.
     * 
     * @param expScheduledTransaction New value of property expScheduledTransaction.
     */
    public void setScheduledTransaction(org.paccman.ui.scheduling.ExpiredScheduledTransaction scheduledTransaction) {
        this.expScheduledTransaction = scheduledTransaction;
    }
}
