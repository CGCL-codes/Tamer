package org.dbe.demos.emailservice.proxyimpl.spamming;

import javax.mail.MessagingException;
import javax.xml.rpc.holders.BooleanHolder;
import org.dbe.demos.emailservice.proxyimpl.EmailServiceClient;

/**
 * All spamming decorator extend this class. It implements the common contract.
 *
 * @author <a href="mailto:Dominik.Dahlem@cs.tcd.ie">Dominik Dahlem</a>
 * @see org.dbe.demos.emailservice.proxyimpl.EmailServiceWorkspace
 */
public abstract class AbstractDecorator extends EmailServiceClient {

    private EmailServiceClient emailService;

    /**
     * Decorate an <code>EmailServiceWorkspace</code>.
     *
     * @param emailService the email service to be decorated
     */
    protected AbstractDecorator(final EmailServiceClient emailService) {
        this.emailService = emailService;
    }

    /**
     * @throws MessagingException
     * @see org.dbe.demos.emailservice.proxyimpl.RemoteEmailService#sendMail(
     *      java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public void sendMail(final String addressTo, final String subject, final String msg, final String addressFrom, final BooleanHolder successful) throws Exception {
        if (!(isValidAddress(addressTo) && isValidAddress(addressFrom))) {
            throw new MessagingException();
        }
        emailService.sendMail(addressTo, subject, msg, addressFrom, successful);
    }

    /**
     * Examines an email address to see whether it is valid. It checks whether an email
     * address contains an @ sign.
     * The calling method should throw an messaging exception if 'false' is returned.
     *
     * @param emailAddress      either the email address of the sender or receipent
     * @return  boolean         is valid or is not valid
     */
    protected abstract boolean isValidAddress(final String emailAddress);
}
