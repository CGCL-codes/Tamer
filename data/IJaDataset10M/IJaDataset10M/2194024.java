package org.codegallery.jpagal.service.impl;

import java.util.List;
import org.codegallery.jpagal.entity.Account;
import org.codegallery.jpagal.entity.Customer;
import org.codegallery.jpagal.service.AccountService;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class AccountServiceHbImpl implements AccountService {

    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Account save(Account account) {
        Session session = sessionFactory.getCurrentSession();
        if (account.getId() == null) {
            session.persist(account);
            return account;
        } else {
            return (Account) session.merge(account);
        }
    }

    public void insertFoo(Account foo) {
        throw new UnsupportedOperationException();
    }

    public List<Account> findByCustomer(Customer customer) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Account.class);
        criteria.add(Restrictions.eq("customer", customer));
        return criteria.list();
    }
}
