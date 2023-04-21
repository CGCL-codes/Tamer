package de.cue4net.eventservice.controller.email;

import de.cue4net.eventservice.model.email.EmailTemplate;

/**
 * EmailTemplateForm
 *
 * @author Thorsten Vogel
 * @version $Id: EmailTemplateForm.java,v 1.2 2008-06-05 11:00:07 keino Exp $
 */
public class EmailTemplateForm {

    private EmailTemplate emailTemplate;

    public EmailTemplate getEmailTemplate() {
        return this.emailTemplate;
    }

    public void setEmailTemplate(EmailTemplate emailTemplate) {
        this.emailTemplate = emailTemplate;
    }
}
