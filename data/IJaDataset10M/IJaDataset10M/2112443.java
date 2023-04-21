package org.jopsdb.model.dao.jdbc;

import java.sql.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jopsdb.model.Observation;
import org.jopsdb.model.Server;
import org.jopsdb.model.dao.ObservationDAO;
import org.jzonic.dao.hibernate.AbstractHibernateDAO;
import org.jzonic.dao.hibernate.HibernateUtil;

public class HibernateObservationDAO extends AbstractHibernateDAO implements ObservationDAO {

    public void addObservation(Observation observation) {
        super.add(observation);
    }

    public void editObservation(Observation observation) {
        update(observation);
    }

    public void deleteObservation(Observation observation) {
        super.delete(observation);
    }

    public List findAll() {
        return find("from Observation");
    }

    public List findAll(String query) {
        return find("from Observation " + query);
    }

    public Observation getObservation(Long id) {
        try {
            return (Observation) findSingleResult("from Observation where id=" + id);
        } catch (Exception e) {
            return null;
        }
    }

    public List getRecentObservations(Server server, Date start, Date end) {
        Session session = HibernateUtil.getSession();
        Query query = session.createQuery("from Observation where date(created)>=:start and date(created)<=:end and serverid=:sid");
        query.setDate("start", start);
        query.setDate("end", end);
        query.setLong("sid", server.getId().longValue());
        return query.list();
    }
}
