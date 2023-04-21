package jgnash.ui.register.invest;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import jgnash.engine.AbstractInvestmentTransactionEntry;
import jgnash.engine.Account;
import jgnash.engine.CurrencyNode;
import jgnash.engine.Engine;
import jgnash.engine.EngineFactory;
import jgnash.engine.InvestmentTransaction;
import jgnash.engine.ReconciledState;
import jgnash.engine.Transaction;
import jgnash.engine.TransactionEntry;
import jgnash.engine.TransactionEntryBuyX;
import jgnash.engine.TransactionEntrySellX;
import jgnash.engine.TransactionFactory;
import jgnash.engine.TransactionTag;
import jgnash.engine.TransactionType;
import jgnash.text.CommodityFormat;
import jgnash.ui.components.AccountListComboBox;
import jgnash.ui.components.AccountSecurityComboBox;
import jgnash.ui.components.AutoCompleteFactory;
import jgnash.ui.components.DatePanel;
import jgnash.ui.components.JFloatField;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/** Form for buying and selling shares.
 * 
 * @author Craig Cavanaugh
 * 
 * $Id: BuySellSharePanel.java,v 1.9 2007/12/04 00:59:47 ccavanaugh Exp $
 */
public class BuySellSharePanel extends AbstractInvTransactionPanel implements ActionListener {

    private TransactionType tranType;

    private DatePanel datePanel;

    private JFloatField priceField;

    private JFloatField quantityField;

    private JFloatField feeField;

    private JTextField memoField;

    private JFloatField totalField;

    private AccountSecurityComboBox securityCombo;

    private AccountListComboBox accountCombo;

    private JFloatField rateField;

    private JLabel rateLabel;

    private JLabel conversionLabel;

    private Logger logger = Logger.getLogger(BuySellSharePanel.class.getName());

    public BuySellSharePanel(Account account, TransactionType tranType) {
        super(account);
        this.tranType = tranType;
        if ((tranType != TransactionType.BUYSHARE) && (tranType != TransactionType.SELLSHARE)) {
            throw new IllegalArgumentException("bad tranType");
        }
        datePanel = new DatePanel();
        feeField = new JFloatField(account.getCurrencyNode());
        memoField = AutoCompleteFactory.getMemoField();
        priceField = new JFloatField(0, 6, account.getCurrencyNode().getScale());
        quantityField = new JFloatField(0, 6, 2);
        totalField = new JFloatField(account.getCurrencyNode());
        totalField.setEditable(false);
        totalField.setFocusable(false);
        securityCombo = new AccountSecurityComboBox(account);
        accountCombo = new AccountListComboBox();
        accountCombo.setSelectedAccount(account);
        reconciledButton = new JCheckBox(rb.getString("Button.Reconciled"));
        conversionLabel = new JLabel();
        rateField = new JFloatField(0, 6, 2);
        rateField.setEnabled(false);
        rateLabel = new JLabel(rb.getString("Label.ExchangeRate"));
        rateLabel.setEnabled(false);
        layoutMainPanel();
        FocusListener focusListener = new FocusAdapter() {

            public void focusLost(FocusEvent evt) {
                updateTotalField();
            }
        };
        feeField.addFocusListener(focusListener);
        quantityField.addFocusListener(focusListener);
        priceField.addFocusListener(focusListener);
        datePanel.getDateField().addKeyListener(keyListener);
        feeField.addKeyListener(keyListener);
        memoField.addKeyListener(keyListener);
        priceField.addKeyListener(keyListener);
        quantityField.addKeyListener(keyListener);
        securityCombo.addKeyListener(keyListener);
        accountCombo.addKeyListener(keyListener);
        reconciledButton.addKeyListener(keyListener);
        accountCombo.addActionListener(this);
        clearForm();
    }

    private void layoutMainPanel() {
        FormLayout layout = new FormLayout("right:d, 4dlu, 50dlu:g, 8dlu, right:d, 4dlu, max(48dlu;min)", "f:d, 3dlu, f:d, 3dlu, f:d, 3dlu, f:d, 3dlu, f:d");
        layout.setRowGroups(new int[][] { { 1, 3, 5, 7, 9 } });
        CellConstraints cc = new CellConstraints();
        setLayout(layout);
        JPanel subPanel = buildHorizontalSubPanel("max(48dlu;min):g(0.5), 8dlu, d, 4dlu, max(48dlu;min):g(0.5)", new Object[] { priceField, "Label.Quantity", quantityField });
        add("Label.Security", cc.xy(1, 1));
        add(securityCombo, cc.xy(3, 1));
        add("Label.Date", cc.xy(5, 1));
        add(datePanel, cc.xy(7, 1));
        add("Label.Price", cc.xy(1, 3));
        add(subPanel, cc.xy(3, 3));
        add("Label.Fees", cc.xy(5, 3));
        add(feeField, cc.xy(7, 3));
        add("Label.Memo", cc.xy(1, 5));
        add(memoField, cc.xy(3, 5));
        add("Label.Total", cc.xy(5, 5));
        add(totalField, cc.xy(7, 5));
        add("Label.Account", cc.xy(1, 7));
        add(accountCombo, cc.xy(3, 7));
        add(reconciledButton, cc.xyw(5, 7, 3));
        add(createBottomPanel(), cc.xyw(1, 9, 5));
    }

    protected JPanel createBottomPanel() {
        FormLayout layout = new FormLayout("m, 4dlu, max(48dlu;min), 4dlu, left:max(1dlu;min), m:g", "f:d");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.append(rateLabel, rateField, conversionLabel);
        return builder.getPanel();
    }

    void updateTotalField() {
        BigDecimal fee = feeField.getDecimal();
        BigDecimal quantity = quantityField.getDecimal();
        BigDecimal price = priceField.getDecimal();
        BigDecimal value = quantity.multiply(price);
        if (tranType == TransactionType.BUYSHARE) {
            value = value.add(fee);
        } else {
            value = value.subtract(fee);
        }
        totalField.setDecimal(value);
    }

    public void modifyTransaction(Transaction tran) {
        if (!(tran instanceof InvestmentTransaction)) {
            throw new IllegalArgumentException("bad tranType");
        }
        clearForm();
        datePanel.setDate(tran.getDate());
        List<TransactionEntry> entries = tran.getTransactionEntries();
        assert entries.size() <= 2;
        for (TransactionEntry e : entries) {
            if (e.getTransactionTag() == TransactionTag.INVESTMENT_FEE) {
                feeField.setDecimal(e.getAmount(account).abs());
                updateTotalField();
            } else if (e instanceof TransactionEntryBuyX || e instanceof TransactionEntrySellX) {
                AbstractInvestmentTransactionEntry entry = (AbstractInvestmentTransactionEntry) e;
                memoField.setText(e.getMemo());
                priceField.setDecimal(entry.getPrice());
                quantityField.setDecimal(entry.getQuantity());
                securityCombo.setSelectedNode(entry.getSecurityNode());
                if (entry.getCreditAccount().equals(account)) {
                    accountCombo.setSelectedAccount(entry.getDebitAccount());
                } else {
                    accountCombo.setSelectedAccount(entry.getCreditAccount());
                }
                updateTotalField();
            } else {
                logger.warning("Invalid transaction");
            }
        }
        modTrans = tran;
        reconciledButton.setSelected(tran.getReconciled(getAccount()) == ReconciledState.RECONCILED);
        updateExchangeField();
    }

    public Transaction buildTransaction() {
        BigDecimal exchange = BigDecimal.ONE;
        if (rateField.isEnabled()) {
            exchange = rateField.getDecimal();
        }
        if (tranType == TransactionType.BUYSHARE) {
            return TransactionFactory.generateBuyXTransaction(accountCombo.getSelectedAccount(), getAccount(), securityCombo.getSelectedNode(), priceField.getDecimal(), quantityField.getDecimal(), exchange, feeField.getDecimal(), datePanel.getDate(), memoField.getText(), reconciledButton.isSelected());
        }
        return TransactionFactory.generateSellXTransaction(accountCombo.getSelectedAccount(), getAccount(), securityCombo.getSelectedNode(), priceField.getDecimal(), quantityField.getDecimal(), exchange, feeField.getDecimal(), datePanel.getDate(), memoField.getText(), reconciledButton.isSelected());
    }

    public void clearForm() {
        modTrans = null;
        if (!getRememberLastDate()) {
            datePanel.setDate(new Date());
        }
        memoField.setText(null);
        priceField.setDecimal(null);
        quantityField.setDecimal(null);
        feeField.setDecimal(null);
        reconciledButton.setSelected(false);
        updateTotalField();
        updateExchangeField();
    }

    public boolean validateForm() {
        if (priceField.getText().equals("") || quantityField.getText().equals("")) {
            return false;
        }
        return true;
    }

    @Override
    protected void cancelAction() {
        clearForm();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == accountCombo) {
            updateExchangeField();
        }
    }

    protected void disableExchangeField() {
        rateLabel.setEnabled(false);
        rateField.setDecimal(null);
        rateField.setEnabled(false);
        conversionLabel.setText(null);
    }

    /**
     * updateExchangeField() is called when a change is made to the account list 
     * except for updates caused by account additions or removals
     */
    protected void updateExchangeField() {
        Engine engine = EngineFactory.getEngine();
        Account tAccount = accountCombo.getSelectedAccount();
        if (tAccount != null) {
            CurrencyNode baseCommodity = getAccount().getCurrencyNode();
            if (tAccount.getCurrencyNode() != baseCommodity) {
                rateLabel.setEnabled(true);
                rateField.setEnabled(true);
                if (modTrans == null) {
                    rateField.setDecimal(engine.getExchangeRate(baseCommodity, tAccount.getCurrencyNode()));
                } else {
                    for (TransactionEntry e : modTrans.getTransactionEntries()) {
                        if (e instanceof TransactionEntryBuyX || e instanceof TransactionEntrySellX) {
                            AbstractInvestmentTransactionEntry entry = (AbstractInvestmentTransactionEntry) e;
                            if (entry.getCreditAccount().equals(account)) {
                                rateField.setDecimal(entry.getExchangeRate(tAccount, account));
                            } else {
                                rateField.setDecimal(entry.getExchangeRate(account, tAccount));
                            }
                            break;
                        }
                    }
                }
                setConversionLabel(baseCommodity, tAccount.getCurrencyNode());
            } else {
                disableExchangeField();
            }
        }
    }

    protected final void setConversionLabel(CurrencyNode baseCurr, CurrencyNode curr) {
        conversionLabel.setText(CommodityFormat.getConversion(baseCurr, curr));
    }
}
