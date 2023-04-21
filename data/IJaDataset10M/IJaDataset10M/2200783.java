package org.pprun.hjpetstore.service.jms.supplier;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.support.converter.MessageConversionException;

/**
 * This is a mocked listener for suppliers that listening on the hjpetstore order backed topic.
 *
 * @author <a href="mailto:quest.run@gmail.com">pprun</a>
 */
public class EastPetCompanyBackOrderedTopicMessageListener implements MessageListener {

    private static final Log log = LogFactory.getLog(EastPetCompanyBackOrderedTopicMessageListener.class);

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof MapMessage) {
                MapMessage msg = (MapMessage) message;
                String itemName = msg.getString("item.name");
                int count = msg.getInt("count");
                log.info("-------- EastPetCompanyBackOrderedTopicMessageListener ---------");
                log.info("itemName: " + itemName);
                log.info("count: " + count);
                log.info("----------------------------------------------");
            } else {
                throw new UnsupportedOperationException("Message Type has not supported yet.");
            }
        } catch (JMSException ex) {
            log.error("cannot convert the message", ex);
            throw new MessageConversionException("cannot convert the message", ex);
        }
    }
}
