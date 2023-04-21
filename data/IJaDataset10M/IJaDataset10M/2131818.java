package com.csft.market.domain;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.csft.hibernate.BaseHibernateDaoSupport;

@Repository
@Transactional
public class EmailDaoImpl extends BaseHibernateDaoSupport implements EmailDao {

    @Autowired
    public EmailDaoImpl(@Qualifier("sessionFactory") SessionFactory sf) {
        super();
        setSessionFactory(sf);
    }

    @Override
    public List<Email> listEmail() {
        Session s = getSession();
        Query q = s.createQuery("from Email");
        List<?> l = q.list();
        List<Email> es = new ArrayList<Email>();
        for (Object o : l) {
            es.add((Email) o);
        }
        return es;
    }

    @Override
    public void removeEmail(String email) {
        Session s = getSession();
        Query q = s.createQuery("from Email where email=:email");
        q.setParameter("email", email);
        List<?> l = q.list();
        for (Object o : l) {
            remove(o);
        }
    }
}
