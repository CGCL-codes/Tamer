package uk.org.ogsadai.activity.delivery;

import java.io.Reader;
import java.util.Collection;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;

/**
 * Interface for sending email messages through.
 * 
 * @author The OGSA-DAI Project Team
 */
interface EmailSender {

    /**
     * Sends an email message.
     * 
     * @param from
     *            from address
     * @param to
     *            a collection of recipients name strings
     * @param subject
     *            subject for the message
     * @param content
     *            content of the message
     * @param server
     *            mail server
     * @throws ActivityUserException
     *             if the settings specified by the user prevent processing from
     *             completing
     * @throws ActivityProcessingException
     *             if an internal error prevents processing from completing
     * @throws ActivityTerminatedException
     *             if activity processing is terminated at an intermediate
     *             stage
     */
    void sendMessage(String from, Collection to, String subject, Reader content, String server) throws ActivityProcessingException, ActivityTerminatedException, ActivityUserException;
}
