package com.wateray.ipassbook.ui.dialog.wizard;

import java.awt.Insets;
import java.text.MessageFormat;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import com.wateray.ipassbook.domain.Entity;
import com.wateray.ipassbook.domain.form.PassbookForm;
import com.wateray.ipassbook.util.LanguageLoader;

/**
 * @author wateray
 * @create 2009-5-16
 */
public class PassbookWizardPageOne extends AbstractWizardPage {

    /**
	 * serialVersionUID
	 */
    private static final long serialVersionUID = 1L;

    private JLabel passbookNameLabel;

    private JComboBox passbookCoboBox;

    private JLabel passbookDescribeLabel;

    private JLabel describeLabel;

    private JTextField describeText;

    private JLabel describeDescribeLabel;

    public PassbookWizardPageOne(IWizardDialog wizardDialog) {
        super(wizardDialog);
        initPage();
        initState();
    }

    protected void initPage() {
        super.initPage();
        constraints.insets = new Insets(15, left, bottom, right);
        constraints.gridy = ++gridy;
        this.add(getPassbookNameLabel(), constraints);
        constraints.gridx = 1;
        this.add(getPassbookCoboBox(), constraints);
        constraints.gridx = 2;
        constraints.weightx = 1.0;
        this.add(new JLabel(), constraints);
        constraints.weightx = 0.0;
        constraints.insets = inserts;
        constraints.gridx = 1;
        constraints.gridy = ++gridy;
        this.add(getPassbookDescribeLabel(), constraints);
        constraints.gridx = 0;
        constraints.gridy = ++gridy;
        constraints.insets = new Insets(85, left, bottom, right);
        this.add(getDescribeLabel(), constraints);
        constraints.gridx = 1;
        this.add(getDescribeText(), constraints);
        constraints.insets = inserts;
        constraints.gridx = 1;
        constraints.gridy = ++gridy;
        this.add(getDescribeDescribeLabel(), constraints);
        constraints.weighty = 1.0;
        constraints.gridy = ++gridy;
        this.add(new JLabel(), constraints);
        constraints.weighty = 0.0;
    }

    private void initState() {
        getPassbookCoboBox().requestFocus();
    }

    /**
	 * @return the passbookNameLabel
	 */
    private JLabel getPassbookNameLabel() {
        if (passbookNameLabel == null) {
            passbookNameLabel = new JLabel(LanguageLoader.getString("Passbook_Name"));
        }
        return passbookNameLabel;
    }

    /**
	 * @return the passbookCoboBox
	 */
    private JComboBox getPassbookCoboBox() {
        if (passbookCoboBox == null) {
            passbookCoboBox = new JComboBox();
            passbookCoboBox.setEditable(true);
        }
        return passbookCoboBox;
    }

    /**
	 * @return the passbookDescribeLabel
	 */
    private JLabel getPassbookDescribeLabel() {
        if (passbookDescribeLabel == null) {
            passbookDescribeLabel = new JLabel(LanguageLoader.getString("PassbookDescribe"));
        }
        return passbookDescribeLabel;
    }

    /**
	 * @return the describeLabel
	 */
    private JLabel getDescribeLabel() {
        if (describeLabel == null) {
            describeLabel = new JLabel(LanguageLoader.getString("Describe"));
        }
        return describeLabel;
    }

    /**
	 * @return the describeText
	 */
    private JTextField getDescribeText() {
        if (describeText == null) {
            describeText = new JTextField();
        }
        return describeText;
    }

    /**
	 * @return the describeDescribeLabel
	 */
    private JLabel getDescribeDescribeLabel() {
        if (describeDescribeLabel == null) {
            describeDescribeLabel = new JLabel(LanguageLoader.getString("DescribeDescribe"));
        }
        return describeDescribeLabel;
    }

    @Override
    public String checkData() {
        String errorMessage = "";
        logger.debug(this.getClass().getSimpleName() + " is invoked.");
        String passbookName = (String) getPassbookCoboBox().getSelectedItem();
        if (passbookName == null || passbookName.trim().equals("")) {
            errorMessage = MessageFormat.format(LanguageLoader.getString("empty"), getPassbookNameLabel().getText());
            getPassbookCoboBox().requestFocus();
            return errorMessage;
        }
        return errorMessage;
    }

    @Override
    public void processData(Entity entity) {
        PassbookForm passbook = (PassbookForm) entity;
        passbook.setPassbookName((String) getPassbookCoboBox().getSelectedItem());
        passbook.setMemo(getDescribeText().getText());
    }

    @Override
    public void beforeShowing() {
    }
}
