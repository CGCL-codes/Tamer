package com.empower.client.controller.file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.swing.JFileChooser;
import com.ecs.etrade.uiinterfaces.AttachmentEmailer;
import com.empower.client.utils.MessageDisplayUtils;
import com.empower.client.utils.ValidationUtil;
import com.empower.client.view.file.EmailFileFrame;
import com.empower.constants.AppClientConstants;
import com.empower.model.Customer;

public class SendEMailController {

    private EmailFileFrame currFrame;

    ResourceBundle resMsg = ResourceBundle.getBundle("com.empower.client.Messages");

    public SendEMailController() {
        super();
    }

    public SendEMailController(EmailFileFrame emailFileFrame) {
        currFrame = emailFileFrame;
        addListeners();
    }

    private void addListeners() {
        currFrame.getBrowseButton().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handleBrowseFileButton();
            }
        });
        currFrame.getSendEmailButton().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handleSendEmailButton();
            }
        });
        currFrame.getCancelButton().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                MessageDisplayUtils.handleCancelButton(currFrame);
            }
        });
    }

    private void handleBrowseFileButton() {
        JFileChooser filechooser = new JFileChooser();
        int returnVal = filechooser.showOpenDialog(currFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = filechooser.getSelectedFile();
            currFrame.getEmailFileTxf().setText(file.getAbsolutePath());
        }
    }

    public void handleSendEmailButton() {
        boolean isToValid = ValidationUtil.validateMandatoryFld(currFrame, currFrame.getToTxf());
        if (!isToValid) {
            currFrame.getToTxf().requestFocus();
            currFrame.getToTxf().setText(AppClientConstants.EMPTY_STRING);
            return;
        }
        boolean isEmailFileValid = ValidationUtil.validateMandatoryFld(currFrame, currFrame.getEmailFileTxf());
        if (!isEmailFileValid) {
            currFrame.getEmailFileTxf().requestFocus();
            currFrame.getEmailFileTxf().setText(AppClientConstants.EMPTY_STRING);
            return;
        }
        try {
            AttachmentEmailer emailer = new AttachmentEmailer();
            currFrame.getEmailFileTxf().getText();
            Customer customer = new com.empower.model.Customer();
            customer.setToAddress(currFrame.getToTxf().getText());
            customer.setCcAddress(currFrame.getCcTxf().getText());
            customer.setBccAddress(currFrame.getBccTxf().getText());
            customer.setSubject(currFrame.getSubjectTxf().getText());
            String filePath = currFrame.getEmailFileTxf().getText();
            customer.setFilePath(filePath);
            emailer.sendReceipt(customer);
            MessageDisplayUtils.displaySuccessMsg(currFrame, resMsg.getString("MAIL_SUCCESS"));
        } catch (Exception e) {
            e.printStackTrace();
            MessageDisplayUtils.displaySuccessMsg(currFrame, resMsg.getString("MAIL_FAILURE"));
        }
    }
}
