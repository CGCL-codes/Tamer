package pl.edu.agh.ssm.persistence.dao.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import pl.edu.agh.ssm.persistence.INotification;
import pl.edu.agh.ssm.persistence.IProperty;
import pl.edu.agh.ssm.persistence.IUser;
import pl.edu.agh.ssm.persistence.IWindow;
import pl.edu.agh.ssm.persistence.dao.NotificationAccessDAO;

public class NotificationAccessDAOImpl extends GenericHibernateDao<INotification> implements NotificationAccessDAO {

    public void delete(INotification entity) throws DataAccessException {
        throw new UnsupportedOperationException("Cannot delete notification");
    }

    public void deleteAll() {
        throw new UnsupportedOperationException("Cannot delete notification");
    }

    @SuppressWarnings("unchecked")
    public INotification findNotification(final IUser user, final IProperty properties) {
        return (INotification) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Criteria criteria = session.createCriteria(getEntityClass());
                criteria.add(Restrictions.eq("user", user));
                criteria.add(Restrictions.eq("property", properties));
                return criteria.uniqueResult();
            }
        });
    }

    @SuppressWarnings("unchecked")
    public List<INotification> findActiveNotification(final IProperty properties) {
        return (List<INotification>) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Criteria criteria = session.createCriteria(getEntityClass());
                criteria.add(Restrictions.eq("active", true));
                criteria.add(Restrictions.eq("property", properties));
                return criteria.list();
            }
        });
    }
}
