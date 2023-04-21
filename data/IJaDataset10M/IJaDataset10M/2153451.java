package jdollars.demoapp;

import jdollars.core.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 *
 *
 *   @author Sean C. Sullivan
 *
 */
public class CreditCardFieldsInputPanel extends JPanel {

    private JComboBox m_cmbCardNumber;

    private JComboBox m_cmbMonth;

    private JComboBox m_cmbYear;

    private JTextField m_txfCardVerificationValue;

    public CreditCardFieldsInputPanel() {
        this.setLayout(new GridLayout(4, 2));
        String[] cardNumbers = { "4111111111111111", "5111111111111118" };
        m_cmbCardNumber = new JComboBox(cardNumbers);
        m_cmbCardNumber.setEditable(true);
        Dimension dim = m_cmbCardNumber.getPreferredSize();
        m_cmbCardNumber.setPreferredSize(new Dimension((int) (dim.getWidth() + 20), (int) (dim.getHeight())));
        m_cmbCardNumber.setSelectedIndex(0);
        String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
        m_cmbMonth = new JComboBox(months);
        m_cmbMonth.setEditable(false);
        m_cmbMonth.setSelectedIndex(months.length - 1);
        String[] years = { "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "1999", "2000" };
        m_cmbYear = new JComboBox(years);
        m_cmbYear.setEditable(false);
        m_cmbYear.setSelectedIndex(0);
        m_txfCardVerificationValue = new JTextField("");
        this.add(new JLabel("Card number:"));
        this.add(this.m_cmbCardNumber);
        this.add(new JLabel("Card expiration month:"));
        this.add(this.m_cmbMonth);
        this.add(new JLabel("Card expiration year:"));
        this.add(this.m_cmbYear);
        this.add(new JLabel("Card verification value:"));
        this.add(m_txfCardVerificationValue);
    }

    public ICreditCardNumber getCreditCardNumber() throws InvalidCreditCardNumberException {
        ICreditCardNumber ccn = null;
        ccn = ICreditCardNumber.Factory.create((String) this.m_cmbCardNumber.getSelectedItem());
        return ccn;
    }

    public String getExpirationMonth() {
        return "" + (m_cmbMonth.getSelectedIndex() + 1);
    }

    public String getExpirationYear() {
        return (String) m_cmbYear.getSelectedItem();
    }

    public String getCardVerification() {
        String strResult = null;
        if (m_txfCardVerificationValue.getText().length() == 0) {
            strResult = null;
        } else {
            strResult = m_txfCardVerificationValue.getText();
        }
        return strResult;
    }

    public boolean success() {
        boolean bResult = true;
        try {
            this.getCreditCardNumber();
        } catch (InvalidCreditCardNumberException ex) {
            bResult = false;
        }
        return bResult;
    }

    public void addActionListener(ActionListener listener) {
        m_txfCardVerificationValue.addActionListener(listener);
    }
}
