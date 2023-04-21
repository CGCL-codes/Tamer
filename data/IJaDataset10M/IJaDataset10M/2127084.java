package org.monet.backoffice.control.actions;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.monet.backoffice.control.constants.Actions;
import org.monet.backoffice.control.constants.Parameter;
import org.monet.backoffice.core.constants.ErrorCode;
import org.monet.backoffice.core.constants.MessageCode;
import org.monet.backoffice.core.model.Language;
import org.monet.backoffice.library.LibraryRequest;
import org.monet.backoffice.presentation.user.constants.Views;
import org.monet.backoffice.presentation.user.views.ViewMail;
import org.monet.kernel.exceptions.DataException;
import org.monet.kernel.exceptions.SystemException;

public class ActionDoSendMail extends Action {

    private static final String BACKOFFICE_TEMPLATES_MAIL_TEXT = "/backoffice/templates/mail.text.tpl";

    private static final String BACKOFFICE_TEMPLATES_MAIL_HTML = "/backoffice/templates/mail.html.tpl";

    public ActionDoSendMail() {
        super();
    }

    public String execute() {
        org.monet.kernel.configuration.Configuration oMonetConfiguration = org.monet.kernel.configuration.Configuration.getInstance();
        String sSubject = LibraryRequest.getParameter(Parameter.SUBJECT, this.request);
        String sBody = LibraryRequest.getParameter(Parameter.BODY, this.request);
        ViewMail oViewMail;
        String sHtmlBody, sTextBody;
        if (!this.componentBackAccountsManager.isLogged()) {
            return ErrorCode.USER_NOT_LOGGED;
        }
        if ((sSubject == null) || (sBody == null)) {
            throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.SEND_MAIL);
        }
        oViewMail = (ViewMail) this.viewsFactory.get(Views.MAIL, this.agentRender, this.codeLanguage);
        oViewMail.setTemplate(BACKOFFICE_TEMPLATES_MAIL_HTML);
        oViewMail.setSubject(sSubject);
        oViewMail.setBody(sBody);
        sHtmlBody = oViewMail.execute();
        oViewMail.setTemplate(BACKOFFICE_TEMPLATES_MAIL_TEXT);
        sTextBody = oViewMail.execute();
        HtmlEmail oEmail = new HtmlEmail();
        try {
            oEmail.setHostName(oMonetConfiguration.getValue(org.monet.kernel.configuration.Configuration.MAIL_ADMIN_HOST));
            oEmail.setFrom(oMonetConfiguration.getValue(org.monet.kernel.configuration.Configuration.MAIL_ADMIN_FROM));
            oEmail.addTo(oMonetConfiguration.getValue(org.monet.kernel.configuration.Configuration.MAIL_ADMIN_TO));
            oEmail.setSubject(sSubject);
            oEmail.setHtmlMsg(sHtmlBody);
            oEmail.setTextMsg(sTextBody);
            oEmail.send();
        } catch (EmailException oException) {
            throw new SystemException(ErrorCode.SEND_MAIL, sSubject, oException);
        }
        return Language.getInstance().getMessage(MessageCode.MAIL_SENT);
    }
}
