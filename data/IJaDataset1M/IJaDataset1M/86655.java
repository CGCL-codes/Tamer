package com.ekeymanlib.dao;

import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.ekeymanlib.domain.User;

public class UserDaoImpl extends HibernateDaoSupport implements UserDao {

    public UserDaoImpl() {
    }

    public void saveUser(User user) {
        getHibernateTemplate().saveOrUpdate(user);
    }

    @SuppressWarnings("unchecked")
    public User getUser(String openidurl) {
        User user = null;
        String queryString = "from User u where u.openidurl = :openidurl " + "and u.disabled = 0 and u.vendor.disabled = 0";
        String[] paramNames = new String[1];
        paramNames[0] = "openidurl";
        Object[] values = new Object[1];
        values[0] = openidurl.toUpperCase();
        List<User> users = getHibernateTemplate().findByNamedParam(queryString, paramNames, values);
        if (users != null && !users.isEmpty()) {
            user = users.get(0);
        }
        return user;
    }

    public void deleteUser(String openidurl) {
        User user = getUser(openidurl);
        if (user != null) {
            user.setDisabled(1);
            saveUser(user);
        }
    }
}
