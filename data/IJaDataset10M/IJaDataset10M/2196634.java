package org.commsuite.ws.services;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javolution.util.FastTable;
import org.apache.log4j.Logger;
import org.commsuite.enums.Direction;
import org.commsuite.managers.ContentsManager;
import org.commsuite.managers.MessageManager;
import org.commsuite.managers.SentContentManager;
import org.commsuite.messaging.JMSMessageManager;
import org.commsuite.model.Contents;
import org.commsuite.model.Message;
import org.commsuite.model.SearchMessageModel;
import org.commsuite.model.SentContent;
import org.commsuite.model.ws.WSContents;
import org.commsuite.model.ws.WSMessage;
import org.commsuite.model.ws.WSSentContent;
import org.commsuite.util.SpringMiddlewareBeansConstants;
import org.commsuite.util.SpringMiddlewareContext;
import org.commsuite.ws.WebServiceException;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.IndexOutOfBoundsException;

/**
 * @since 1.0
 * @author Szymon Kuzniak
 */
public class MessagesService {

    /**
	 * TODO: JavaDoc
	 */
    private static final Comparator<Message> MESSAGES_COMPARATOR = new Comparator<Message>() {

        public int compare(Message m1, Message m2) {
            final int comparisonByLastProcessDate = m2.getLastProcessDate().compareTo(m1.getLastProcessDate());
            if (0 == comparisonByLastProcessDate) {
                final int comparisonBySendDate = m1.getSendDate().compareTo(m1.getSendDate());
                return comparisonBySendDate;
            } else {
                return comparisonByLastProcessDate;
            }
        }
    };

    private final MessageManager messageManager;

    private final JMSMessageManager jmsManager;

    private static final Logger logger = Logger.getLogger(MessagesService.class);

    private static MessagesService instance;

    /**
	 * TODO: JavaDoc
	 */
    public MessagesService() {
        messageManager = SpringMiddlewareContext.getMessageManager();
        jmsManager = SpringMiddlewareContext.getJMSManager();
    }

    /**
	 * TODO: JavaDoc
	 */
    public static MessagesService getMessagesService() {
        if (null == instance) {
            instance = new MessagesService();
        }
        return instance;
    }

    /**
	 * TODO: JavaDoc
	 */
    public int getMessagesSize(SearchMessageModel wsMessage) throws WebServiceException {
        final MessageManager msgManager = SpringMiddlewareContext.getMessageManager();
        if (null == wsMessage) {
            return msgManager.getMessages().size();
        } else {
            return msgManager.getSelectedMessages(wsMessage).size();
        }
    }

    /**
	 * TODO: JavaDoc
	 */
    public Collection<WSMessage> getMessagesSubset(int begin, int end) throws WebServiceException, IllegalArgumentException, IndexOutOfBoundsException {
        if (begin > end) throw new IllegalArgumentException("Begin index should be lower than end");
        final MessageManager msgManager = SpringMiddlewareContext.getMessageManager();
        final List<Message> mes = msgManager.getMessages();
        if (begin < 0 || end >= mes.size()) throw new IndexOutOfBoundsException("Index is out of bounds: begin:[" + begin + "]end:[" + end + "]size:[" + mes.size() + "]");
        sortMessagesList(mes);
        final Collection<WSMessage> result = new FastTable<WSMessage>();
        for (int i = begin; i <= end; i++) {
            result.add(mes.get(i));
        }
        return result;
    }

    private void sortMessagesList(final List<Message> list) {
        Collections.sort(list, MESSAGES_COMPARATOR);
    }

    /**
	 * TODO: JavaDoc
	 */
    public Collection<WSMessage> getAllMessages() throws WebServiceException {
        final MessageManager msgManager = SpringMiddlewareContext.getMessageManager();
        logger.debug("1. THIS CODE IS NEVER RUNNING - this mean that the problem is not in Hibernate mapping");
        final Collection<Message> mes = msgManager.getMessages();
        final Collection<WSMessage> result = new FastTable<WSMessage>();
        for (final Message message : mes) {
            result.add(message);
        }
        logger.debug("2. THIS CODE IS ALSO NEVER RUNNING - this mean that the problem is not in Hibernate mapping");
        logger.debug("3. msgs: " + mes);
        return result;
    }

    /**
	 * TODO: JavaDoc
	 */
    public Collection<WSMessage> getSelectedMessagesSubset(SearchMessageModel wsMessage, int begin, int end) throws WebServiceException, IllegalArgumentException, IndexOutOfBoundsException {
        if (begin > end) throw new IllegalArgumentException("Begin index should be lower than end");
        final MessageManager msgManager = SpringMiddlewareContext.getMessageManager();
        List<Message> messages = msgManager.getSelectedMessages(wsMessage);
        if (begin < 0 || end >= messages.size()) throw new IndexOutOfBoundsException("Index is out of bounds: begin:[" + begin + "]end:[" + end + "]size:[" + messages.size() + "]");
        sortMessagesList(messages);
        Collection<WSMessage> result = new FastTable<WSMessage>();
        for (int i = begin; i <= end; i++) {
            result.add(messages.get(i));
        }
        return result;
    }

    /**
	 * TODO: JavaDoc
	 */
    public Collection<WSMessage> getSelectedMessages(SearchMessageModel wsMessage) throws WebServiceException {
        final MessageManager msgManager = SpringMiddlewareContext.getMessageManager();
        Collection<Message> messages = msgManager.getSelectedMessages(wsMessage);
        Collection<WSMessage> result = new FastTable<WSMessage>();
        for (Message message : messages) {
            result.add(message);
        }
        return result;
    }

    /**
	 * TODO: JavaDoc
	 */
    public WSContents getContentsById(String id) throws WebServiceException {
        final ContentsManager contentsManager = SpringMiddlewareContext.getContentsManager();
        return contentsManager.getContents(id);
    }

    /**
	 * TODO: JavaDoc
	 */
    public Collection<WSSentContent> getSentContentsByMessage(String id) throws WebServiceException {
        final SentContentManager sentContentManager = SpringMiddlewareContext.getSentContentManager();
        final List<SentContent> sentContents = sentContentManager.getSentContentsByMessage(id);
        final List<WSSentContent> result = new FastTable<WSSentContent>();
        for (final SentContent content : sentContents) {
            result.add(content);
        }
        return result;
    }

    /**
	 * TODO: JavaDoc
	 */
    public Collection<WSContents> getContentsBySentContent(String id) throws WebServiceException {
        final ContentsManager contentsManager = SpringMiddlewareContext.getContentsManager();
        final List<Contents> contents = contentsManager.getContentsBySentContent(id);
        final List<WSContents> result = new FastTable<WSContents>();
        for (final Contents content : contents) {
            result.add(content);
        }
        logger.debug("getContentsBySentContent.size: " + result.size());
        return result;
    }

    /**
	 * TODO: JavaDoc
	 */
    public WSMessage getMessageById(String id) throws WebServiceException {
        final MessageManager msgManager = SpringMiddlewareContext.getMessageManager();
        return msgManager.getMessage(id);
    }

    /**
	 * TODO: JavaDoc
	 */
    public void resubmitMessageWithId(String messageId) throws WebServiceException {
        final Message msg = messageManager.getMessage(messageId);
        if (null == msg) {
            throw new WebServiceException("Could not find message with id: " + messageId);
        }
        if (Direction.INBOUND == msg.getDirection()) {
            jmsManager.sendMessage(SpringMiddlewareBeansConstants.M_SAP_IN_QUEUE_NAME, msg);
            logger.debug("Resubmitted inbound message: " + messageId);
        } else if (Direction.OUTBOUND == msg.getDirection()) {
            jmsManager.sendMessage(SpringMiddlewareBeansConstants.M_OUT_ROUTER_QUEUE_NAME, msg);
            logger.debug("Resubmitted outbound message: " + messageId);
        } else {
            throw new WebServiceException("Unsupproted message direction for message with id: " + messageId);
        }
    }

    public void deleteMessage(String id) throws WebServiceException {
        final MessageManager msgManager = SpringMiddlewareContext.getMessageManager();
        try {
            msgManager.deleteMessage(id);
        } catch (Throwable t) {
            throw new WebServiceException("Exception during deleting mesage");
        }
    }

    public Collection<WSMessage> getMessagesByServer(String serverId) throws WebServiceException {
        final MessageManager msgManager = SpringMiddlewareContext.getMessageManager();
        try {
            logger.debug("test1:: " + serverId);
            Collection<Message> mes = msgManager.getMessagesByServer(serverId);
            logger.debug("test2");
            Collection<WSMessage> result = new FastTable<WSMessage>();
            logger.debug("test3");
            for (Message m : mes) {
                result.add((WSMessage) m);
            }
            logger.debug("test4");
            return result;
        } catch (Throwable t) {
            throw new WebServiceException("Exception during receiving mesages", t);
        }
    }
}
