package org.zeroexchange.messaging.embedded.read;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.zeroexchange.dao.DAOFactory;
import org.zeroexchange.dao.message.MessageDAO;
import org.zeroexchange.data.SlicingDataSet;
import org.zeroexchange.dataset.SortDescriptor;
import org.zeroexchange.dataset.criteria.ProcessorAwareDataSet;
import org.zeroexchange.dataset.criteria.processor.sort.CriteriaSortingProcessorsFactory;
import org.zeroexchange.dataset.criteria.processor.sort.OrderData;
import org.zeroexchange.model.message.Message;
import org.zeroexchange.model.message.UserMessage;
import org.zeroexchange.model.user.Role;
import org.zeroexchange.model.user.User;

/**
 * The messages reader implementation.
 * 
 * @author black
 */
@Secured(Role.ACEGITOKEN_USER)
public class DAOMessageReader implements MessageReader {

    @Autowired
    private DAOFactory daoFactory;

    @Autowired
    private CriteriaSortingProcessorsFactory sortingProcessorsFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public SlicingDataSet<Message> getUserMessages(Integer userId, SortDescriptor sortDescriptor) {
        MessageDAO messageDAO = daoFactory.getDAOForEntity(Message.class);
        ProcessorAwareDataSet<Message> dataSet = messageDAO.getUserMessages(userId);
        if (sortDescriptor != null) {
            dataSet.setSortProcessor(sortingProcessorsFactory.getProcessor(sortDescriptor.getSortType()), new OrderData(sortDescriptor.getField(), sortDescriptor.isAsc(), sortDescriptor.getLang()));
        }
        return dataSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message getUserMessage(Integer messageId, Integer recepientId) {
        MessageDAO messageDAO = daoFactory.getDAOForEntity(UserMessage.class);
        Message message = messageDAO.getById(Message.class, messageId);
        User recipient = message.getRecipient();
        if (recipient == null || !recipient.getId().equals(recepientId)) {
            return null;
        }
        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int countUnread(Integer userId) {
        MessageDAO messageDAO = daoFactory.getDAOForEntity(UserMessage.class);
        return messageDAO.countUnreadMessages(userId);
    }
}
