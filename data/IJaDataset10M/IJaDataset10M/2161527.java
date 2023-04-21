package pl.net.bluesoft.archetype.gwt.dao.query.interfaces;

import java.util.List;

/**
 * Interface for filter parameters implementing the 'in' or 'not in' condition
 * @author mkrzempek, pstepaniak
 *
 */
public interface IHibernateCollectionValueParameter extends IHibernateParameter {

    List<?> getValueCollecion();

    void setValueCollection(List<?> valueCollection);

    OperationType getOperationType();

    void setOperationType(OperationType operationType);

    /**
	 * Collection operation type
	 * @author pstepaniak
	 *
	 */
    enum OperationType {

        IN, NOT_IN
    }
}
