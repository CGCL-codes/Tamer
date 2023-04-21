package org.nakedobjects.persistence.nhibernate.resource;

import System.Type;
import NHibernate.ICriteria;
import NHibernate.IQuery;
import NHibernate.ISession;
import org.nakedobjects.persistence.nhibernate.HibernateUtil;
import org.nakedobjects.reflector.dotnet.resource.AbstractRepository;

public abstract class HibernateRepository extends AbstractRepository {

    protected final Type type;

    public HibernateRepository(final Type type) {
        super(type);
        this.type = type;
    }

    public HibernateRepository(final Type type, final boolean includeSubclasses) {
        super(type, includeSubclasses);
        this.type = type;
    }

    /**
	 * Create a Hibernate ICriteria for the class persisted by this repository 
	 */
    protected ICriteria createCriteria() {
        return HibernateUtil.getCurrentSession().CreateCriteria(type);
    }

    /**
	 * Create a Hibernate IQuery using a complete query string.  This can be used
	 * to query any entity.
	 */
    protected IQuery createQuery(final String query) {
        return HibernateUtil.getCurrentSession().CreateQuery(query);
    }

    /**
	 * Return a named Hibernate Query which is defined in hibernate.cfg.xml.
	 * @param name the Query name
	 */
    protected IQuery getNamedQuery(final String name) {
        return HibernateUtil.getCurrentSession().GetNamedQuery(name);
    }

    /**
	 * A shortcut for creating a count(*) IQuery on the class for this
	 * repository.  The query generated is
	 * <pre>select count(*) from <type> as o {where <whereClause>}</pre>
	 * The whereClause is optional.
	 * <p>To generate more complex queries, possibly using other classes, use
	 * the {@link #CreateQuery(String)}} method
	 * @param whereClause the where clause, not including "where", or null to count
	 * all instances.
	 */
    protected IQuery createCountQuery(final String whereClause) {
        String query;
        if (whereClause == null) {
            query = "select count(*) from " + type.get_FullName() + " as o";
        } else {
            query = "select count(*) from " + type.get_FullName() + " as o where " + whereClause;
        }
        return createQuery(query);
    }

    /**
	 * A shortcut for creating a IQuery on the class for this
	 * repository.  The query generated is
	 * <pre>from <type> as o where <whereClause></pre>
	 * The whereClause must be specified, to select all instances
	 * use {@link #allInstances()}.
	 * <p>To generate more complex queries, possibly using other classes, use
	 *  {@link #CreateQuery(String)}}.
	 * @param whereClause the where clause, not including "where"
	 */
    protected IQuery createEntityQuery(final String whereClause) {
        return createQuery("from " + type.get_FullName() + " as o where " + whereClause);
    }

    protected Object[] findByCriteria(final ICriteria criteria) {
        return findByCriteria(new HibernateCriteriaCriteria(getSpec(), criteria));
    }

    protected Object[] findByCriteria(final ICriteria criteria, final Type type) {
        return findByCriteria(new HibernateCriteriaCriteria(type, criteria), Class.FromType(type));
    }

    protected Object findFirstByCriteria(final ICriteria criteria) {
        criteria.SetMaxResults(1);
        return getFirst(findByCriteria(criteria));
    }

    protected Object findFirstByCriteria(final ICriteria criteria, final Type type) {
        criteria.SetMaxResults(1);
        return getFirst(findByCriteria(criteria, type));
    }

    protected Object[] findByQuery(final IQuery query) {
        return findByCriteria(new HibernateQueryCriteria(getSpec(), query));
    }

    protected Object[] findByQuery(final IQuery query, final Type type) {
        return findByCriteria(new HibernateQueryCriteria(type, query), Class.FromType(type));
    }

    protected Object findFirstByQuery(final IQuery query) {
        query.SetMaxResults(1);
        return getFirst(findByQuery(query));
    }

    protected Object findFirstByQuery(final IQuery query, final Type type) {
        query.SetMaxResults(1);
        return getFirst(findByQuery(query, type));
    }

    protected Object[] findByQuery(final String query) {
        return findByQuery(HibernateUtil.getCurrentSession().CreateQuery(query));
    }

    protected Object[] findByQuery(final String query, final Type type) {
        return findByQuery(HibernateUtil.getCurrentSession().CreateQuery(query), type);
    }

    protected Object findFirstByQuery(final String query) {
        return findFirstByQuery(HibernateUtil.getCurrentSession().CreateQuery(query));
    }

    protected Object findFirstByQuery(final String query, final Type type) {
        return findFirstByQuery(HibernateUtil.getCurrentSession().CreateQuery(query), type);
    }

    /**
	 * Return the fist object in an array.  If the array is null, or is empty,
	 * then return null. 
	 */
    protected Object getFirst(final Object[] array) {
        if (array == null || array.length == 0) {
            return null;
        }
        return array[0];
    }

    /**
	 * Get the Hibernate ISession, which can be used to create Queries and
	 * ICriteria for any entity.
	 */
    protected ISession getSession() {
        return HibernateUtil.getCurrentSession();
    }

    /**
	 * Return true if the IQuery returns an integer > 0.  The IQuery MUST return
	 * a single value, which must be an integer, e.g. "select count(*) from myType".
	 * This is a utility method to help with common repository usage.
	 */
    protected boolean countNotZero(final IQuery query) {
        int count = ((Integer) query.UniqueResult()).intValue();
        return count > 0;
    }
}
