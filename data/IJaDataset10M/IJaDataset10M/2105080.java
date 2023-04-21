package com.liferay.portal.audit.jms;

import com.liferay.portal.SystemException;
import com.liferay.util.JMSUtil;
import java.io.Serializable;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;

/**
 * <a href="CommandProducer.java.html"><b><i>View Source</i></b></a>
 *
 * @author Bruno Farache
 *
 */
public class CommandProducer {

    static {
        CommandConsumer consumer = new CommandConsumer();
        consumer.consume();
    }

    public static void produce(Serializable obj) throws SystemException {
        QueueConnection con = null;
        QueueSession session = null;
        QueueSender sender = null;
        try {
            QueueConnectionFactory qcf = CommandQCFUtil.getQCF();
            Queue queue = CommandQueueUtil.getQueue();
            con = qcf.createQueueConnection();
            session = con.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
            sender = session.createSender(queue);
            ObjectMessage objMsg = session.createObjectMessage();
            objMsg.setObject(obj);
            sender.send(objMsg);
        } catch (JMSException jmse) {
            throw new SystemException(jmse);
        } finally {
            JMSUtil.cleanUp(con, session, sender);
        }
    }
}
