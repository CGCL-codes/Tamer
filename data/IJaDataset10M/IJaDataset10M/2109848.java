package pl.net.bluesoft.archetype.gwt.dao.query.interfaces;

/**
 * Interface for filter parameters implementing the 'empty', 'null', 'not empty', 'not null' conditions
 * @author mkrzempek, pstepaniak
 *
 */
public interface IHibernateNoneValueParameter extends IHibernateParameter {

    OperationType getOperationType();

    void setOperationType(OperationType operationType);

    /**
	 * Check if empty/null operations
	 * @author pstepaniak
	 *
	 */
    enum OperationType {

        EMPTY, NON_EMPTY, NULL, NOT_NULL
    }
}
