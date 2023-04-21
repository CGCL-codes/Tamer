package nuts.core.orm.restriction;

import nuts.core.orm.dao.QueryParameter;

/**
 */
public class ObjectRestriction<E extends QueryParameter> extends AbstractRestriction<E> {

    /**
	 * @param example example
	 * @param name name
	 */
    public ObjectRestriction(E example, String name) {
        super(example, name);
    }

    /**
	 * @param example example
	 * @param property property
	 * @param column column
	 * @param alias alias
	 */
    public ObjectRestriction(E example, String property, String column, String alias) {
        super(example, property, column, alias);
    }

    /**
	 * @return example
	 */
    public E isNull() {
        example.getRestrictions().isNull(column);
        return example;
    }

    /**
	 * @return example
	 */
    public E isNotNull() {
        example.getRestrictions().isNotNull(column);
        return example;
    }
}
