package com.casheen.cxf.annotation.persistence;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import com.casheen.cxf.annotation.model.User;

@Repository
public class UserDaoImpl extends HibernateDaoSupport implements UserDao {

    @Resource
    private SessionFactory sessionFacotry;

    @PostConstruct
    public void injectSessionFactory() {
        super.setSessionFactory(sessionFacotry);
    }

    public void saveOrUpdate(User user) {
        super.getHibernateTemplate().saveOrUpdate(user);
    }

    public User get(Integer id) {
        return (User) super.getHibernateTemplate().get(User.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<User> list() {
        Criteria criteria = super.getSession().createCriteria(User.class);
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }
}
