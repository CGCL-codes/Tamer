package org.commonlibrary.lcms.security.dao.hibernate;

import org.commonlibrary.lcms.model.User;
import org.commonlibrary.lcms.security.dao.UserDAO;
import org.commonlibrary.lcms.support.dao.hibernate.HibernateCrudDao;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

/**
 * Hibernane implementation of <code>UserDao</code>
 * @author Jeff Wysong
 *         Date: Jul 29, 2008
 *         Time: 4:43:55 PM
 * @see org.commonlibrary.lcms.security.dao.UserDAO
 */
@Repository("userDAO")
public class HibernateUserDAO extends HibernateCrudDao<User, String> implements UserDAO {

    @Autowired
    public HibernateUserDAO(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
        setSessionFactory(sessionFactory);
    }

    /**
     * @see org.commonlibrary.lcms.security.dao.UserDAO#findUserByUsername(String)
     */
    public User findUserByUsername(String username) {
        String hql = "from CLv2User where username = ?";
        return (User) DataAccessUtils.singleResult(getHibernateTemplate().find(hql, username));
    }

    /**
     * @see org.commonlibrary.lcms.security.dao.UserDAO#doesEmailAddressExist(String)
     */
    public boolean doesEmailAddressExist(String emailAddress) {
        String hql = "from CLv2User where email = ?";
        return getHibernateTemplate().find(hql, emailAddress).size() > 0;
    }
}
