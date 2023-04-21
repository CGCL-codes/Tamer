package pl.net.bluesoft.archetype.gwt.dao.query.interfaces;

/**
 * Interface for filter parameters implementing comparision between two attributes
 * @author mkrzempek, pstepaniak
 *
 */
public interface IHibernatePropertyParameter extends IHibernateParameter {

    String getTargetPropertyName();

    void setTargetPropertyName(String propertyName);

    OperationType getOperationType();

    void setOperationType(OperationType operationType);

    /**
	 * Comparison operations
	 * @author pstepaniak
	 *
	 */
    enum OperationType {

        EQUAL, NOT_EQUAL
    }
}
