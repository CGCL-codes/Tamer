package com.empower.client.view.create;

import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import com.empower.client.utils.WidgetProperties;
import com.empower.constants.AppClientConstants;

public class CreateCustomerAccountFrame extends JInternalFrame {

    private JLabel daysLbl;

    private JFormattedTextField gracePeriodTxf;

    private JFormattedTextField interestRateTxf;

    private JLabel interestRateLbl;

    private JLabel gracePeriodLbl;

    private JPanel intrstRatePnl;

    private JComboBox branchNameCbx;

    private JLabel branchNameLbl;

    private JButton viewCustomersBtn;

    private JCheckBox markInactiveCheckBox;

    private JSpinner creationDateSpnr;

    private JLabel creationDateLbl;

    private JTextField accountHolderTxf;

    private JLabel accountHolderLbl;

    private JTextField acctCodeTxf;

    private JPanel topPanel;

    private JLabel acctCodeLbl;

    private JPanel basicDtlsPnl;

    private JLabel bankAcctNbrLbl;

    private JTextField bankAcctNbrTxf;

    private JLabel bankNameLbl;

    private JComboBox bankNameCbx;

    private JLabel dpIdNbrLbl;

    private JTextField dpIdNbrTxf;

    private JLabel dpNameLbl;

    private JTextField dpNameTxf;

    private JLabel panCardNbrLbl;

    private JTextField panCardNbrTxf;

    private JLabel customerCodeLbl;

    private JTextField customerCodeTxf;

    private JButton cancelBtn;

    private JButton okBtn;

    ImageIcon requiredIcon = new ImageIcon(getClassLoader().getResource(AppClientConstants.IMG_PKG_PATH.concat("required_field.gif")));

    public CreateCustomerAccountFrame(String frameTitle, boolean isResizable, boolean isClosable, boolean isMaximzable, boolean isMinimizable) {
        super(frameTitle, isResizable, isClosable, isMaximzable, isMinimizable);
        setPreferredSize(new Dimension(600, 320));
        ImageIcon ecsIcon = new ImageIcon(getClassLoader().getResource(AppClientConstants.IMG_PKG_PATH.concat("empower_logo.JPG")));
        setFrameIcon(ecsIcon);
        getContentPane().setLayout(null);
        getContentPane().add(getTopPanel());
        final JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(5, 127, 580, 128);
        getContentPane().add(tabbedPane);
        tabbedPane.addTab("Basic Details", null, getBasicDtlsPnl(), null);
        tabbedPane.addTab("Interest rate configuration", null, getIntrstRatePnl(), null);
        getContentPane().add(getOkBtn());
        getContentPane().add(getCancelBtn());
    }

    public JLabel getBankAcctNbrLbl() {
        if (null == bankAcctNbrLbl) {
            bankAcctNbrLbl = new JLabel();
            bankAcctNbrLbl.setText("Bank account #");
            bankAcctNbrLbl.setBounds(5, 10, 117, 20);
            setRequiredIcon(bankAcctNbrLbl);
            WidgetProperties.setLabelProperties(bankAcctNbrLbl);
        }
        return bankAcctNbrLbl;
    }

    public JTextField getBankAcctNbrTxf() {
        if (null == bankAcctNbrTxf) {
            bankAcctNbrTxf = new JTextField();
            bankAcctNbrTxf.setBounds(127, 10, 108, 20);
            bankAcctNbrTxf.setName(getBankAcctNbrLbl().getText());
        }
        return bankAcctNbrTxf;
    }

    public JLabel getBankNameLbl() {
        if (null == bankNameLbl) {
            bankNameLbl = new JLabel();
            bankNameLbl.setText("Bank name");
            bankNameLbl.setBounds(250, 10, 80, 20);
            setRequiredIcon(bankNameLbl);
            WidgetProperties.setLabelProperties(bankNameLbl);
        }
        return bankNameLbl;
    }

    public JComboBox getBankNameCbx() {
        if (null == bankNameCbx) {
            bankNameCbx = new JComboBox();
            bankNameCbx.setBounds(324, 10, 245, 20);
        }
        return bankNameCbx;
    }

    public JLabel getDPIdNbrLbl() {
        if (null == dpIdNbrLbl) {
            dpIdNbrLbl = new JLabel();
            dpIdNbrLbl.setText("DP Identification #");
            dpIdNbrLbl.setBounds(5, 70, 117, 20);
            setRequiredIcon(dpIdNbrLbl);
            WidgetProperties.setLabelProperties(dpIdNbrLbl);
        }
        return dpIdNbrLbl;
    }

    public JTextField getDPIdNbrTxf() {
        if (null == dpIdNbrTxf) {
            dpIdNbrTxf = new JTextField();
            dpIdNbrTxf.setBounds(127, 70, 108, 20);
            dpIdNbrTxf.setName(getDPIdNbrLbl().getText());
        }
        return dpIdNbrTxf;
    }

    public JLabel getDPNameLbl() {
        if (null == dpNameLbl) {
            dpNameLbl = new JLabel();
            dpNameLbl.setText("DP Name");
            dpNameLbl.setBounds(250, 70, 80, 20);
            setRequiredIcon(dpNameLbl);
            WidgetProperties.setLabelProperties(dpNameLbl);
        }
        return dpNameLbl;
    }

    public JTextField getDPNameTxf() {
        if (null == dpNameTxf) {
            dpNameTxf = new JTextField();
            dpNameTxf.setBounds(324, 70, 245, 20);
            dpNameTxf.setName(getDPNameLbl().getText());
        }
        return dpNameTxf;
    }

    public JLabel getPANCardNbrLbl() {
        if (null == panCardNbrLbl) {
            panCardNbrLbl = new JLabel();
            panCardNbrLbl.setText("PAN Card");
            panCardNbrLbl.setBounds(5, 40, 117, 20);
            setRequiredIcon(panCardNbrLbl);
            WidgetProperties.setLabelProperties(panCardNbrLbl);
        }
        return panCardNbrLbl;
    }

    public JTextField getPANCardNbrTxf() {
        if (null == panCardNbrTxf) {
            panCardNbrTxf = new JTextField();
            panCardNbrTxf.setBounds(127, 40, 108, 20);
            panCardNbrTxf.setName(getPANCardNbrLbl().getText());
        }
        return panCardNbrTxf;
    }

    public JPanel getBasicDtlsPnl() {
        if (null == basicDtlsPnl) {
            basicDtlsPnl = new JPanel();
            basicDtlsPnl.setLayout(null);
            basicDtlsPnl.setBounds(0, 0, 490, 93);
            basicDtlsPnl.add(getBankAcctNbrLbl());
            basicDtlsPnl.add(getBankAcctNbrTxf());
            basicDtlsPnl.add(getBankNameLbl());
            basicDtlsPnl.add(getBankNameCbx());
            basicDtlsPnl.add(getDPIdNbrLbl());
            basicDtlsPnl.add(getDPIdNbrTxf());
            basicDtlsPnl.add(getDPNameLbl());
            basicDtlsPnl.add(getDPNameTxf());
            basicDtlsPnl.add(getPANCardNbrLbl());
            basicDtlsPnl.add(getPANCardNbrTxf());
            basicDtlsPnl.add(getBranchNameLbl());
            basicDtlsPnl.add(getBranchNameCbx());
        }
        return basicDtlsPnl;
    }

    public JPanel getTopPanel() {
        if (null == topPanel) {
            topPanel = new JPanel();
            topPanel.setLayout(null);
            topPanel.setBounds(5, 5, 580, 121);
            topPanel.add(getAcctNbr());
            topPanel.add(getAcctCodeTxf());
            topPanel.add(getAccountHolderLbl());
            topPanel.add(getAccountHolderTxf());
            topPanel.add(getCreationDateLbl());
            topPanel.add(getCreationDateSpnr());
            topPanel.add(getMarkInactiveCheckBox());
            topPanel.add(getCustomerCodeLbl());
            topPanel.add(getCustomerCodeTxf());
            topPanel.add(getViewCustomersBtn());
        }
        return topPanel;
    }

    public JLabel getCustomerCodeLbl() {
        if (null == customerCodeLbl) {
            customerCodeLbl = new JLabel();
            customerCodeLbl.setText("Customer code");
            customerCodeLbl.setBounds(5, 95, 110, 20);
            setRequiredIcon(customerCodeLbl);
            WidgetProperties.setLabelProperties(customerCodeLbl);
        }
        return customerCodeLbl;
    }

    public JTextField getCustomerCodeTxf() {
        if (customerCodeTxf == null) {
            customerCodeTxf = new JTextField();
            customerCodeTxf.setBounds(120, 95, 172, 20);
            customerCodeTxf.setName(getCustomerCodeLbl().getText());
        }
        return customerCodeTxf;
    }

    public JLabel getAcctNbr() {
        if (null == acctCodeLbl) {
            acctCodeLbl = new JLabel();
            acctCodeLbl.setText("Account code");
            acctCodeLbl.setBounds(5, 5, 110, 20);
            setRequiredIcon(acctCodeLbl);
            WidgetProperties.setLabelProperties(acctCodeLbl);
        }
        return acctCodeLbl;
    }

    public JTextField getAcctCodeTxf() {
        if (acctCodeTxf == null) {
            acctCodeTxf = new JTextField();
            acctCodeTxf.setBounds(120, 5, 172, 20);
            acctCodeTxf.setName(getAcctNbr().getText());
        }
        return acctCodeTxf;
    }

    public JLabel getAccountHolderLbl() {
        if (accountHolderLbl == null) {
            accountHolderLbl = new JLabel();
            accountHolderLbl.setText("Account holder");
            accountHolderLbl.setBounds(5, 35, 104, 20);
            setRequiredIcon(accountHolderLbl);
            WidgetProperties.setLabelProperties(accountHolderLbl);
        }
        return accountHolderLbl;
    }

    public JTextField getAccountHolderTxf() {
        if (accountHolderTxf == null) {
            accountHolderTxf = new JTextField();
            accountHolderTxf.setBounds(120, 35, 375, 20);
            accountHolderTxf.setName(getAccountHolderLbl().getText());
        }
        return accountHolderTxf;
    }

    public JLabel getCreationDateLbl() {
        if (creationDateLbl == null) {
            creationDateLbl = new JLabel();
            creationDateLbl.setText("Creation Date");
            creationDateLbl.setBounds(5, 65, 110, 20);
            setRequiredIcon(creationDateLbl);
            WidgetProperties.setLabelProperties(creationDateLbl);
        }
        return creationDateLbl;
    }

    public JSpinner getCreationDateSpnr() {
        if (creationDateSpnr == null) {
            creationDateSpnr = WidgetProperties.setJSpinnerDate(creationDateSpnr);
            creationDateSpnr.setBounds(120, 65, 90, 20);
            creationDateSpnr.setName(getCreationDateLbl().getText());
        }
        return creationDateSpnr;
    }

    public JCheckBox getMarkInactiveCheckBox() {
        if (markInactiveCheckBox == null) {
            markInactiveCheckBox = new JCheckBox();
            markInactiveCheckBox.setText("Mark Inactive");
            markInactiveCheckBox.setBounds(297, 5, 118, 20);
        }
        return markInactiveCheckBox;
    }

    public JButton getViewCustomersBtn() {
        if (viewCustomersBtn == null) {
            viewCustomersBtn = new JButton();
            viewCustomersBtn.setText("View customers");
            viewCustomersBtn.setBounds(301, 95, 135, 20);
        }
        return viewCustomersBtn;
    }

    public JLabel getBranchNameLbl() {
        if (branchNameLbl == null) {
            branchNameLbl = new JLabel();
            branchNameLbl.setText("Branch");
            branchNameLbl.setBounds(250, 40, 80, 20);
            setRequiredIcon(branchNameLbl);
            WidgetProperties.setLabelProperties(branchNameLbl);
        }
        return branchNameLbl;
    }

    public JComboBox getBranchNameCbx() {
        if (branchNameCbx == null) {
            branchNameCbx = new JComboBox();
            branchNameCbx.setBounds(324, 40, 245, 20);
        }
        return branchNameCbx;
    }

    private void setRequiredIcon(JLabel label) {
        if (label != null) {
            label.setHorizontalTextPosition(SwingConstants.LEFT);
            label.setIcon(requiredIcon);
        }
    }

    public ClassLoader getClassLoader() {
        return this.getClass().getClassLoader();
    }

    public JButton getOkBtn() {
        if (okBtn == null) {
            okBtn = new JButton();
            okBtn.setBounds(5, 260, 93, 23);
            okBtn.setText("OK");
        }
        return okBtn;
    }

    public JButton getCancelBtn() {
        if (cancelBtn == null) {
            cancelBtn = new JButton();
            cancelBtn.setBounds(490, 260, 93, 23);
            cancelBtn.setText("Cancel");
        }
        return cancelBtn;
    }

    public JPanel getIntrstRatePnl() {
        if (intrstRatePnl == null) {
            intrstRatePnl = new JPanel();
            intrstRatePnl.setLayout(null);
            intrstRatePnl.add(getGracePeriodLbl());
            intrstRatePnl.add(getInterestRateLbl());
            intrstRatePnl.add(getInterestRateTxf());
            intrstRatePnl.add(getGracePeriodTxf());
            intrstRatePnl.add(getDaysLbl());
        }
        return intrstRatePnl;
    }

    public JLabel getGracePeriodLbl() {
        if (gracePeriodLbl == null) {
            gracePeriodLbl = new JLabel();
            gracePeriodLbl.setText("Grace Period");
            gracePeriodLbl.setBounds(10, 10, 90, 20);
            WidgetProperties.setLabelProperties(gracePeriodLbl);
            setRequiredIcon(gracePeriodLbl);
        }
        return gracePeriodLbl;
    }

    public JLabel getInterestRateLbl() {
        if (interestRateLbl == null) {
            interestRateLbl = new JLabel();
            interestRateLbl.setText("Interest rate");
            interestRateLbl.setBounds(10, 45, 90, 20);
            WidgetProperties.setLabelProperties(interestRateLbl);
            setRequiredIcon(interestRateLbl);
        }
        return interestRateLbl;
    }

    public JFormattedTextField getInterestRateTxf() {
        if (interestRateTxf == null) {
            interestRateTxf = WidgetProperties.setPctFmtToTxf(interestRateTxf);
            interestRateTxf.setBounds(105, 45, 100, 20);
            interestRateTxf.setName(getInterestRateLbl().getText());
            interestRateTxf.setHorizontalAlignment(SwingConstants.RIGHT);
        }
        return interestRateTxf;
    }

    public JFormattedTextField getGracePeriodTxf() {
        if (gracePeriodTxf == null) {
            gracePeriodTxf = WidgetProperties.setIntegerFmtToTxf(gracePeriodTxf);
            gracePeriodTxf.setBounds(105, 10, 50, 20);
            gracePeriodTxf.setName(getGracePeriodLbl().getText());
            gracePeriodTxf.setHorizontalAlignment(SwingConstants.RIGHT);
        }
        return gracePeriodTxf;
    }

    private JLabel getDaysLbl() {
        if (daysLbl == null) {
            daysLbl = new JLabel();
            daysLbl.setText("days");
            daysLbl.setBounds(160, 10, 35, 20);
            WidgetProperties.setLabelProperties(daysLbl);
        }
        return daysLbl;
    }
}
