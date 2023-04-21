package org.geergenbein.azw.dao.transcription;

import javax.persistence.EntityManager;
import org.geergenbein.azw.model.transcription.Dialect;
import org.geergenbein.dao.GenericHibernateDAO;

/**
 *
 * @author andrew
 */
public class DialectDAOHibernate extends GenericHibernateDAO<Dialect, Long> {

    public DialectDAOHibernate(EntityManager em) {
        super(em);
    }

    public Dialect find(String dialectName) {
        return (Dialect) getSession().getNamedQuery("findDialectByName").setParameter("name", dialectName).uniqueResult();
    }

    public Dialect loadOrPersist(String name) {
        Dialect dialect = null;
        if ((dialect = find(name)) == null) {
            dialect = new Dialect(name);
            makePersistent(dialect);
            log.debug("Persisted dialect: " + dialect);
        } else {
            log.debug("Found dialect: " + dialect);
        }
        return dialect;
    }
}
