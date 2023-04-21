package pl.edu.agh.iosr.ftpserverremote.dataaccess;

import java.sql.SQLException;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import pl.edu.agh.iosr.ftpserverremote.data.Message;

/**
 * This is a Hibernate DAO for <code>Message</code>.
 * 
 * @author Tomasz Sadura
 *
 */
public class MessageHibernateDAO extends GenericHibernateDAO<Message> {

    /**
   * Creates a new <code>MessageHibernateDAO</code>.
   */
    public MessageHibernateDAO() {
        super(Message.class);
    }

    @Override
    public Message getByName(final String name) throws DaoException, EntityNotFoundException {
        if (name == null) {
            throw new EntityNotFoundException("Name is null");
        }
        Message result = null;
        try {
            result = (Message) getHibernateTemplate().execute(new HibernateCallback() {

                public Object doInHibernate(final Session session) throws HibernateException, SQLException {
                    final Criteria criteria = session.createCriteria(getEntityClass());
                    criteria.add(Restrictions.eq("name", name));
                    return criteria.uniqueResult();
                }
            });
        } catch (final DataAccessException e) {
            throw new DaoException(e);
        }
        if (result == null) {
            throw new EntityNotFoundException("no Message with Name " + name);
        }
        return result;
    }
}
