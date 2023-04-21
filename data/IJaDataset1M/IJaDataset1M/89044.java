package org.jamba.jms;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jamba.IQueueManager;
import org.jamba.JambaManager;
import org.jamba.channel.JambaChannelFactory;
import org.jamba.config.Queue;
import org.jgroups.Channel;
import org.jgroups.ChannelClosedException;
import org.jgroups.ChannelException;
import org.jgroups.ChannelNotConnectedException;
import org.jgroups.JChannelFactory;
import org.jgroups.blocks.DistributedHashtable;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

public class JMSUtil {

    private static Log log = LogFactory.getLog(JMSUtil.class);

    private static final int MESSAGE_BATCH_SIZE = 100;

    public static void processMessages(Map channelMap, Queue thisQueueConfig) {
        String queueName = thisQueueConfig.getLocalQueueName();
        Context jndiContext = null;
        QueueConnectionFactory queueConnectionFactory = null;
        QueueConnection queueConnection = null;
        QueueSession queueSession = null;
        javax.jms.Queue queue = null;
        QueueReceiver queueReceiver = null;
        Message message = null;
        DistributedHashtable controlTable = (DistributedHashtable) channelMap.get(JambaManager.CONTROL_CHANNEL_NAME);
        Channel thisQueuesChannel = (Channel) channelMap.get(queueName);
        try {
            thisQueuesChannel.connect(queueName);
        } catch (ChannelClosedException e1) {
            log.error("Could not connect to channel: ", e1);
            return;
        } catch (ChannelException e1) {
            log.error("Could not connect to channel: ", e1);
            return;
        }
        IQueueManager thisQueuesManager = (IQueueManager) controlTable.get(queueName);
        try {
            jndiContext = new InitialContext();
        } catch (NamingException e) {
            log.error("Could not create JNDI API context: " + e.toString(), e);
            return;
        }
        try {
            queueConnectionFactory = (QueueConnectionFactory) jndiContext.lookup("QueueConnectionFactory");
            queue = (javax.jms.Queue) jndiContext.lookup(queueName);
        } catch (NamingException e) {
            log.error("JNDI API lookup failed: " + e.toString(), e);
            return;
        }
        try {
            queueConnection = queueConnectionFactory.createQueueConnection();
            queueSession = queueConnection.createQueueSession(true, Session.SESSION_TRANSACTED);
            queueReceiver = queueSession.createReceiver(queue);
            queueConnection.start();
            for (int i = 0; i < MESSAGE_BATCH_SIZE; i++) {
                Serializable m = (Serializable) queueReceiver.receive(100);
                if (m != null) {
                    try {
                        thisQueuesChannel.send(thisQueuesManager.getNextSubscriber(), thisQueuesChannel.getLocalAddress(), m);
                    } catch (ChannelNotConnectedException e) {
                        log.error("Channel Not Connected Can't Send: " + e.toString(), e);
                        return;
                    } catch (ChannelClosedException e) {
                        log.error("Channel Already Closed Can't Send: " + e.toString(), e);
                        return;
                    }
                } else {
                    break;
                }
            }
        } catch (JMSException e) {
            log.error("Exception occurred: " + e.toString(), e);
            return;
        } finally {
            if (thisQueuesChannel.isConnected()) {
                thisQueuesChannel.disconnect();
            }
            if (queueConnection != null) {
                try {
                    queueConnection.close();
                } catch (JMSException e) {
                }
            }
        }
    }

    public static void putMessageInQueue(String queueName, org.jgroups.Message jGroupMsg) {
        Context jndiContext = null;
        QueueConnectionFactory queueConnectionFactory = null;
        QueueConnection queueConnection = null;
        QueueSession queueSession = null;
        javax.jms.Queue queue = null;
        QueueSender queueSender = null;
        Message message = null;
        message = (Message) jGroupMsg.getObject();
        try {
            jndiContext = new InitialContext();
        } catch (NamingException e) {
            log.error("Could not create JNDI API context: " + e.toString(), e);
            return;
        }
        try {
            queueConnectionFactory = (QueueConnectionFactory) jndiContext.lookup("QueueConnectionFactory");
            queue = (javax.jms.Queue) jndiContext.lookup(queueName);
        } catch (NamingException e) {
            log.error("JNDI API lookup failed: " + e.toString(), e);
            return;
        }
        try {
            queueConnection = queueConnectionFactory.createQueueConnection();
            queueSession = queueConnection.createQueueSession(true, Session.SESSION_TRANSACTED);
            queueSender = queueSession.createSender(queue);
            queueConnection.start();
            queueSender.send(message);
        } catch (JMSException e) {
            log.error("Exception occurred: " + e.toString(), e);
            return;
        } finally {
            if (queueConnection != null) {
                try {
                    queueConnection.close();
                } catch (JMSException e) {
                }
            }
        }
    }
}
