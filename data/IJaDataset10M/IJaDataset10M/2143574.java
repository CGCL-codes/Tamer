package org.nakedobjects.nos.store.hibernate.resource;

import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.persist.InstancesCriteria;
import org.nakedobjects.noa.spec.NakedObjectSpecification;
import org.nakedobjects.nof.core.context.NakedObjectsContext;
import java.util.List;

/**
 * Superclass of all InstancesCriteria which use Hibernate to access the
 * database.
 */
public abstract class HibernateInstancesCriteria implements InstancesCriteria {

    private final NakedObjectSpecification specification;

    public HibernateInstancesCriteria(final NakedObjectSpecification specification) {
        this.specification = specification;
    }

    public HibernateInstancesCriteria(final Class cls) {
        this.specification = NakedObjectsContext.getReflector().loadSpecification(cls);
    }

    public NakedObjectSpecification getSpecification() {
        return specification;
    }

    /**
	 * Not required as this will be in the Hibernate query/criteria
	 */
    public boolean includeSubclasses() {
        return false;
    }

    /**
	 * Not required as this will be decided by the Hibernate query/criteria
	 */
    public boolean matches(final NakedObject object) {
        return false;
    }

    /**
	 * Return the results of executing the Hibernate query.
	 * @return a List of persistent Objects
	 */
    public abstract List getResults();
}
