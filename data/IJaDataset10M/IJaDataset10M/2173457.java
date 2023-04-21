package net.sf.hibernate4gwt.sample.server.service.implementation;

import java.util.List;
import net.sf.hibernate4gwt.sample.domain.Message;
import net.sf.hibernate4gwt.sample.server.dao.IMessageDAO;
import net.sf.hibernate4gwt.sample.server.service.IMessageService;

/**
 * Implementation of the message service
 * @author bruno.marchesson
 *
 */
public class MessageService implements IMessageService {

    /**
	 * the associated DAO
	 */
    private IMessageDAO messageDAO;

    /**
	 * @return the messageDAO
	 */
    public IMessageDAO getMessageDAO() {
        return messageDAO;
    }

    /**
	 * @param messageDAO the messageDAO to set
	 */
    public void setMessageDAO(IMessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public Message loadLastMessage() {
        return messageDAO.loadLastMessage();
    }

    public List<Message> loadAllMessage(int startIndex, int maxResult) {
        return messageDAO.loadAllMessage(startIndex, maxResult);
    }

    public int countAllMessage() {
        return messageDAO.countAllMessages();
    }

    public Message loadMessageDetails(Message message) {
        return messageDAO.loadMessageAndUser(message.getId());
    }

    public void saveMessage(Message message) {
        messageDAO.saveMessage(message);
    }

    public void deleteMessage(Message message) {
        messageDAO.deleteMessage(message);
    }
}
