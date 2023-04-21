package hermes.store;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Topic;

/**
 * Used for storing destinations that exist in both domains - such as WebLogic.
 * 
 * @author colincrist@hermesjms.com
 * @version $Id: MessageStoreQueueTopic.java,v 1.1 2005/08/14 16:33:38 colincrist Exp $
 */
public class MessageStoreQueueTopic implements Queue, Topic {

    private String name;

    public MessageStoreQueueTopic(String name) {
        this.name = name;
    }

    public String getTopicName() throws JMSException {
        return name;
    }

    public String getQueueName() throws JMSException {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            if (obj instanceof MessageStoreQueueTopic) {
                return name.equals(((MessageStoreQueueTopic) obj).getQueueName());
            } else {
                return false;
            }
        } catch (JMSException ex) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
