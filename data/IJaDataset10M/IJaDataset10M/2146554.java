package com.wateray.ipassbook.ui.dialog.wizard;

import java.awt.Dimension;
import java.awt.Insets;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import com.wateray.ipassbook.commom.CommomTool;
import com.wateray.ipassbook.domain.Currency;
import com.wateray.ipassbook.domain.Entity;
import com.wateray.ipassbook.domain.form.PassbookForm;
import com.wateray.ipassbook.kernel.service.CurrencyService;
import com.wateray.ipassbook.ui.model.PassbookComboBoxModel;
import com.wateray.ipassbook.util.CalendarUtils;
import com.wateray.ipassbook.util.LanguageLoader;

/**
 * @author wateray
 * @create 2009-5-16
 */
public class PassbookWizardPageTwo extends AbstractWizardPage {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private JLabel initDataLabel;

    private JTextField initDataText;

    private JLabel initAmountLabel;

    private JTextField initAmountText;

    private JLabel currencyLabel;

    private JComboBox currencyComboBox;

    private PassbookComboBoxModel currencyComboxMode;

    private JLabel describeLabel;

    public PassbookWizardPageTwo(IWizardDialog wizardDialog) {
        super(wizardDialog);
        initPage();
        initState();
    }

    protected void initPage() {
        super.initPage();
        constraints.insets = new Insets(15, left, bottom, right);
        constraints.gridy = ++gridy;
        this.add(getInitDataLabel(), constraints);
        constraints.gridx = 1;
        this.add(getInitDataText(), constraints);
        constraints.gridx = 2;
        constraints.weightx = 1.0;
        this.add(new JLabel(), constraints);
        constraints.weightx = 0.0;
        constraints.insets = inserts;
        constraints.gridx = 0;
        constraints.gridy = ++gridy;
        this.add(getInitAmountLabel(), constraints);
        constraints.gridx = 1;
        this.add(getInitAmountText(), constraints);
        constraints.gridx = 0;
        constraints.gridy = ++gridy;
        this.add(getCurrencyLabel(), constraints);
        constraints.gridx = 1;
        this.add(getCurrencyComboBox(), constraints);
        constraints.insets = new Insets(60, left, bottom, right);
        constraints.weighty = 1.0;
        constraints.gridwidth = 3;
        constraints.gridx = 0;
        constraints.gridy = ++gridy;
        this.add(getDescribeLabel(), constraints);
        constraints.weighty = 0.0;
    }

    private void initState() {
        getInitDataText().requestFocusInWindow();
    }

    /**
	 * @return the initDataLabel
	 */
    private JLabel getInitDataLabel() {
        if (initDataLabel == null) {
            initDataLabel = new JLabel(LanguageLoader.getString("Init_Data"));
        }
        return initDataLabel;
    }

    /**
	 * @return the initDataText
	 */
    private JTextField getInitDataText() {
        if (initDataText == null) {
            initDataText = new JTextField();
            initDataText.setPreferredSize(new Dimension(150, initDataText.getPreferredSize().height));
            initDataText.setText(CalendarUtils.getTodayInstance().getBeginDay());
        }
        return initDataText;
    }

    /**
	 * @return the initAmountLabel
	 */
    private JLabel getInitAmountLabel() {
        if (initAmountLabel == null) {
            initAmountLabel = new JLabel(LanguageLoader.getString("Init_Amount"));
        }
        return initAmountLabel;
    }

    /**
	 * @return the initAmountText
	 */
    private JTextField getInitAmountText() {
        if (initAmountText == null) {
            initAmountText = new JTextField();
            initAmountText.setHorizontalAlignment(JTextField.RIGHT);
        }
        return initAmountText;
    }

    /**
	 * @return the currencyLabel
	 */
    private JLabel getCurrencyLabel() {
        if (currencyLabel == null) {
            currencyLabel = new JLabel(LanguageLoader.getString("Currency"));
        }
        return currencyLabel;
    }

    /**
	 * @return the currencyComboBox
	 */
    private JComboBox getCurrencyComboBox() {
        if (currencyComboBox == null) {
            currencyComboBox = new JComboBox();
            currencyComboxMode = new PassbookComboBoxModel();
            CurrencyService service = new CurrencyService();
            List<Currency> list = service.getCurrencyList();
            Vector<Entity> v = new Vector<Entity>();
            Iterator<Currency> it = list.iterator();
            while (it.hasNext()) {
                v.add((Entity) it.next());
            }
            currencyComboxMode.setData(v);
            currencyComboBox.setModel(currencyComboxMode);
        }
        return currencyComboBox;
    }

    private JLabel getDescribeLabel() {
        if (describeLabel == null) {
            describeLabel = new JLabel(LanguageLoader.getString("PassbookWizardPageTwoDescribe"));
        }
        return describeLabel;
    }

    @Override
    public String checkData() {
        String errorMessage = "";
        logger.debug(this.getClass().getSimpleName() + " is invoked.");
        String initDataText = getInitDataText().getText();
        if (initDataText == null || initDataText.trim().equals("")) {
            errorMessage = MessageFormat.format(LanguageLoader.getString("empty"), getInitDataLabel().getText());
            getInitDataText().requestFocus();
            return errorMessage;
        }
        String initAmountText = getInitAmountText().getText();
        if (initAmountText == null || initAmountText.trim().equals("")) {
            errorMessage = MessageFormat.format(LanguageLoader.getString("empty"), getInitAmountLabel().getText());
            getInitAmountText().requestFocus();
            return errorMessage;
        }
        if (!CommomTool.isNumber(initAmountText)) {
            errorMessage = MessageFormat.format(LanguageLoader.getString("NumberFormatException"), getInitAmountLabel().getText());
            getInitAmountText().requestFocus();
            return errorMessage;
        }
        String currency = (String) getCurrencyComboBox().getSelectedItem();
        if (currency == null || currency.trim().equals("")) {
            errorMessage = MessageFormat.format(LanguageLoader.getString("empty"), getCurrencyLabel().getText());
            getCurrencyComboBox().requestFocus();
            return errorMessage;
        }
        return errorMessage;
    }

    @Override
    public void processData(Entity entity) {
        PassbookForm passbookEntity = (PassbookForm) entity;
        passbookEntity.setCreateDate(getInitDataText().getText());
        passbookEntity.setCapital(Double.parseDouble(getInitAmountText().getText().trim()));
        passbookEntity.setCurrencyId(((Currency) currencyComboxMode.getSelectedObject()).getCurrencyId());
        passbookEntity.setCurrencyName(((Currency) currencyComboxMode.getSelectedObject()).getCurrencyName());
    }

    @Override
    public void beforeShowing() {
    }
}
