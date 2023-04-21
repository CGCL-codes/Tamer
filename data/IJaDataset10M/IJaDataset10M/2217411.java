package net.derquinse.common.orm.hib;

import org.hibernate.SessionFactory;

/**
 * Data access object for the sequence entity. As it represents a very specific case, it does not
 * follow normal best-practices (interface-based, etc.). WARNING: Transactional sequences are a
 * source of locks and may hurt scalability.
 * @author Andres Rodriguez
 */
public class HibernateSequenceDAO extends AbstractHibernateSequenceDAO<SequenceImpl> {

    /**
	 * Initialized the DAO
	 * @param sessionFactory Hibernate Session factory.
	 */
    public HibernateSequenceDAO(final SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected SequenceImpl create(String id, long initial, long increment) {
        return new SequenceImpl(id, initial, increment);
    }
}
