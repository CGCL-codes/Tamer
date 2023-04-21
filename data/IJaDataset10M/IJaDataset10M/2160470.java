package com.pjsofts.eurobudget.beans;

import com.pjsofts.beans.AmountEditor;
import com.pjsofts.beans.DateEditor;
import com.pjsofts.eurobudget.DataModel;
import com.pjsofts.eurobudget.EBConstants;
import com.pjsofts.eurobudget.EuroBudget;
import com.pjsofts.eurobudget.data.TxnSearchParam;
import com.pjsofts.eurobudget.util.TxnFinder;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.beans.*;
import java.util.*;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;

/**
 * Customizer for a Transaction
 * Will update immediatly all changes on the given bean
 * @author  Standard
 */
public class TransactionCustomizer extends JPanel implements Customizer {

    /** its own map of categories, given by the data model (see DataModel for format )
     * Used to update sub categories list
     */
    private SortedMap categories;

    /** The account on which we are creating this transaction,
     * this is used only on complex transaction which need to know about it.
     * (Virement, Actions ...)
     * Also used by auto-entry ..
     */
    private Account account;

    /** flag to enable/disable bean changes, this allows to init gui without resetting the bean 
     * warning: this could breaks the rules that what is on screen is really bean's values.
     * but in the same time protects bean data from side-effects and bugs from gui initialization...
     */
    private boolean doBeanChanges = true;

    /** manager property listeners for this customizer */
    protected PropertyChangeSupport support = new PropertyChangeSupport(this);

    /** warning will stay null as long as setObject is not called */
    protected Transaction txnBean = null;

    /** date editor */
    private DateEditor dateEditor = new DateEditor(EBConstants.DATE_PARSER_LENIENT, true, false);

    /** amount editor */
    private AmountEditor amountEditor = new AmountEditor(EBConstants.NB_PARSER, true, false);

    /** focus cycle root for this container , date editor */
    public Component focusRoot = null;

    /** flag for auto-entry (when entering amount, other fields are completed with same value as latest similar transaction )
     * of transaction */
    protected static boolean doAutoEntry = true;

    /** flag for auto-completion of transaction :
     * when writting in some special field (to/from), some solutions by completion are proposed
     * (not yey implemented) */
    private static boolean doAutoCompletion = true;

    /** Creates a new instance of TransactionCustomizer, without category */
    public TransactionCustomizer() {
        initComponents();
        initTexts();
        setCategories(null);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        dateEditor.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                dateEditorChange(evt);
            }
        });
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        add(dateEditor.getCustomEditor(), gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        amountEditor.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                amountEditorChange(evt);
            }
        });
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        add(amountEditor.getCustomEditor(), gridBagConstraints);
        dateEditor.getCustomEditor().setFocusable(true);
        setFocusable(true);
        setFocusCycleRoot(false);
        this.focusRoot = dateEditor.getCustomEditor();
    }

    /** add a listener to any changes on this customizer.
     *  act as a proxy on the bean ..
     *  Changes are fired when customizer changes a bean value.
     * the source object will be the customizer
     */
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        support.addPropertyChangeListener(propertyChangeListener);
    }

    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        support.removePropertyChangeListener(propertyChangeListener);
    }

    /** 
     * Associate a bean with this customizer.
     * must reinitialize all gui components even if bean variables are null 
     * Enable/disable the customizer depending of transaction(virement,verified..).
     * Warning: side-effect, setting object to this customizer cause customizer to change this object value !!
     * (this allows to see quickly bugs on screen (value disappearing) and a lazy implementation, but is quite tricky)
     * Another implementation could call an initGuiField method that doesn't do & fire property change on bean (but this reclaims twice as more methods, or to remove action listeners before and readding them after)
     */
    public void setObject(Object obj) {
        this.txnBean = (Transaction) obj;
        if (txnBean != null) {
            this.setVisible(true);
            this.doBeanChanges = false;
            dateEditor.setValue(txnBean.getDate());
            amountEditor.setValue(new Double(txnBean.getAmount()));
            if (txnBean.getDetails() != null) jTextAreaDetails.setText(txnBean.getDetails()); else jTextAreaDetails.setText("");
            setCategoryPair(txnBean.getCategoryPair());
            StringBuffer sb = new StringBuffer("");
            if (txnBean.isSplitted()) {
                sb.append(i18n.getString("txn_flag_group")).append("  ");
            }
            if (txnBean.isVerified()) {
                sb.append(i18n.getString("Verified"));
            } else if (txnBean.isChecked()) {
                sb.append(i18n.getString("Checked"));
            } else {
                sb.append(i18n.getString("Not_Checked"));
            }
            flagsLabel.setText(sb.toString());
            if (txnBean.isVerified() || account.isFlagSet(Account.FLAG_ENDED)) {
                this.setEnabledAll(false);
            } else {
                this.setEnabledAll(true);
            }
            this.doBeanChanges = true;
        } else {
            this.doBeanChanges = false;
            resetComponents();
            this.setEnabledAll(false);
            this.setVisible(false);
            this.support = null;
        }
    }

    /** if no bean, then reset all gui component to sort of null or empty value */
    protected void resetComponents() {
        dateEditor.setValue(null);
        amountEditor.setValue(null);
        jComboCtg.setSelectedIndex(-1);
        jComboCtg2.setSelectedIndex(-1);
        jTextAreaDetails.setText("");
        flagsLabel.setText("");
    }

    /** 
     * update customizer with this value of category pair 
     * if SPECIAL CATEGORY, hide combo and put a label [category]
     */
    protected void setCategoryPair(CategoryPair pair) {
        if (pair == null) {
            jComboCtg.setVisible(true);
            jComboCtg2.setVisible(true);
            jLabelCtgSpecial.setVisible(false);
            jComboCtg.setSelectedIndex(-1);
            jComboCtg2.setSelectedIndex(-1);
        } else {
            if (pair.isSpecial()) {
                jComboCtg.setVisible(false);
                jComboCtg2.setVisible(false);
                jLabelCtgSpecial.setVisible(true);
                jLabelCtgSpecial.setText(pair.toString());
            } else {
                jComboCtg.setVisible(true);
                jComboCtg2.setVisible(true);
                jLabelCtgSpecial.setVisible(false);
                Category oldValueSubCategory = pair.getSub();
                if (pair != null && pair.getMain() != null) {
                    jComboCtg.setSelectedItem(pair.getMain());
                } else {
                    jComboCtg.setSelectedItem(Category.CATEGORY_NULL);
                }
                if (pair != null && oldValueSubCategory != null) {
                    jComboCtg2.setSelectedItem(oldValueSubCategory);
                } else {
                    jComboCtg2.setSelectedItem(Category.CATEGORY_NULL);
                }
            }
        }
    }

    /** use carefully */
    public Transaction getObject() {
        return this.txnBean;
    }

    /** synch with a data model change , try to keep selection ...
     * @param may help to update only part of data
     */
    public void updateDataModel(DataModel dm, Object arg) {
        if (arg == null || arg == Category.class) {
        }
        if (arg == null || arg == Entity.class) {
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        jLabelDate = new javax.swing.JLabel();
        jLabelAmount = new javax.swing.JLabel();
        jLabelDetails = new javax.swing.JLabel();
        jLabelCtg = new javax.swing.JLabel();
        flagsLabel = new javax.swing.JLabel();
        jLabelCtgSpecial = new javax.swing.JLabel();
        detailsScrollPane = new javax.swing.JScrollPane();
        jTextAreaDetails = new javax.swing.JTextArea();
        ctgPanel = new javax.swing.JPanel();
        jComboCtg = new javax.swing.JComboBox();
        jComboCtg2 = new javax.swing.JComboBox();
        setLayout(new java.awt.GridBagLayout());
        jLabelDate.setText("Date");
        jLabelDate.setDisplayedMnemonicIndex(0);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabelDate, gridBagConstraints);
        jLabelAmount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelAmount.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jLabelAmount, gridBagConstraints);
        jLabelDetails.setText("Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabelDetails, gridBagConstraints);
        jLabelCtg.setText("Category");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabelCtg, gridBagConstraints);
        flagsLabel.setText("Flags Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(flagsLabel, gridBagConstraints);
        jLabelCtgSpecial.setText("jLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(jLabelCtgSpecial, gridBagConstraints);
        jTextAreaDetails.setLineWrap(true);
        jTextAreaDetails.setRows(3);
        jTextAreaDetails.setTabSize(1);
        jTextAreaDetails.setWrapStyleWord(true);
        jTextAreaDetails.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextAreaDetailsFocusLost(evt);
            }
        });
        detailsScrollPane.setViewportView(jTextAreaDetails);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        add(detailsScrollPane, gridBagConstraints);
        ctgPanel.setLayout(new java.awt.GridLayout(1, 2));
        jComboCtg.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboCtgActionPerformed(evt);
            }
        });
        jComboCtg.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                jComboCtgFocusLost(evt);
            }
        });
        ctgPanel.add(jComboCtg);
        jComboCtg2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboCtg2ActionPerformed(evt);
            }
        });
        jComboCtg2.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                jComboCtg2FocusLost(evt);
            }
        });
        ctgPanel.add(jComboCtg2);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        add(ctgPanel, gridBagConstraints);
    }

    private void initTexts() {
        jLabelDate.setText(i18n.getString("Date"));
        jLabelAmount.setText(i18n.getString("Amount"));
        jLabelDetails.setText(i18n.getString("Details"));
        jLabelCtg.setText(i18n.getString("Category"));
        flagsLabel.setText(i18n.getString("Flags_Value"));
    }

    /** t means todays, +/-/up/down slides date,PageUp/Down slides month */
    private void jComboCtg2FocusLost(java.awt.event.FocusEvent evt) {
        jComboCtg2ActionPerformed(null);
    }

    private void jTextAreaDetailsFocusLost(java.awt.event.FocusEvent evt) {
        if (this.txnBean != null && doBeanChanges) {
            String oldValue = this.txnBean.getDetails();
            String newValue = jTextAreaDetails.getText();
            if (newValue == null || !newValue.equals(oldValue)) {
                this.txnBean.setDetails(newValue);
                support.firePropertyChange(Transaction.PROPERTY_DETAILS, oldValue, newValue);
            }
        }
    }

    /** */
    private void dateEditorChange(PropertyChangeEvent evt) {
        if (this.txnBean != null && doBeanChanges) {
            Date oldValue = txnBean != null ? txnBean.getDate() : null;
            Date newValue = (Date) dateEditor.getValue();
            if (newValue == null || !newValue.equals(oldValue)) {
                this.txnBean.setDate(newValue);
                support.firePropertyChange(Transaction.PROPERTY_DATE, oldValue, newValue);
            }
        }
    }

    private void amountEditorChange(PropertyChangeEvent evt) {
        if (this.txnBean != null && doBeanChanges) {
            Double oldValue = new Double(this.txnBean.getAmount());
            Number newValue = ((Number) amountEditor.getValue());
            if (newValue == null || !newValue.equals(oldValue)) {
                this.txnBean.setAmount(newValue.doubleValue());
                support.firePropertyChange(Transaction.PROPERTY_AMOUNT, oldValue, newValue);
            }
            if (doAutoEntry && newValue != null && isAutoEntryFromAmountPossible()) {
                doAutoEntryFromAmount();
            }
        }
    }

    /**
     * may be overriden (should be) by all subclasses
     */
    protected boolean isAutoEntryFromAmountPossible() {
        boolean result = txnBean.getCategoryPair().isNull();
        return result;
    }

    /**
     * implement auto entry from amount
     * may be overriden (should be) by all subclasses
     */
    protected void doAutoEntryFromAmount() {
        TxnSearchParam searchParam = new TxnSearchParam();
        searchParam.amount = new Double(txnBean.getAmount());
        searchParam.types = new Class[1];
        searchParam.types[0] = txnBean.getClass();
        Transaction lastSimTxn = TxnFinder.findLast(getAccount(), searchParam, txnBean);
        if (lastSimTxn != null) {
            EuroBudget.getInstance().showStatus(i18n.getString("message_autoentry_possible"));
            txnBean.copyAutoEntry(lastSimTxn);
            this.setObject(txnBean);
        }
    }

    /**
     * may be overriden (should be) by all subclasses
     */
    protected boolean isAutoEntryFromDirectionPossible() {
        boolean result = txnBean.getCategoryPair().isNull() && txnBean.getAmount() == 0d;
        return result;
    }

    /**
     * implement auto entry from amount
     * may be overriden (should be) by all subclasses
     */
    protected void doAutoEntryFromDirection() {
        TxnSearchParam searchParam = new TxnSearchParam();
        assert this.txnBean.getEntity() != null;
        searchParam.entities = new Entity[1];
        searchParam.entities[0] = this.txnBean.getEntity();
        searchParam.withEntity = Boolean.TRUE;
        searchParam.types = new Class[1];
        searchParam.types[0] = txnBean.getClass();
        Transaction lastSimTxn = TxnFinder.findLast(getAccount(), searchParam, txnBean);
        if (lastSimTxn != null) {
            EuroBudget.getInstance().showStatus(i18n.getString("message_autoentry_possible"));
            txnBean.copyAutoEntry(lastSimTxn);
            this.setObject(txnBean);
        }
    }

    private void jComboCtgFocusLost(java.awt.event.FocusEvent evt) {
    }

    private void jComboCtg2ActionPerformed(java.awt.event.ActionEvent evt) {
        if (this.txnBean != null && doBeanChanges) {
            Category oldValue = txnBean.getCategoryPair().getSub();
            Category newValue = null;
            newValue = (Category) jComboCtg2.getSelectedItem();
            if (newValue == Category.CATEGORY_NULL) newValue = null;
            if (newValue == null || !newValue.equals(oldValue)) {
                this.txnBean.getCategoryPair().setSub(newValue);
                support.firePropertyChange(Transaction.PROPERTY_CATEGORY, oldValue, newValue);
            }
        }
    }

    private void jComboCtgActionPerformed(java.awt.event.ActionEvent evt) {
        Category oldValue = null;
        Category newValue = null;
        Object obj = jComboCtg.getSelectedItem();
        if (obj != null) {
            oldValue = txnBean != null ? txnBean.getCategoryPair().getMain() : null;
            if (obj instanceof Category) {
                Category main = (Category) obj;
                updateSubCategoriesList(main);
                newValue = main;
            } else if (obj instanceof String) {
                String s = (String) obj;
                System.out.println(i18n.getString("error") + obj + " class:" + obj.getClass());
                newValue = null;
            } else {
                System.out.println(i18n.getString("error") + obj + " class:" + obj.getClass());
                newValue = null;
            }
        } else {
            jComboCtg2.removeAllItems();
            newValue = null;
        }
        if (this.txnBean != null && doBeanChanges) {
            if (newValue == null || !newValue.equals(oldValue)) {
                this.txnBean.getCategoryPair().setMain(newValue);
                support.firePropertyChange(Transaction.PROPERTY_CATEGORY, oldValue, newValue);
            }
        }
    }

    /** fill the list of sub categories; select none (and put txn sub cat to null!!)*/
    public void updateSubCategoriesList(Category main) {
        if (main == Category.CATEGORY_NULL) {
            jComboCtg2.removeAllItems();
        } else {
            Collection aList = (Collection) this.categories.get(main);
            if (aList != null) {
                DefaultComboBoxModel cmodel = new DefaultComboBoxModel(aList.toArray());
                cmodel.insertElementAt(Category.CATEGORY_NULL, 0);
                jComboCtg2.setModel(cmodel);
                jComboCtg2.setSelectedIndex(0);
            } else {
                jComboCtg2.removeAllItems();
            }
        }
        if (this.txnBean != null && doBeanChanges) {
            this.txnBean.getCategoryPair().setSub(null);
        }
    }

    /** Getter for property categories.
     * @return Value of property categories.
     */
    public SortedMap getCategories() {
        return categories;
    }

    /** Setter for property categories.
     * @param categories New value of property categories.
     * called once during customizer init (see TxnEditor.initCustomizer)
     */
    public void setCategories(SortedMap categories) {
        if (categories == null) categories = new TreeMap();
        this.categories = categories;
        DefaultComboBoxModel cmodel = new DefaultComboBoxModel(this.categories.keySet().toArray());
        cmodel.insertElementAt(Category.CATEGORY_NULL, 0);
        jComboCtg.setModel(cmodel);
    }

    /** Getter for property account.
     * @return Value of property account.
     */
    public com.pjsofts.eurobudget.beans.Account getAccount() {
        return account;
    }

    /** Setter for property account.
     * @param account New value of property account.
     */
    public void setAccount(com.pjsofts.eurobudget.beans.Account account) {
        this.account = account;
    }

    /** enable or disable all components */
    public void setEnabledAll(boolean b) {
        Component[] items = getComponents();
        for (int i = 0; i < items.length; i++) {
            items[i].setEnabled(b);
        }
        jTextAreaDetails.setEnabled(b);
    }

    /** disabled all category Combo */
    protected void setCategoryEnable(boolean b) {
        jLabelCtg.setEnabled(b);
        jComboCtg.setEnabled(b);
        jComboCtg2.setEnabled(b);
    }

    private javax.swing.JLabel jLabelCtgSpecial;

    private javax.swing.JLabel jLabelDate;

    private javax.swing.JScrollPane detailsScrollPane;

    private javax.swing.JLabel jLabelDetails;

    private javax.swing.JLabel jLabelCtg;

    private javax.swing.JLabel jLabelAmount;

    private javax.swing.JLabel flagsLabel;

    private javax.swing.JComboBox jComboCtg2;

    private javax.swing.JPanel ctgPanel;

    private javax.swing.JComboBox jComboCtg;

    private javax.swing.JTextArea jTextAreaDetails;

    private static final ResourceBundle i18n = EBConstants.i18n;
}
