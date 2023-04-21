package com.sl.eventlog.dao.user;

import com.sl.eventlog.domain.user.User;
import com.sl.eventlog.dao.HibernateBaseDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.apache.commons.collections.CollectionUtils;
import java.util.List;

public class UserDaoImpl extends HibernateBaseDao<User> implements UserDao {

    public User getByUserName(String userName) {
        User user = null;
        DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
        criteria.add(Restrictions.eq("username", userName));
        List<User> users = getHibernateTemplate().findByCriteria(criteria);
        if (CollectionUtils.isNotEmpty(users)) {
            user = users.get(0);
        }
        return user;
    }
}
