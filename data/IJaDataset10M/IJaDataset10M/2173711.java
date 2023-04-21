package com.hack23.cia.service.impl.commondao.impl;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;
import com.hack23.cia.model.api.common.Role;
import com.hack23.cia.model.impl.application.common.RegisteredUser;
import com.hack23.cia.model.impl.application.common.User;
import com.hack23.cia.service.api.common.GenericUserInterfaceLoaderService;
import com.hack23.cia.service.impl.commondao.api.UserDAO;

/**
 * The Class UserDAOImpl.
 */
public class UserDAOImpl extends GenericHibernateDAO<Role, User, Long> implements UserDAO, GenericUserInterfaceLoaderService<Role, User> {

    /**
     * Instantiates a new user dao impl.
     */
    public UserDAOImpl() {
        super(User.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final User findByUsername(final String username) {
        try {
            List<RegisteredUser> list = getSession().createCriteria(RegisteredUser.class).add(Restrictions.eq("username", username)).setCacheable(true).setMaxResults(1).list();
            if (list.size() > 0) {
                return list.get(0);
            } else {
                return null;
            }
        } catch (HibernateException hibernateException) {
            throw getHibernateTemplate().convertHibernateAccessException(hibernateException);
        }
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public final User getIfValidUser(final String username, final String encodedPassword) {
        DetachedCriteria findUserByName = DetachedCriteria.forClass(User.class);
        findUserByName.add(Restrictions.eq("username", username));
        findUserByName.add(Restrictions.eq("password", encodedPassword));
        List<User> result = this.getHibernateTemplate().findByCriteria(findUserByName);
        User user = null;
        if (!result.isEmpty()) {
            user = result.get(0);
        }
        return user;
    }
}
