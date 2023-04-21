package edu.psu.citeseerx.messaging.messages;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import edu.psu.citeseerx.messaging.JMSSender;

/**
 * Wrapper for messages that indicate that a submission job has completed
 * in its entirety (all sub-urls have been crawled).
 * 
 * @author Isaac Councill
 * @version $Rev: 860 $ $Date: 2009-01-02 12:56:26 -0500 (Fri, 02 Jan 2009) $
 *
 */
public class SubmissionJobCompleteMsg extends SubmissionNotification {

    /**
     * Initializes a SubmissionJobCompleteMsg with a raw MapMessage.
     * @param msg
     * @throws JMSException
     */
    public SubmissionJobCompleteMsg(MapMessage msg) throws JMSException {
        super(JOBCOMPLETION, msg);
    }

    /**
     * Initializes a SubmissionJobCompleteMsg with a utility for sending the 
     * message. The underlying MapMessage is generated from the sender.
     * @param sender
     * @throws JMSException
     */
    public SubmissionJobCompleteMsg(JMSSender sender) throws JMSException {
        super(JOBCOMPLETION, sender);
    }

    /**
     * Initializes a SubmissionJobCompleteMsg with a sender utility and type
     * specifier as well as all content fields.
     * @param sender
     * @param jobID
     * @param url
     * @param status
     * @throws JMSException
     */
    public SubmissionJobCompleteMsg(JMSSender sender, String jobID, String url, int status) throws JMSException {
        super(JOBCOMPLETION, sender, jobID, url, status);
    }
}
