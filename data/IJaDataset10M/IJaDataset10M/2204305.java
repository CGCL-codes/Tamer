package no.monsen.monsenservice.db.impl;

import no.monsen.monsenservice.db.*;
import no.monsen.monsenservice.impl.MonsenFact;
import org.hibernate.Session;

/**
 * 
 * @author geir.k.wollum
 * 28.02.2008
 */
public class MonsenDAOImpl implements MonsenDAO {

    @Override
    public void addVisitor(MonsenFact request) {
        int clientId = request.getClientID();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Visits visit = (Visits) session.get(Visits.class, request.getClientID());
        if (visit != null) {
            int count = visit.getCount();
            count += 1;
            visit.setCount(count);
            session.update(visit);
        } else {
            visit = new Visits();
            visit.setClientId(clientId);
            visit.setCount(1);
            session.save(visit);
        }
        session.getTransaction().commit();
    }

    @Override
    public MonsenFact getMonsenFact(int id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        if (id == 0) {
            long count = (Long) session.createQuery("select count(*) from Facts").uniqueResult();
            id = (int) Math.floor(Math.random() * count + 1);
        }
        Facts fact = null;
        do {
            fact = (Facts) session.load(Facts.class, id);
        } while (fact == null);
        int retrieved_count = fact.getRetrieved_count();
        retrieved_count += 1;
        fact.setRetrieved_count(retrieved_count);
        session.update(fact);
        session.getTransaction().commit();
        return new MonsenFact(fact.getId(), fact.getFact());
    }

    @Override
    public String checkVersion() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        String versjon = (String) session.createQuery("select cversion from Version").uniqueResult();
        return versjon;
    }
}
