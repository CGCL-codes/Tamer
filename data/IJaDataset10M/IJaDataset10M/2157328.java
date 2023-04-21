package edu.psu.citeseerx.messaging.messages;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import edu.psu.citeseerx.messaging.JMSSender;

/**
 * Wrapper for messages to indicate that a submission has started
 * 
 * @author Isaac Councill
 * @version $Rev$ $Date$
 *
 */
public class SubmissionStartedMsg extends SubmissionNotification {

    /**
     * Initializes a SubmissionStartedMsg with a raw MapMessage.
     * @param msg
     * @throws JMSException
     */
    public SubmissionStartedMsg(MapMessage msg) throws JMSException {
        super(JOBSTARTED, msg);
    }

    /**
     * Initializes a SubmissionStartedMsg with a utility for sending the 
     * message. The underlying MapMessage is generated from the sender.
     * @param sender
     * @throws JMSException
     */
    public SubmissionStartedMsg(JMSSender sender) throws JMSException {
        super(JOBSTARTED, sender);
    }

    /**
     * Initializes a SubmissionStartedMsg with a sender utility and type
     * specifier as well as all content fields.
     * @param sender
     * @param jobID
     * @param url
     * @param status
     * @throws JMSException
     */
    public SubmissionStartedMsg(JMSSender sender, String jobID, String url, int status) throws JMSException {
        super(JOBSTARTED, sender, jobID, url, status);
    }
}
