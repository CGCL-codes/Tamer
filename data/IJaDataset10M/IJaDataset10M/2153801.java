package com.marcinjunger.simplecrm.forms;

import javax.swing.JPanel;
import java.awt.Frame;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Insets;
import com.marcinjunger.utils.beanatoms.MJCheckBox;
import com.marcinjunger.utils.beanatoms.MJComboBox;
import com.marcinjunger.utils.beanatoms.MJLabel;
import com.marcinjunger.utils.beanatoms.MJTextArea;
import com.marcinjunger.utils.beanatoms.MJTextField;
import com.marcinjunger.utils.beanatoms.OkCancelBtnsPanelBean;
import com.marcinjunger.utils.hibernate.beanatoms.MJHibernateComboBox_NDIC;

/**
 * Visual form editable by Eclipse VE, foundation of JobEdit class
 * 
 * @author Marcin Junger, mjunger@hornet.eu.org
 * @see com.marcinjunger.simplecrm.forms.CusEdit
 */
public class JobEdit_frm extends JDialog {

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    private MJLabel MJLabel1 = null;

    private MJLabel MJLabel2 = null;

    private MJLabel MJLabel3 = null;

    private MJLabel MJLabel4 = null;

    private MJLabel MJLabel5 = null;

    private MJTextField jobTitle = null;

    private MJLabel MJLabel10 = null;

    private JPanel contentPanel = null;

    private OkCancelBtnsPanelBean okCancelBtnsPanelBean = null;

    private MJTextArea notes = null;

    private MJHibernateComboBox_NDIC status = null;

    private MJLabel MJLabel31 = null;

    private MJComboBox paymentDate = null;

    private MJComboBox jobDate = null;

    private MJHibernateComboBox_NDIC paymentType = null;

    private MJLabel MJLabel41 = null;

    private MJLabel MJLabel42 = null;

    private MJLabel MJLabel32 = null;

    private MJTextField price = null;

    private MJLabel MJLabel51 = null;

    private MJLabel MJLabel33 = null;

    private MJCheckBox isPaid = null;

    private MJLabel MJLabel43 = null;

    private MJLabel MJLabel331 = null;

    private MJCheckBox isInvoiceIssued = null;

    private MJLabel MJLabel21 = null;

    private MJComboBox invoiceIssueDate = null;

    /**
   * @param owner
   */
    public JobEdit_frm(Frame owner) {
        super(owner, true);
        initialize();
    }

    /**
   * This method initializes this
   * 
   * @return void
   */
    private void initialize() {
        this.setSize(495, 347);
        this.setContentPane(getContentPanel());
    }

    /**
   * This method initializes jContentPane
   * 
   * @return javax.swing.JPanel
   */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            GridBagConstraints gridBagConstraints71 = new GridBagConstraints();
            gridBagConstraints71.fill = GridBagConstraints.BOTH;
            gridBagConstraints71.gridy = 6;
            gridBagConstraints71.weightx = 1.0;
            gridBagConstraints71.gridx = 6;
            GridBagConstraints gridBagConstraints61 = new GridBagConstraints();
            gridBagConstraints61.gridx = 5;
            gridBagConstraints61.insets = new Insets(1, 1, 1, 3);
            gridBagConstraints61.fill = GridBagConstraints.BOTH;
            gridBagConstraints61.gridy = 6;
            MJLabel21 = new MJLabel();
            MJLabel21.setPreferredSize(new Dimension(100, 16));
            MJLabel21.setText("Issue date");
            MJLabel21.setHorizontalAlignment(SwingConstants.TRAILING);
            GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
            gridBagConstraints51.gridx = 3;
            gridBagConstraints51.anchor = GridBagConstraints.WEST;
            gridBagConstraints51.gridy = 6;
            GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
            gridBagConstraints41.gridx = 1;
            gridBagConstraints41.fill = GridBagConstraints.BOTH;
            gridBagConstraints41.insets = new Insets(1, 1, 1, 3);
            gridBagConstraints41.gridy = 6;
            MJLabel331 = new MJLabel();
            MJLabel331.setPreferredSize(new Dimension(75, 16));
            MJLabel331.setText("Issued");
            MJLabel331.setHorizontalAlignment(SwingConstants.TRAILING);
            GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
            gridBagConstraints31.gridx = 1;
            gridBagConstraints31.gridwidth = 6;
            gridBagConstraints31.gridy = 5;
            MJLabel43 = new MJLabel();
            MJLabel43.setFont(new Font("Dialog", Font.BOLD, 12));
            MJLabel43.setText("Invoice");
            MJLabel43.setHorizontalAlignment(SwingConstants.CENTER);
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.gridx = 6;
            gridBagConstraints9.anchor = GridBagConstraints.WEST;
            gridBagConstraints9.gridy = 3;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 5;
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.insets = new Insets(1, 1, 1, 3);
            gridBagConstraints.gridy = 3;
            MJLabel33 = new MJLabel();
            MJLabel33.setPreferredSize(new Dimension(75, 16));
            MJLabel33.setText("Paid");
            MJLabel33.setHorizontalAlignment(SwingConstants.TRAILING);
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.gridx = 1;
            gridBagConstraints8.insets = new Insets(1, 1, 1, 3);
            gridBagConstraints8.gridy = 11;
            MJLabel51 = new MJLabel();
            MJLabel51.setPreferredSize(new Dimension(100, 16));
            MJLabel51.setText("Notes");
            MJLabel51.setHorizontalAlignment(SwingConstants.TRAILING);
            GridBagConstraints gridBagConstraints72 = new GridBagConstraints();
            gridBagConstraints72.fill = GridBagConstraints.BOTH;
            gridBagConstraints72.gridy = 3;
            gridBagConstraints72.weightx = 1.0;
            gridBagConstraints72.insets = new Insets(1, 0, 1, 0);
            gridBagConstraints72.gridx = 3;
            GridBagConstraints gridBagConstraints62 = new GridBagConstraints();
            gridBagConstraints62.gridx = 1;
            gridBagConstraints62.insets = new Insets(1, 1, 1, 3);
            gridBagConstraints62.gridy = 3;
            MJLabel32 = new MJLabel();
            MJLabel32.setPreferredSize(new Dimension(100, 16));
            MJLabel32.setText("Price");
            MJLabel32.setHorizontalAlignment(SwingConstants.TRAILING);
            GridBagConstraints gridBagConstraints53 = new GridBagConstraints();
            gridBagConstraints53.gridx = 1;
            gridBagConstraints53.gridwidth = 6;
            gridBagConstraints53.gridy = 2;
            MJLabel42 = new MJLabel();
            MJLabel42.setFont(new Font("Dialog", Font.BOLD, 12));
            MJLabel42.setText("Payment");
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 1;
            gridBagConstraints4.gridwidth = 6;
            gridBagConstraints4.gridy = 0;
            MJLabel41 = new MJLabel();
            MJLabel41.setFont(new Font("Dialog", Font.BOLD, 12));
            MJLabel41.setText("Basic information");
            GridBagConstraints gridBagConstraints33 = new GridBagConstraints();
            gridBagConstraints33.fill = GridBagConstraints.BOTH;
            gridBagConstraints33.gridy = 4;
            gridBagConstraints33.weightx = 1.0;
            gridBagConstraints33.insets = new Insets(1, 0, 1, 0);
            gridBagConstraints33.gridx = 3;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.fill = GridBagConstraints.BOTH;
            gridBagConstraints2.gridy = 1;
            gridBagConstraints2.weightx = 1.0;
            gridBagConstraints2.insets = new Insets(1, 0, 1, 0);
            gridBagConstraints2.gridx = 6;
            GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
            gridBagConstraints17.fill = GridBagConstraints.BOTH;
            gridBagConstraints17.gridy = 4;
            gridBagConstraints17.weightx = 1.0;
            gridBagConstraints17.insets = new Insets(1, 0, 1, 0);
            gridBagConstraints17.gridx = 6;
            GridBagConstraints gridBagConstraints52 = new GridBagConstraints();
            gridBagConstraints52.gridx = 5;
            gridBagConstraints52.insets = new Insets(1, 1, 1, 3);
            gridBagConstraints52.fill = GridBagConstraints.BOTH;
            gridBagConstraints52.gridy = 1;
            MJLabel31 = new MJLabel();
            MJLabel31.setPreferredSize(new Dimension(75, 16));
            MJLabel31.setText("Job date");
            MJLabel31.setHorizontalAlignment(SwingConstants.TRAILING);
            GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
            gridBagConstraints32.fill = GridBagConstraints.BOTH;
            gridBagConstraints32.gridy = 1;
            gridBagConstraints32.weightx = 1.0;
            gridBagConstraints32.insets = new Insets(1, 0, 1, 0);
            gridBagConstraints32.gridx = 4;
            GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
            gridBagConstraints16.fill = GridBagConstraints.BOTH;
            gridBagConstraints16.gridy = 1;
            gridBagConstraints16.weightx = 1.0;
            gridBagConstraints16.insets = new Insets(1, 0, 1, 0);
            gridBagConstraints16.gridx = 3;
            GridBagConstraints gridBagConstraints111 = new GridBagConstraints();
            gridBagConstraints111.gridx = 3;
            gridBagConstraints111.gridy = 13;
            MJLabel10 = new MJLabel();
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.fill = GridBagConstraints.BOTH;
            gridBagConstraints7.gridy = 10;
            gridBagConstraints7.weightx = 1.0;
            gridBagConstraints7.gridwidth = 4;
            gridBagConstraints7.insets = new Insets(1, 0, 1, 0);
            gridBagConstraints7.gridx = 3;
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.gridx = 1;
            gridBagConstraints6.fill = GridBagConstraints.BOTH;
            gridBagConstraints6.insets = new Insets(1, 1, 1, 3);
            gridBagConstraints6.gridy = 10;
            MJLabel5 = new MJLabel();
            MJLabel5.setText("Title");
            MJLabel5.setHorizontalAlignment(SwingConstants.TRAILING);
            MJLabel5.setPreferredSize(new Dimension(100, 16));
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 1;
            gridBagConstraints5.gridwidth = 6;
            gridBagConstraints5.gridheight = 1;
            gridBagConstraints5.insets = new Insets(5, 0, 5, 0);
            gridBagConstraints5.fill = GridBagConstraints.BOTH;
            gridBagConstraints5.gridy = 7;
            MJLabel4 = new MJLabel();
            MJLabel4.setText("Job details");
            MJLabel4.setHorizontalAlignment(SwingConstants.CENTER);
            MJLabel4.setFont(new Font("Dialog", Font.BOLD, 12));
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 1;
            gridBagConstraints3.fill = GridBagConstraints.BOTH;
            gridBagConstraints3.insets = new Insets(1, 1, 1, 3);
            gridBagConstraints3.gridy = 4;
            MJLabel3 = new MJLabel();
            MJLabel3.setText("Payment type");
            MJLabel3.setHorizontalAlignment(SwingConstants.TRAILING);
            MJLabel3.setPreferredSize(new Dimension(75, 16));
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.gridx = 5;
            gridBagConstraints11.fill = GridBagConstraints.BOTH;
            gridBagConstraints11.insets = new Insets(1, 1, 1, 3);
            gridBagConstraints11.gridy = 4;
            MJLabel2 = new MJLabel();
            MJLabel2.setText("Payment date");
            MJLabel2.setHorizontalAlignment(SwingConstants.TRAILING);
            MJLabel2.setPreferredSize(new Dimension(100, 16));
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.fill = GridBagConstraints.BOTH;
            gridBagConstraints1.insets = new Insets(1, 1, 1, 3);
            gridBagConstraints1.gridy = 1;
            MJLabel1 = new MJLabel();
            MJLabel1.setText("Status");
            MJLabel1.setHorizontalAlignment(SwingConstants.TRAILING);
            MJLabel1.setPreferredSize(new Dimension(100, 16));
            jContentPane = new JPanel();
            jContentPane.setLayout(new GridBagLayout());
            jContentPane.add(MJLabel1, gridBagConstraints1);
            jContentPane.add(MJLabel2, gridBagConstraints11);
            jContentPane.add(MJLabel3, gridBagConstraints3);
            jContentPane.add(MJLabel4, gridBagConstraints5);
            jContentPane.add(MJLabel5, gridBagConstraints6);
            jContentPane.add(getJobTitle(), gridBagConstraints7);
            jContentPane.add(MJLabel10, gridBagConstraints111);
            jContentPane.add(getStatus(), gridBagConstraints16);
            jContentPane.add(MJLabel31, gridBagConstraints52);
            jContentPane.add(getPaymentDate(), gridBagConstraints17);
            jContentPane.add(getJobDate(), gridBagConstraints2);
            jContentPane.add(getPaymentType(), gridBagConstraints33);
            jContentPane.add(MJLabel41, gridBagConstraints4);
            jContentPane.add(MJLabel42, gridBagConstraints53);
            jContentPane.add(MJLabel32, gridBagConstraints62);
            jContentPane.add(getPrice(), gridBagConstraints72);
            jContentPane.add(MJLabel51, gridBagConstraints8);
            jContentPane.add(MJLabel33, gridBagConstraints);
            jContentPane.add(getIsPaid(), gridBagConstraints9);
            jContentPane.add(MJLabel43, gridBagConstraints31);
            jContentPane.add(MJLabel331, gridBagConstraints41);
            jContentPane.add(getIsInvoiceIssued(), gridBagConstraints51);
            jContentPane.add(MJLabel21, gridBagConstraints61);
            jContentPane.add(getInvoiceIssueDate(), gridBagConstraints71);
        }
        return jContentPane;
    }

    /**
   * This method initializes jobTitle
   * 
   * @return com.martinjunger.utils.beanatoms.MJTextField
   */
    protected MJTextField getJobTitle() {
        if (jobTitle == null) {
            jobTitle = new MJTextField();
            jobTitle.setPreferredSize(new Dimension(100, 20));
        }
        return jobTitle;
    }

    /**
   * This method initializes contentPanel
   * 
   * @return javax.swing.JPanel
   */
    private JPanel getContentPanel() {
        if (contentPanel == null) {
            contentPanel = new JPanel();
            contentPanel.setLayout(new BorderLayout());
            contentPanel.setPreferredSize(new Dimension(100, 20));
            contentPanel.add(getJContentPane(), BorderLayout.NORTH);
            contentPanel.add(getOkCancelBtnsPanelBean(), BorderLayout.SOUTH);
            contentPanel.add(getNotes(), BorderLayout.CENTER);
        }
        return contentPanel;
    }

    /**
   * This method initializes okCancelBtnsPanelBean
   * 
   * @return com.martinjunger.utils.beanatoms.OkCancelBtnsPanelBean
   */
    protected OkCancelBtnsPanelBean getOkCancelBtnsPanelBean() {
        if (okCancelBtnsPanelBean == null) {
            okCancelBtnsPanelBean = new OkCancelBtnsPanelBean();
            okCancelBtnsPanelBean.setPreferredSize(new Dimension(156, 30));
        }
        return okCancelBtnsPanelBean;
    }

    /**
   * This method initializes notes
   * 
   * @return com.martinjunger.utils.beanatoms.MJTextArea
   */
    protected MJTextArea getNotes() {
        if (notes == null) {
            notes = new MJTextArea();
            notes.setPreferredSize(new Dimension(0, 100));
        }
        return notes;
    }

    /**
   * This method initializes status
   * 
   * @return com.marcinjunger.utils.hibernate.beanatoms.MJHibernateComboBox_NDIC
   */
    protected MJHibernateComboBox_NDIC getStatus() {
        if (status == null) {
            status = new MJHibernateComboBox_NDIC();
            status.setPreferredSize(new Dimension(100, 20));
        }
        return status;
    }

    /**
   * This method initializes paymentDate
   * 
   * @return com.martinjunger.utils.beanatoms.MJComboBox
   */
    protected MJComboBox getPaymentDate() {
        if (paymentDate == null) {
            paymentDate = new MJComboBox();
            paymentDate.setPreferredSize(new Dimension(100, 20));
            paymentDate.setEnabled(false);
        }
        return paymentDate;
    }

    /**
   * This method initializes jobDate
   * 
   * @return com.martinjunger.utils.beanatoms.MJComboBox
   */
    protected MJComboBox getJobDate() {
        if (jobDate == null) {
            jobDate = new MJComboBox();
            jobDate.setPreferredSize(new Dimension(100, 20));
        }
        return jobDate;
    }

    /**
   * This method initializes paymentType
   * 
   * @return com.marcinjunger.utils.hibernate.beanatoms.MJHibernateComboBox_NDIC
   */
    protected MJHibernateComboBox_NDIC getPaymentType() {
        if (paymentType == null) {
            paymentType = new MJHibernateComboBox_NDIC();
            paymentType.setPreferredSize(new Dimension(100, 20));
            paymentType.setEnabled(false);
        }
        return paymentType;
    }

    /**
   * This method initializes price
   * 
   * @return com.martinjunger.utils.beanatoms.MJTextField
   */
    protected MJTextField getPrice() {
        if (price == null) {
            price = new MJTextField();
            price.setPreferredSize(new Dimension(100, 20));
        }
        return price;
    }

    /**
   * This method initializes isPaid
   * 
   * @return com.martinjunger.utils.beanatoms.MJCheckBox
   */
    protected MJCheckBox getIsPaid() {
        if (isPaid == null) {
            isPaid = new MJCheckBox();
        }
        return isPaid;
    }

    /**
   * This method initializes isInvoiceIssued	
   * 	
   * @return com.marcinjunger.utils.beanatoms.MJCheckBox	
   */
    protected MJCheckBox getIsInvoiceIssued() {
        if (isInvoiceIssued == null) {
            isInvoiceIssued = new MJCheckBox();
        }
        return isInvoiceIssued;
    }

    /**
   * This method initializes invoiceIssueDate	
   * 	
   * @return com.marcinjunger.utils.beanatoms.MJComboBox	
   */
    protected MJComboBox getInvoiceIssueDate() {
        if (invoiceIssueDate == null) {
            invoiceIssueDate = new MJComboBox();
            invoiceIssueDate.setEnabled(false);
            invoiceIssueDate.setPreferredSize(new Dimension(100, 20));
        }
        return invoiceIssueDate;
    }
}
