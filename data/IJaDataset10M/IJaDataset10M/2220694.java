package org.opennms.netmgt.dao.hibernate;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.opennms.netmgt.EventConstants;
import org.opennms.netmgt.Pager;
import org.opennms.netmgt.dao.UserDao;
import org.opennms.netmgt.model.OnmsAuthority;
import org.opennms.netmgt.model.OnmsGroup;
import org.opennms.netmgt.model.OnmsGroupMembers;
import org.opennms.netmgt.model.OnmsUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * @author Massimiliano Dess&igrave; (desmax74@yahoo.it)
 * @since jdk 1.5.0
 */
@Repository("userRepository")
public class UserDaoHibernate extends HibernateDaoSupport implements UserDao {

    @SuppressWarnings("unchecked")
    public List<OnmsUser> getUsers(final Pager pager) {
        return (List) getHibernateTemplate().executeFind(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery("SELECT users FROM OnmsUser users WHERE users.username != 'admin'");
                int offset = pager.getPage() * pager.getItemsNumberOnPage();
                int limit = pager.getItemsNumberOnPage();
                if (offset > 0) query.setFirstResult(offset);
                if (limit > 0) query.setMaxResults(limit);
                return query.list();
            }
        });
    }

    public void disableUser(String username) {
        if (!username.equals(EventConstants.ADMIN)) {
            OnmsUser auth = (OnmsUser) getHibernateTemplate().get(OnmsUser.class, username);
            auth.setEnabled(false);
            getHibernateTemplate().saveOrUpdate(auth);
        }
    }

    public void enableUser(String username) {
        OnmsUser auth = (OnmsUser) getHibernateTemplate().get(OnmsUser.class, username);
        auth.setEnabled(true);
        getHibernateTemplate().saveOrUpdate(auth);
    }

    @SuppressWarnings("unchecked")
    public List<OnmsUser> getDisabledUsers(final Pager pager) {
        return (List) getHibernateTemplate().executeFind(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery("SELECT users FROM OnmsUser users WHERE enabled = false");
                int offset = pager.getPage() * pager.getItemsNumberOnPage();
                int limit = pager.getItemsNumberOnPage();
                if (offset > 0) query.setFirstResult(offset);
                if (limit > 0) query.setMaxResults(limit);
                return query.list();
            }
        });
    }

    @SuppressWarnings("unchecked")
    public List<OnmsUser> getEnabledUsers(final Pager pager) {
        return (List) getHibernateTemplate().executeFind(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery("SELECT users FROM OnmsUser users WHERE enabled = true");
                int offset = pager.getPage() * pager.getItemsNumberOnPage();
                int limit = pager.getItemsNumberOnPage();
                if (offset > 0) query.setFirstResult(offset);
                if (limit > 0) query.setMaxResults(limit);
                return query.list();
            }
        });
    }

    public OnmsUser getUser(String username) {
        return (OnmsUser) getHibernateTemplate().get(OnmsUser.class, username);
    }

    public Integer getUsersNumber() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("SELECT count(*) FROM OnmsUser"));
    }

    public OnmsUser getUserWithAuthorities(final String username) {
        return (OnmsUser) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                OnmsUser authentication = (OnmsUser) session.get(OnmsUser.class, username);
                Set<OnmsAuthority> authorities = new HashSet<OnmsAuthority>();
                Iterator<OnmsGroupMembers> groupsMember = authentication.getGroups().iterator();
                while (groupsMember.hasNext()) {
                    OnmsGroupMembers gm = groupsMember.next();
                    OnmsGroup group = gm.getGroup();
                    Iterator<OnmsAuthority> auths = group.getAuthorities().iterator();
                    while (auths.hasNext()) {
                        OnmsAuthority auth = auths.next();
                        authorities.add(auth);
                    }
                }
                authentication.setAuthorities(authorities);
                return authentication;
            }
        });
    }

    public void save(OnmsUser auth) {
        getHibernateTemplate().saveOrUpdate("authentication", auth);
    }

    @Autowired
    public void setupSessionFactory(SessionFactory sessionFactory) {
        this.setSessionFactory(sessionFactory);
    }
}
