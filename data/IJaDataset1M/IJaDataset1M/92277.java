package com.centraview.preference.mail;

import java.io.IOException;
import javax.mail.MessagingException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.mail.Mail;
import com.centraview.mail.MailAccountVO;
import com.centraview.mail.MailHome;
import com.centraview.preference.common.AdminConstantKeys;
import com.centraview.settings.Settings;

/**
 * Handles the request for SaveAccount.do which takes the
 * input from the email account details screen, and updates
 * that email account record in the database.
 */
public class SaveAccountHandler extends Action {

    private static Logger logger = Logger.getLogger(SaveAccountHandler.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException {
        String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
        request.setAttribute(AdminConstantKeys.PREFERENCEPAGE, "EMAIL");
        HttpSession session = request.getSession();
        UserObject userObject = (UserObject) session.getAttribute("userobject");
        int individualID = userObject.getIndividualID();
        ActionErrors allErrors = new ActionErrors();
        String forward = ".forward.preference.mail.account_list";
        String errorForward = ".view.preference.mail.view_account";
        DynaActionForm accountForm = (DynaActionForm) form;
        MailHome home = (MailHome) CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");
        try {
            Integer accountID = (Integer) accountForm.get("accountID");
            if (accountID == null || accountID.intValue() <= 0) {
                allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Account ID"));
                return mapping.findForward(forward);
            }
            Mail remote = (Mail) home.create();
            remote.setDataSource(dataSource);
            MailAccountVO accountVO = remote.getMailAccountVO(accountID.intValue());
            String accountName = (String) accountForm.get("accountName");
            if (accountName == null || accountName.length() <= 0) {
                allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Account Name"));
            }
            accountVO.setAccountName(accountName);
            String emailAddress = (String) accountForm.get("emailAddress");
            if (emailAddress == null) {
                allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Email Address"));
            } else if (!CVUtility.isEmailAddressValid(emailAddress)) {
                allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.email.invalidAddress", emailAddress));
            }
            accountVO.setEmailAddress(emailAddress);
            Boolean leaveOnServerForm = (Boolean) accountForm.get("leaveOnServer");
            boolean leaveOnServer = false;
            if (leaveOnServerForm != null && leaveOnServerForm.booleanValue() == true) {
                leaveOnServer = true;
            }
            accountVO.setLeaveMessagesOnServer(leaveOnServer);
            String username = (String) accountForm.get("username");
            if (username == null || username.length() <= 0) {
                allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "User Name (login)"));
            }
            accountVO.setLogin(username);
            String password = (String) accountForm.get("password");
            if (password == null || password.length() <= 0) {
                allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Password"));
            }
            accountVO.setPassword(password);
            String mailServer = (String) accountForm.get("mailServer");
            if (mailServer == null || mailServer.length() <= 0) {
                allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Mail Server Address"));
            }
            accountVO.setMailServer(mailServer);
            String replyTo = (String) accountForm.get("replyTo");
            accountVO.setReplyToAddress(replyTo);
            String signature = (String) accountForm.get("signature");
            accountVO.setSignature(signature);
            Integer smtpPort = (Integer) accountForm.get("port");
            if (smtpPort == null || smtpPort.intValue() <= 0) {
                allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "SMTP Port"));
            }
            accountVO.setSmtpPort(smtpPort.intValue());
            String smtpServer = (String) accountForm.get("smtpServer");
            if (smtpServer == null || smtpServer.length() <= 0) {
                allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "SMTP Server"));
            }
            accountVO.setSmtpServer(smtpServer);
            accountVO.setAuthenticationRequiredForSMTP(false);
            Boolean authenticationRequiredForSMTP = (Boolean) accountForm.get("authenticationRequiredForSMTP");
            if (authenticationRequiredForSMTP != null) {
                if (authenticationRequiredForSMTP.booleanValue()) {
                    accountVO.setAuthenticationRequiredForSMTP(true);
                }
            }
            if (!allErrors.isEmpty()) {
                saveErrors(request, allErrors);
                return mapping.findForward(forward);
            }
            try {
                remote.updateEmailAccount(accountVO);
            } catch (MessagingException me) {
                String errorMessage = me.getMessage();
                allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm", "Error while updating email account, " + errorMessage));
                saveErrors(request, allErrors);
                return mapping.findForward(errorForward);
            }
        } catch (Exception e) {
            logger.error("[Exception] SaveAccountHandler.Execute Handler ", e);
            allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm", "Could not update email account information. Please check your values and try again."));
            saveErrors(request, allErrors);
            forward = errorForward;
        }
        return mapping.findForward(forward);
    }
}
