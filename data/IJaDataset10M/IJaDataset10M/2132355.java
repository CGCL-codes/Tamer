package eu.activelogic.mailparse;

import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

/**
 * 
 * A mail filtering interface to be implemented by
 * 
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public interface MailFilter {

    /**
     * This method is called before the parsing cycle.
     */
    public void cycleBegin();

    /**
     * This method is called after the parsing cycle.
     */
    public void cycleEnd();

    /**
     * Method is called on each message from the server. Before MIME Body parts processing.
     * 
     * @param msg
     *            the message that is being processed
     */
    public void filterStart(MimeMessage msg);

    /**
     * Method is called if any textual MIME body parts are found
     * 
     * @param msg
     *            all text message parts
     */
    public void filterText(String[] msg);

    /**
     * Method is called if any image MIME body parts are found
     * 
     * @param msg
     *            all image message parts
     */
    public void filterImages(MimeBodyPart[] msg);

    /**
     * Method is called on each message from the server. After MIME Body parts processing.
     * 
     * @param msg
     *            the message that is being processed
     */
    public void filterEnd(MimeMessage msg);
}
