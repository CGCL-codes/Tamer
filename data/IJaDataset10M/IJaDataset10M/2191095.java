package net.sf.brightside.stockswatcher.server.service.hibernate;

import net.sf.brightside.stockswatcher.server.metamodel.User;
import net.sf.brightside.stockswatcher.server.metamodel.beans.UserBean;
import net.sf.brightside.stockswatcher.server.service.api.hibernate.ILogIn;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

public class LogInImpl extends HibernateDaoSupport implements ILogIn {

    private Session getManager() {
        SessionFactory sessionFactory = getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        return session;
    }

    @Transactional
    public User verifyUser(String username, String password) {
        Criteria criteria = getManager().createCriteria(UserBean.class);
        criteria.add(Restrictions.eq("username", username));
        criteria.add(Restrictions.eq("password", password));
        User user = null;
        try {
            user = (User) criteria.uniqueResult();
        } catch (Exception e) {
        }
        return user;
    }
}
