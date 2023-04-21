package ua.org.hatu.daos.engine.dao.hibernate;

import ua.org.hatu.daos.engine.dao.SubjectDao;
import ua.org.hatu.daos.engine.domain.Subject;

/**
 * TODO: javadocs 
 * 
 * @author zeus (alex.pogrebnyuk@gmail.com)
 * @author dmytro (pogrebniuk@gmail.com)
 *
 */
public class SubjectHibernateDao extends GenericHibernateDao<Subject, Long> implements SubjectDao {

    public SubjectHibernateDao() {
        super(Subject.class);
    }
}
