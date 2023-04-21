package org.opensource.bbs.test.beanmanager;

import java.util.List;
import org.opensource.bbs.test.bean.User;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class UserManager extends HibernateDaoSupport implements Manager {

    public List<User> listUser(String name) {
        List<User> list = this.getHibernateTemplate().find("SELECT u FROM User u WHERE u.name=\'" + name + "\'");
        return list;
    }
}
