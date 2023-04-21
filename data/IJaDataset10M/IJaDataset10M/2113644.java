package com.geoservices.dao;

import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.geoservices.model.Group;
import com.geoservices.model.User;

public class UserDaoImpl extends HibernateDaoSupport implements UserDao {

    public void addUser(User user) {
        getHibernateTemplate().save(user);
        getHibernateTemplate().flush();
    }

    public User getUser(String login) {
        User user = (User) getHibernateTemplate().find("from User u where u.login=?", login).get(0);
        user.getGroups();
        getHibernateTemplate().flush();
        return user;
    }

    @SuppressWarnings("unchecked")
    public List<User> getUsers() {
        List<User> f = (List<User>) getHibernateTemplate().find("from User");
        getHibernateTemplate().flush();
        return f;
    }

    public void updateUser(User user) {
        getHibernateTemplate().update(user);
        getHibernateTemplate().flush();
    }

    public void deleteUser(User user) {
        getHibernateTemplate().delete(user);
        getHibernateTemplate().flush();
    }

    public User addGroupToUser(String login, Group group) {
        User user = getUser(login);
        user.addGroup(group);
        getHibernateTemplate().update(user);
        getHibernateTemplate().flush();
        return user;
    }
}
