package jgnash.ui.register.invest;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import jgnash.engine.*;
import jgnash.engine.commodity.CommodityNode;
import jgnash.text.CommodityFormat;
import jgnash.ui.components.*;
import jgnash.ui.register.SplitsDialog;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.*;

/** Handles transfers between accounts.
 * <p>
 * $Id: TransferPanel.java 675 2008-06-17 01:36:01Z ccavanaugh $
 * 
 * @author Craig Cavanaugh
 * @author Don Brown
 */
public class TransferPanel extends AbstractInvTransactionPanel implements ActionListener {

    private int parentTransactionID = 0;

    private Account creditAccount;

    private Account debitAccount;

    private CommodityNode baseCommodity;

    private CommodityNode commodity;

    private List splits = null;

    private byte tranType;

    private JCheckBox reconciledButton;

    private JLabel rateLabel;

    private JLabel conversionLabel;

    private JButton splitsButton;

    private JFloatField amountField;

    private DatePanel datePanel;

    private JTextField memoField;

    private JTextField payeeField;

    private JFloatField rateField;

    private AccountListComboBox accountCombo;

    private TransactionNumberComboBox numberField;

    public TransferPanel(Account account, byte type) {
        super(account);
        baseCommodity = account.getCommodityNode();
        tranType = type;
        amountField = new JFloatField(account.getCommodityNode());
        datePanel = new DatePanel();
        accountCombo = new AccountListComboBox(account);
        payeeField = AutoCompleteFactory.getPayeeField(account);
        memoField = AutoCompleteFactory.getMemoField();
        numberField = new TransactionNumberComboBox(account);
        rateField = new JFloatField(0, 6, 2);
        rateLabel = new JLabel(rb.getString("Label.ExchangeRate"));
        splitsButton = new JButton(rb.getString("Button.Splits"));
        reconciledButton = new JCheckBox(rb.getString("Button.Reconciled"));
        conversionLabel = new JLabel();
        layoutMainPanel();
        accountCombo.addActionListener(this);
        splitsButton.addActionListener(this);
        amountField.addKeyListener(keyListener);
        datePanel.getDateField().addKeyListener(keyListener);
        accountCombo.addKeyListener(keyListener);
        payeeField.addKeyListener(keyListener);
        memoField.addKeyListener(keyListener);
        numberField.addKeyListener(keyListener);
        rateField.addKeyListener(keyListener);
        clearForm();
    }

    private void layoutMainPanel() {
        FormLayout layout = new FormLayout("right:d, 4dlu, 50dlu:g, 4dlu, d, 8dlu, right:d, 4dlu, max(48dlu;min)", "f:d, 3dlu, f:d, 3dlu, f:d, 3dlu, f:d");
        layout.setRowGroups(new int[][] { { 1, 3, 5, 7 } });
        CellConstraints cc = new CellConstraints();
        setLayout(layout);
        add("Label.Payee", cc.xy(1, 1));
        add(payeeField, cc.xywh(3, 1, 3, 1));
        add("Label.Number", cc.xy(7, 1));
        add(numberField, cc.xy(9, 1));
        if (tranType == DEBIT) {
            add("Label.TransferTo", cc.xy(1, 3));
        } else {
            add("Label.TransferFrom", cc.xy(1, 3));
        }
        add(accountCombo, cc.xy(3, 3));
        add(splitsButton, cc.xy(5, 3));
        add("Label.Date", cc.xy(7, 3));
        add(datePanel, cc.xy(9, 3));
        add("Label.Memo", cc.xy(1, 5));
        add(memoField, cc.xywh(3, 5, 3, 1));
        add("Label.Amount", cc.xy(7, 5));
        add(amountField, cc.xy(9, 5));
        add(createBottomPanel(), cc.xywh(1, 7, 9, 1));
    }

    private JPanel createBottomPanel() {
        FormLayout layout = new FormLayout("p, 8dlu, p, 4dlu, 45dlu, 4dlu, left:45dlu", "");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.append(reconciledButton, rateLabel, rateField);
        builder.append(conversionLabel);
        return builder.getPanel();
    }

    public void accountAction() {
        displayCurrency();
    }

    /**
     * displayCurrency gets called when a change is made to the account list except for updates
     * caused by account additions or removals
     */
    private void displayCurrency() {
        Account tAccount = accountCombo.getSelectedAccount();
        if (tAccount != null) {
            if (tAccount.getCommodityNode() != baseCommodity && splits == null) {
                setCurrencyEnabled(true);
                if (modTrans == null) {
                    rateField.setDecimal(engine.getExchangeRate(baseCommodity, tAccount.getCommodityNode()));
                }
                if (commodity.equals(baseCommodity)) {
                    setConversionLabel(baseCommodity, tAccount.getCommodityNode());
                } else {
                    setConversionLabel(tAccount.getCommodityNode(), baseCommodity);
                }
            } else {
                setCurrencyEnabled(false);
            }
        }
    }

    private void setCurrencyEnabled(boolean enabled) {
        if (!enabled) {
            rateField.setDecimal(null);
            conversionLabel.setText("");
        }
        rateField.setEnabled(enabled);
        rateLabel.setEnabled(enabled);
    }

    private void setConversionLabel(CommodityNode baseCurr, CommodityNode curr) {
        conversionLabel.setText(CommodityFormat.getConversion(baseCurr, curr));
    }

    public void splitsAction() {
        SplitsDialog dlg = SplitsDialog.getSplitsDialog(this, account, splits, tranType);
        dlg.setVisible(true);
        if (dlg.returnStatus) {
            if (dlg.getSplits().size() > 0) {
                accountCombo.setEnabled(false);
                amountField.setDecimal(dlg.getBalance().abs());
                amountField.setEnabled(false);
                setCurrencyEnabled(false);
            } else {
                accountCombo.setEnabled(true);
                amountField.setEnabled(true);
            }
            splits = dlg.getSplits();
        }
    }

    public void modifyTransaction(Transaction t) {
        clearForm();
        modTrans = t;
        commodity = t.getCommodityNode();
        splits = null;
        amountField.setDecimal(t.getAmount().abs());
        memoField.setText(t.getMemo());
        payeeField.setText(t.getPayee());
        numberField.setText(t.getNumber());
        datePanel.setDate(t.getDate());
        reconciledButton.setSelected(t.isReconciled(account));
        if (t instanceof SplitTransaction) {
            SplitTransaction _t = (SplitTransaction) t;
            accountCombo.setEnabled(false);
            amountField.setEnabled(false);
            int count = _t.getSplitCount();
            splits = new ArrayList(count);
            for (int i = 0; i < count; i++) {
                splits.add(_t.getSplitAt(i).clone());
            }
            displayCurrency();
        } else if (t instanceof SplitEntryTransaction) {
            SplitEntryTransaction _t = (SplitEntryTransaction) t;
            parentTransactionID = _t.getParentTransactionID();
            creditAccount = _t.getCreditAccount();
            debitAccount = _t.getDebitAccount();
            splitsButton.setEnabled(false);
            accountCombo.setEnabled(false);
            datePanel.getDateField().setEnabled(false);
            numberField.setEnabled(false);
            displayCurrency();
        } else {
            DoubleEntryTransaction _t = (DoubleEntryTransaction) t;
            accountCombo.setEnabled(true);
            amountField.setEnabled(true);
            if (tranType == DEBIT) {
                accountCombo.setSelectedAccount(_t.getCreditAccount());
            } else {
                accountCombo.setSelectedAccount(_t.getDebitAccount());
            }
        }
        if (t instanceof DoubleEntryTransaction) {
            rateField.setDecimal(((DoubleEntryTransaction) t).getExchangeRate());
        }
    }

    public Transaction buildTransaction() {
        Transaction transaction;
        if (splits != null) {
            transaction = new SplitTransaction(commodity);
            ((SplitTransaction) transaction).addSplits(splits);
            ((SplitTransaction) transaction).setAccount(account);
        } else {
            if (parentTransactionID != 0) {
                transaction = new SplitEntryTransaction(commodity);
                SplitEntryTransaction _t = (SplitEntryTransaction) transaction;
                _t.setParentTransactionID(parentTransactionID);
                _t.setCreditAccount(creditAccount);
                _t.setDebitAccount(debitAccount);
            } else {
                transaction = new DoubleEntryTransaction(commodity);
                DoubleEntryTransaction _t = (DoubleEntryTransaction) transaction;
                int signum = amountField.getDecimal().signum();
                if ((tranType == DEBIT && signum >= 0) || (tranType == CREDIT && signum == -1)) {
                    _t.setCreditAccount(accountCombo.getSelectedAccount());
                    _t.setDebitAccount(account);
                } else {
                    _t.setCreditAccount(account);
                    _t.setDebitAccount(accountCombo.getSelectedAccount());
                }
            }
            if (modTrans instanceof DoubleEntryTransaction) {
                ((DoubleEntryTransaction) transaction).setReconciled(((DoubleEntryTransaction) modTrans).getCreditAccount(), modTrans.isReconciled(((DoubleEntryTransaction) modTrans).getCreditAccount()));
                ((DoubleEntryTransaction) transaction).setReconciled(((DoubleEntryTransaction) modTrans).getDebitAccount(), modTrans.isReconciled(((DoubleEntryTransaction) modTrans).getDebitAccount()));
            }
            if (rateField.isEnabled()) {
                ((DoubleEntryTransaction) transaction).setExchangeRate(rateField.getDecimal());
            }
            transaction.setAmount(amountField.getDecimal().abs());
        }
        transaction.setDate(datePanel.getDate());
        transaction.setMemo(memoField.getText());
        transaction.setNumber(numberField.getText());
        transaction.setPayee(payeeField.getText());
        reconcileTransaction(transaction, reconciledButton.isSelected());
        return transaction;
    }

    public boolean validateForm() {
        if (amountField.getText().equals("")) {
            return false;
        }
        return true;
    }

    public void clearForm() {
        modTrans = null;
        parentTransactionID = 0;
        creditAccount = null;
        debitAccount = null;
        commodity = baseCommodity;
        splits = null;
        amountField.setEnabled(true);
        accountCombo.setEnabled(true);
        splitsButton.setEnabled(true);
        datePanel.getDateField().setEnabled(true);
        numberField.setEnabled(true);
        if (!getRememberLastDate()) {
            datePanel.setDate(new Date());
        }
        memoField.setText(null);
        amountField.setDecimal(null);
        payeeField.setText(null);
        numberField.setText(null);
        reconciledButton.setSelected(false);
        displayCurrency();
    }

    /** Invoked when an action occurs. */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == accountCombo) {
            accountAction();
        } else if (e.getSource() == splitsButton) {
            splitsAction();
        }
    }

    public static final byte CREDIT = 1;

    public static final byte DEBIT = 2;
}
