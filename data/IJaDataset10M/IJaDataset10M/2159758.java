package com.empower.client.view.file;

import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import com.empower.client.utils.LabeledBorder;
import com.empower.client.utils.WidgetProperties;
import com.empower.constants.AppClientConstants;

public class CompanyInfoFrame extends JInternalFrame {

    private JTextField emailIdTxf;

    private JLabel eMailIDLbl;

    private JTextField faxTxf;

    private JLabel faxLbl;

    private JTextField phone2Txf;

    private JLabel phone2Lbl;

    private JTextField phone1Txf;

    private JLabel phone1Lbl;

    private JTextField pinCodeTxf;

    private JLabel pinCodeLbl;

    private JComboBox stateCbx;

    private JLabel stateLabel;

    private JTextField cityDistrictTxf;

    private JLabel cityDistrictLbl;

    private JTextField address2Txf;

    private JLabel address2Lbl;

    private JTextField address1Txf;

    private JLabel address1Lbl;

    private JLabel companyURLLbl;

    private JPanel companyInfoPnl = null;

    private JTextField companyURLTxf;

    private JTextField captionTxtFld;

    private JTextField companyNameTxf;

    private JLabel captionLbl;

    private JLabel companyNameLbl;

    private JButton okBtn;

    private JButton cancelBtn;

    ImageIcon requiredIcon = new ImageIcon(getClassLoader().getResource(AppClientConstants.IMG_PKG_PATH.concat("required_field.gif")));

    public CompanyInfoFrame(String frameTitle, boolean isResizable, boolean isClosable, boolean isMaximzable, boolean isMinimizable) {
        super(frameTitle, isResizable, isClosable, isMaximzable, isMinimizable);
        ImageIcon ecsIcon = new ImageIcon(getClassLoader().getResource(AppClientConstants.IMG_PKG_PATH.concat("empower_logo.JPG")));
        setFrameIcon(ecsIcon);
        setPreferredSize(new Dimension(450, 465));
        getContentPane().setLayout(null);
        getContentPane().add(getCompanyInfoPnl());
        getContentPane().add(getOkBtn());
        getContentPane().add(getCancelBtn());
    }

    private ClassLoader getClassLoader() {
        return this.getClass().getClassLoader();
    }

    public JPanel getCompanyInfoPnl() {
        if (companyInfoPnl == null) {
            companyInfoPnl = new JPanel();
            companyInfoPnl.setLayout(null);
            companyInfoPnl.setBounds(5, 10, 430, 387);
            LabeledBorder companybrdr = new LabeledBorder();
            companybrdr.setTitle(null);
            companyInfoPnl.setBorder(companybrdr);
            companyInfoPnl.add(getCompanyNameLbl());
            companyInfoPnl.add(getCompanyNameTxf());
            companyInfoPnl.add(getCompanyURLTxf());
            companyInfoPnl.add(getCaptionLbl());
            companyInfoPnl.add(getCaptionTxf());
            companyInfoPnl.add(getCompanyURLLbl());
            companyInfoPnl.add(getAddress1Lbl());
            companyInfoPnl.add(getAddress1Txf());
            companyInfoPnl.add(getAddress2Lbl());
            companyInfoPnl.add(getAddress2Txf());
            companyInfoPnl.add(getCityDistrictLbl());
            companyInfoPnl.add(getCityDistrictTxf());
            companyInfoPnl.add(getStateLabel());
            companyInfoPnl.add(getStateCbx());
            companyInfoPnl.add(getPinCodeLbl());
            companyInfoPnl.add(getPinCodeTxf());
            companyInfoPnl.add(getPhone1Lbl());
            companyInfoPnl.add(getPhone1Txf());
            companyInfoPnl.add(getPhone2Lbl());
            companyInfoPnl.add(getPhone2Txf());
            companyInfoPnl.add(getFaxLbl());
            companyInfoPnl.add(getFaxTxf());
            companyInfoPnl.add(getEMailIDLbl());
            companyInfoPnl.add(getEmailIdTxf());
        }
        return companyInfoPnl;
    }

    public JLabel getCompanyNameLbl() {
        if (companyNameLbl == null) {
            companyNameLbl = new JLabel();
            companyNameLbl.setText("Company Name");
            companyNameLbl.setBounds(15, 20, 105, 20);
            setRequiredIcon(companyNameLbl);
            WidgetProperties.setLabelProperties(companyNameLbl);
        }
        return companyNameLbl;
    }

    public JLabel getCaptionLbl() {
        if (captionLbl == null) {
            captionLbl = new JLabel();
            captionLbl.setText("Caption");
            captionLbl.setBounds(15, 80, 100, 20);
            WidgetProperties.setLabelProperties(captionLbl);
        }
        return captionLbl;
    }

    public JTextField getCompanyNameTxf() {
        if (companyNameTxf == null) {
            companyNameTxf = new JTextField();
            companyNameTxf.setBounds(115, 20, 245, 20);
            companyNameTxf.setName(getCompanyNameLbl().getText());
        }
        return companyNameTxf;
    }

    public JTextField getCaptionTxf() {
        if (captionTxtFld == null) {
            captionTxtFld = new JTextField();
            captionTxtFld.setBounds(115, 80, 245, 20);
            captionTxtFld.setName(getCaptionLbl().getText());
        }
        return captionTxtFld;
    }

    public JTextField getCompanyURLTxf() {
        if (companyURLTxf == null) {
            companyURLTxf = new JTextField();
            companyURLTxf.setBounds(115, 50, 245, 20);
            companyURLTxf.setName(getCompanyURLLbl().getText());
        }
        return companyURLTxf;
    }

    public JLabel getCompanyURLLbl() {
        if (companyURLLbl == null) {
            companyURLLbl = new JLabel();
            companyURLLbl.setBounds(15, 50, 100, 20);
            companyURLLbl.setText("Company URL");
            WidgetProperties.setLabelProperties(companyURLLbl);
        }
        return companyURLLbl;
    }

    public JLabel getAddress1Lbl() {
        if (address1Lbl == null) {
            address1Lbl = new JLabel();
            address1Lbl.setBounds(15, 110, 100, 20);
            address1Lbl.setText("Address 1");
            setRequiredIcon(address1Lbl);
            WidgetProperties.setLabelProperties(address1Lbl);
        }
        return address1Lbl;
    }

    public JTextField getAddress1Txf() {
        if (address1Txf == null) {
            address1Txf = new JTextField();
            address1Txf.setBounds(115, 110, 305, 20);
            address1Txf.setName(getAddress1Lbl().getText());
        }
        return address1Txf;
    }

    public JLabel getAddress2Lbl() {
        if (address2Lbl == null) {
            address2Lbl = new JLabel();
            address2Lbl.setBounds(15, 140, 100, 20);
            address2Lbl.setText("Address 2");
            setRequiredIcon(address2Lbl);
            WidgetProperties.setLabelProperties(address2Lbl);
        }
        return address2Lbl;
    }

    public JTextField getAddress2Txf() {
        if (address2Txf == null) {
            address2Txf = new JTextField();
            address2Txf.setBounds(115, 140, 305, 20);
            address2Txf.setName(address2Lbl.getText());
        }
        return address2Txf;
    }

    public JLabel getCityDistrictLbl() {
        if (cityDistrictLbl == null) {
            cityDistrictLbl = new JLabel();
            cityDistrictLbl.setBounds(15, 170, 100, 20);
            cityDistrictLbl.setText("City / District");
            setRequiredIcon(cityDistrictLbl);
            WidgetProperties.setLabelProperties(cityDistrictLbl);
        }
        return cityDistrictLbl;
    }

    public JTextField getCityDistrictTxf() {
        if (cityDistrictTxf == null) {
            cityDistrictTxf = new JTextField();
            cityDistrictTxf.setBounds(115, 170, 165, 20);
            cityDistrictTxf.setName(getCityDistrictLbl().getText());
        }
        return cityDistrictTxf;
    }

    public JLabel getStateLabel() {
        if (stateLabel == null) {
            stateLabel = new JLabel();
            stateLabel.setBounds(15, 200, 100, 20);
            stateLabel.setText("State");
            setRequiredIcon(stateLabel);
            WidgetProperties.setLabelProperties(stateLabel);
        }
        return stateLabel;
    }

    public JComboBox getStateCbx() {
        if (stateCbx == null) {
            stateCbx = new JComboBox();
            stateCbx.setBounds(115, 200, 170, 20);
        }
        return stateCbx;
    }

    public JLabel getPinCodeLbl() {
        if (pinCodeLbl == null) {
            pinCodeLbl = new JLabel();
            pinCodeLbl.setBounds(15, 230, 100, 20);
            pinCodeLbl.setText("Pin Code");
            setRequiredIcon(pinCodeLbl);
            WidgetProperties.setLabelProperties(pinCodeLbl);
        }
        return pinCodeLbl;
    }

    public JTextField getPinCodeTxf() {
        if (pinCodeTxf == null) {
            pinCodeTxf = new JTextField();
            pinCodeTxf.setBounds(115, 230, 70, 20);
            pinCodeTxf.setName(getPinCodeLbl().getText());
        }
        return pinCodeTxf;
    }

    public JLabel getPhone1Lbl() {
        if (phone1Lbl == null) {
            phone1Lbl = new JLabel();
            phone1Lbl.setBounds(15, 260, 100, 20);
            phone1Lbl.setText("Phone1 #");
            setRequiredIcon(phone1Lbl);
            WidgetProperties.setLabelProperties(phone1Lbl);
        }
        return phone1Lbl;
    }

    public JTextField getPhone1Txf() {
        if (phone1Txf == null) {
            phone1Txf = new JTextField();
            phone1Txf.setBounds(115, 260, 150, 20);
            phone1Txf.setName(getPhone1Lbl().getText());
        }
        return phone1Txf;
    }

    public JLabel getPhone2Lbl() {
        if (phone2Lbl == null) {
            phone2Lbl = new JLabel();
            phone2Lbl.setBounds(15, 290, 100, 20);
            phone2Lbl.setText("Phone2 #");
            WidgetProperties.setLabelProperties(phone2Lbl);
        }
        return phone2Lbl;
    }

    public JTextField getPhone2Txf() {
        if (phone2Txf == null) {
            phone2Txf = new JTextField();
            phone2Txf.setBounds(115, 290, 150, 20);
            phone2Txf.setName(getPhone2Lbl().getText());
        }
        return phone2Txf;
    }

    public JLabel getFaxLbl() {
        if (faxLbl == null) {
            faxLbl = new JLabel();
            faxLbl.setBounds(15, 320, 100, 20);
            faxLbl.setText("Fax #");
            WidgetProperties.setLabelProperties(faxLbl);
        }
        return faxLbl;
    }

    public JTextField getFaxTxf() {
        if (faxTxf == null) {
            faxTxf = new JTextField();
            faxTxf.setBounds(115, 320, 150, 20);
            faxTxf.setName(getFaxLbl().getText());
        }
        return faxTxf;
    }

    public JLabel getEMailIDLbl() {
        if (eMailIDLbl == null) {
            eMailIDLbl = new JLabel();
            eMailIDLbl.setBounds(15, 350, 100, 20);
            eMailIDLbl.setText("E-Mail ID");
            setRequiredIcon(eMailIDLbl);
            WidgetProperties.setLabelProperties(eMailIDLbl);
        }
        return eMailIDLbl;
    }

    public JTextField getEmailIdTxf() {
        if (emailIdTxf == null) {
            emailIdTxf = new JTextField();
            emailIdTxf.setBounds(115, 350, 305, 20);
            emailIdTxf.setName(getEMailIDLbl().getText());
        }
        return emailIdTxf;
    }

    public JButton getOkBtn() {
        if (okBtn == null) {
            okBtn = new JButton();
            okBtn.setText("OK");
            okBtn.setBounds(10, 403, 93, 23);
        }
        return okBtn;
    }

    public JButton getCancelBtn() {
        if (cancelBtn == null) {
            cancelBtn = new JButton();
            cancelBtn.setText("Cancel");
            cancelBtn.setBounds(340, 400, 93, 23);
        }
        return cancelBtn;
    }

    private void setRequiredIcon(JLabel label) {
        if (label != null) {
            label.setHorizontalTextPosition(SwingConstants.LEFT);
            label.setIcon(requiredIcon);
        }
    }
}
