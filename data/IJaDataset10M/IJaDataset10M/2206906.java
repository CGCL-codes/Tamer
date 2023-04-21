package org.opencms.newsletter;

import org.opencms.file.CmsObject;
import org.opencms.file.CmsResource;
import org.opencms.main.CmsException;
import javax.mail.MessagingException;
import org.apache.commons.mail.Email;

/**
 * Interface for the newsletter.<p>
 */
public interface I_CmsNewsletter {

    /** Macro for the date. */
    String MACRO_SEND_DATE = "date";

    /** Macro for the email address. */
    String MACRO_USER_EMAIL = "email";

    /** Macro for the firstname. */
    String MACRO_USER_FIRSTNAME = "firstname";

    /** Macro for the full name. */
    String MACRO_USER_FULLNAME = "fullname";

    /** Macro for the lastname. */
    String MACRO_USER_LASTNAME = "lastname";

    /** 
     * Adds a OpenCms resource as an attachment to the newsletter.<p>
     * 
     * @param cms the CmsObject
     * @param resource the resource to attach
     * 
     * @throws CmsException if something goes wrong
     */
    void addAttachment(CmsObject cms, CmsResource resource) throws CmsException;

    /**
     * Adds content to the newsletter.<p>
     * 
     * @param content the content to add
     */
    void addContent(I_CmsNewsletterContent content);

    /**
     * Returns the newsletter as an e-mail to be sent.<p>
     * 
     * @param cms the CmsObject
     * @param recipient the recipient to which the newsletter will be sent
     * @return the newsletter as an e-mail
     * @throws MessagingException if something goes wrong
     * @throws CmsException if something goes wrong
     */
    Email getEmail(CmsObject cms, I_CmsNewsletterRecipient recipient) throws MessagingException, CmsException;

    /**
     * Sets the subject.<p>
     * 
     * @param subject the subject to set
     */
    void setSubject(String subject);
}
