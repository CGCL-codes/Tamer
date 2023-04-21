package com.realtournament.service.dao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.Predicate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import com.realtournament.model.User;
import com.realtournament.service.dao.definition.IUserDAO;

@Repository("userDAO")
@Scope("session")
public class UserDAOHibernate extends HibernateDaoSupport implements IUserDAO {

    @Autowired
    public UserDAOHibernate(HibernateTemplate hibernateTemplate) {
        this.setHibernateTemplate(hibernateTemplate);
    }

    public List<User> getAllUser(Comparator beanComparator, int rowIndex, int rowCount) {
        try {
            List l = getHibernateTemplate().find("from player order by " + ((BeanComparator) beanComparator).getProperty() + " ASC");
            return l;
        } catch (Exception e) {
            System.out.println("UserDaoHibernate: getAllUsers: throws " + e.toString());
            e.printStackTrace();
        }
        return new ArrayList();
    }

    public List<User> getAllUser(Comparator beanComparator) {
        return getAllUser(beanComparator, 0, 0);
    }

    public List<User> getList() {
        return getAllUser(new BeanComparator("id_player"), 0, 0);
    }

    public User getUser(Predicate predicate) {
        return null;
    }
}
