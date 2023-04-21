package de.mogwai.common.business.service.impl;

import java.util.Locale;
import java.util.Map;
import de.mogwai.common.business.service.EMailException;
import de.mogwai.common.business.service.MailService;
import de.mogwai.common.business.service.TemplateMailService;
import de.mogwai.common.business.service.TemplateService;

/**
 * Implementierung des TemplateMailServices.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-06-17 15:06:41 $
 */
public class TemplateMailServiceImpl implements TemplateMailService {

    private TemplateService templateService;

    private MailService mailService;

    /**
     * Gibt den Wert des Attributs <code>templateService</code> zur�ck.
     * 
     * @return Wert des Attributs templateService.
     */
    public TemplateService getTemplateService() {
        return templateService;
    }

    /**
     * Setzt den Wert des Attributs <code>templateService</code>.
     * 
     * @param templateService
     *                Wert f�r das Attribut templateService.
     */
    public void setTemplateService(TemplateService templateService) {
        this.templateService = templateService;
    }

    /**
     * Gibt den Wert des Attributs <code>mailService</code> zur�ck.
     * 
     * @return Wert des Attributs mailService.
     */
    public MailService getMailService() {
        return mailService;
    }

    /**
     * Setzt den Wert des Attributs <code>mailService</code>.
     * 
     * @param aMailService
     *                Wert f�r das Attribut mailService.
     */
    public void setMailService(MailService aMailService) {
        this.mailService = aMailService;
    }

    /**
     * {@inheritDoc}
     */
    public void sendTextMessage(String aFrom, String[] aRecipients, String aTemplateName, Map<String, Object> aParams, Locale aLocale) throws EMailException {
        StringBuffer theBuffer = templateService.renderToString(aParams, aTemplateName, aLocale);
        int p = theBuffer.indexOf("\n");
        String theSubject = theBuffer.substring(0, p).trim();
        String theText = theBuffer.substring(p + 1).trim();
        mailService.sendTextMessage(theText, aFrom, aRecipients, theSubject);
    }
}
