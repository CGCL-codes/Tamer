package org.monet.manager.control.actions;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.monet.manager.configuration.Configuration;
import org.monet.manager.control.constants.Actions;
import org.monet.manager.control.constants.Parameter;
import org.monet.manager.core.constants.ErrorCode;
import org.monet.manager.core.constants.MessageCode;
import org.monet.manager.core.model.Language;
import org.monet.manager.presentation.user.constants.Views;
import org.monet.manager.presentation.user.views.ViewMail;
import org.monet.kernel.exceptions.DataException;
import org.monet.kernel.library.LibraryString;
import org.monet.kernel.model.Account;
import org.monet.kernel.model.User;
import org.monet.kernel.model.UserInfo;

public class ActionDoCreateAccount extends Action {

    private static final String MANAGER_TEMPLATES_MAIL_TEXT = "/manager/templates/mail.text.tpl";

    private static final String MANAGER_TEMPLATES_MAIL_HTML = "/manager/templates/mail.html.tpl";

    public ActionDoCreateAccount() {
        super();
    }

    private String sendMail(User oUser, String sPassword) {
        Configuration oConfiguration = Configuration.getInstance();
        ViewMail oViewMail;
        String sHtmlBody, sTextBody;
        oViewMail = (ViewMail) this.viewsFactory.get(Views.MAIL, this.codeLanguage);
        oViewMail.setTemplate(MANAGER_TEMPLATES_MAIL_HTML);
        oViewMail.setUser(oUser);
        oViewMail.setPassword(sPassword);
        sHtmlBody = oViewMail.execute();
        oViewMail.setTemplate(MANAGER_TEMPLATES_MAIL_TEXT);
        sTextBody = oViewMail.execute();
        HtmlEmail oEmail = new HtmlEmail();
        try {
            oEmail.setHostName(oConfiguration.getValue(Configuration.MAIL_HOST));
            oEmail.setFrom(oConfiguration.getValue(Configuration.MAILS_FROM));
            oEmail.addTo(oUser.getInfo().getEmail());
            oEmail.setSubject(Language.getInstance().getMessage(MessageCode.MAIL_NEW_USER_TITLE, this.codeLanguage));
            oEmail.setHtmlMsg(sHtmlBody);
            oEmail.setTextMsg(sTextBody);
            oEmail.send();
        } catch (EmailException oException) {
            this.agentException.error(oException);
            return Language.getInstance().getMessage(MessageCode.USER_CREATED_BUT_EMAIL_FAILED, this.codeLanguage);
        }
        return null;
    }

    public String execute() {
        String code = this.request.getParameter(Parameter.NAME);
        String sEmail = this.request.getParameter(Parameter.EMAIL);
        String codeNodeType = this.request.getParameter(Parameter.DEFINITION);
        String Type = this.request.getParameter(Parameter.TYPE);
        String sPassword = LibraryString.generatePassword();
        String sendEmailString = this.request.getParameter(Parameter.SEND_EMAIL);
        Account oAccount;
        UserInfo oUserInfo = new UserInfo();
        String sMessage;
        Boolean sendEmail = false;
        if (!this.kernel.isLogged()) {
            return ErrorCode.USER_NOT_LOGGED;
        }
        if ((code == null) || (sEmail == null) || (codeNodeType == null)) {
            throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.CREATE_ACCOUNT);
        }
        oUserInfo.setEmail(sEmail);
        oUserInfo.setFullname(code);
        oAccount = this.kernel.createAccount(code, sPassword, codeNodeType, oUserInfo, Type);
        sMessage = "";
        try {
            sendEmail = Boolean.parseBoolean(sendEmailString);
            if (sendEmail) {
                sMessage = this.sendMail(oAccount.getUser(), sPassword);
                if (sMessage == null) sMessage = Language.getInstance().getMessage(MessageCode.ACCOUNT_CREATED, this.codeLanguage);
            }
        } catch (Exception ex) {
            this.agentException.error(ex);
        }
        return sMessage;
    }
}
