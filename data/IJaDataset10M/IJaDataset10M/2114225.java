package net.sf.dropboxmq.messageconsumers;

import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;
import net.sf.dropboxmq.LogHelper;
import net.sf.dropboxmq.destinations.TopicImpl;
import net.sf.dropboxmq.sessions.SessionImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created: 11 Mar 2006
 *
 * @author <a href="mailto:dwayne@schultz.net">Dwayne Schultz</a>
 * @version $Revision: 231 $, $Date: 2011-08-12 23:50:47 -0400 (Fri, 12 Aug 2011) $
 */
public class TopicSubscriberImpl extends MessageConsumerImpl implements TopicSubscriber {

    private static final Log log = LogFactory.getLog(TopicSubscriberImpl.class);

    public TopicSubscriberImpl(final SessionImpl session, final TopicImpl topic, final String messageSelector, final String subscriptionName, final boolean durable, final boolean noLocal) throws JMSException {
        super(session, topic, messageSelector, subscriptionName == null ? session.getNewID() : subscriptionName, durable, noLocal);
        LogHelper.logMethod(log, toObjectString(), "TopicSubscriberImpl(), session = " + session + ", topic = " + topic + ", messageSelector = " + messageSelector + ", subscriptionName = " + subscriptionName + ", durable = " + durable + ", noLocal = " + noLocal);
    }

    public Topic getTopic() throws JMSException {
        LogHelper.logMethod(log, toObjectString(), "getTopic() = " + getDropbox().getDestination());
        return (Topic) getDropbox().getDestination();
    }

    public boolean getNoLocal() throws JMSException {
        return getDropbox().isNoLocal();
    }
}
