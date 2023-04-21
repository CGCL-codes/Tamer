package de.spotnik.mail.core.internal.persistence.hibernate;

import de.spotnik.mail.core.message.MessageTO;
import de.spotnik.mail.core.model.SpotnikMessage;
import de.spotnik.persistence.DatabaseException;
import de.spotnik.persistence.hibernate.AbstractDAO;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;

/**
 * MimeMessageDAO.
 * 
 * @author Jens Rehp�hler
 * @since 24.03.2006
 */
public class SpotnikMessageDAO extends AbstractDAO<SpotnikMessage> {

    /** the class logger. */
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(SpotnikMessageDAO.class);

    /**
     * Find all SpotnikMessages for the user account.
     * 
     * @param tagId the primary key of the tag
     * @return list of found messages
     */
    public List<SpotnikMessage> findByTag(Long tagId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("find message by user account: " + tagId);
        }
        Query q = this.createQuery("from SpotnikMessage m where m.tags.id = :id");
        q.setLong("id", tagId);
        return this.findList(q);
    }

    /**
     * Find a SpotnikMessage by the unique message id.
     * 
     * @param uniqueMessageId the unique id of the message
     * @return the message with the id
     * @throws DatabaseException
     */
    public SpotnikMessage findByUniqueId(String uniqueMessageId) throws DatabaseException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("find message by uniqueId: " + uniqueMessageId);
        }
        Query q = this.createQuery("from SpotnikMessage m where m.uniqueMessageId = :uniqueMessageId");
        q.setString("uniqueMessageId", uniqueMessageId);
        return this.find(q);
    }

    /**
     * Find all SpotnikMessages for the user account.
     * 
     * @param accountId the primary key of the user account
     * @return list of found messages
     */
    public List<MessageTO> findByUserAccount(Long accountId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("find message by user account: " + accountId);
        }
        Query q = this.createQuery("select new de.spotnik.mail.core.message.MessageTO( m.id, m.from, m.subject, m.sendOnDate, m.read, m.spam, m.hasAttachments)" + " from SpotnikMessage m where m.mailAccount.userAccount.id = :account");
        q.setLong("account", accountId);
        return this.findList(q);
    }

    @Override
    protected Class getEntityClass() {
        return SpotnikMessage.class;
    }
}
