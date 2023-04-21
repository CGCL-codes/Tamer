package org.rpt.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.rpt.model.User;

public class UserDao {

    /**
   * 
   * <p>按照用户名与密码查找相应的用户信息</p>
   * <p>存在返回详细信息，不存在返回一个null</p>
   * @author Panxingyu
   * @param username 用户名
   * @param password  密码
   * @return 存在返回详细信息，不存在返回一个null
   */
    @SuppressWarnings("unchecked")
    public User findUuserByUsernameAndPassword(String username, String password) {
        Session session = HibernateSessionFactory.getSession();
        List<User> users = session.createCriteria(User.class).add(Restrictions.eq("username", username)).add(Restrictions.eq("password", password)).list();
        return (users.size() == 1) ? users.get(0) : null;
    }
}
