package org.qtitools.qti.value;

import org.qtitools.qti.exception.QTIBaseTypeException;

/**
 * Implementation of "bag-type" container.
 * <p>
 * This container can be multiple or NULL (if empty).
 *
 * @see org.qtitools.qti.value.Cardinality
 * 
 * @author Jiri Kajaba
 */
public class MultipleValue extends ListValue {

    private static final long serialVersionUID = 1L;

    /**
	 * Constructs empty (NULL) <code>MultipleValue</code> container.
	 */
    public MultipleValue() {
        super();
    }

    /**
	 * Constructs empty (NULL) <code>MultipleValue</code> container and adds given <code>SingleValue</code> into it.
	 *
	 * @param value added <code>SingleValue</code>
	 */
    public MultipleValue(SingleValue value) {
        super(value);
    }

    /**
	 * Constructs empty (NULL) <code>MultipleValue</code> container and adds all given <code>SingleValue</code>s into it.
	 *
	 * @param values added <code>SingleValue</code>s
	 */
    public MultipleValue(SingleValue[] values) {
        super(values);
    }

    /**
	 * Constructs empty (NULL) <code>MultipleValue</code> container and adds given <code>MultipleValue</code> into it.
	 *
	 * @param value added <code>MultipleValue</code>
	 */
    public MultipleValue(MultipleValue value) {
        super();
        add(value);
    }

    public Cardinality getCardinality() {
        if (isNull()) return null;
        return Cardinality.MULTIPLE;
    }

    @Override
    public boolean isOrdered() {
        return false;
    }

    /**
	 * Returns true if this container contains given <code>MultipleValue</code>; false otherwise.
	 *
	 * @param multipleValue given <code>MultipleValue</code>
	 * @return true if this container contains given <code>MultipleValue</code>; false otherwise
	 */
    public boolean contains(MultipleValue multipleValue) {
        if (multipleValue.isNull()) return false;
        for (SingleValue singleValue : multipleValue.container) if (multipleValue.count(singleValue) > count(singleValue)) return false;
        return true;
    }

    /**
	 * Adds <code>MultipleValue</code> into this container.
	 * <p>
	 * Takes all values from <code>MultipleValue</code> container and adds them into this container.
	 * <p>
	 * Multiple container can contain only values of the same <code>BaseType</code>.
	 * <p>
	 * NULL <code>MultipleValue</code> container is ignored.
	 *
	 * @param value added <code>MultipleValue</code>
	 * @return true if value was added; false otherwise
	 * @throws QTIBaseTypeException if <code>BaseType</code> is not same
	 */
    public boolean add(MultipleValue value) throws QTIBaseTypeException {
        if (value.isNull()) return false;
        if (!isNull() && getBaseType() != value.getBaseType()) throw new QTIBaseTypeException("Invalid baseType: " + value.getBaseType());
        return container.addAll(value.container);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (isNull() && (object instanceof Value) && ((Value) object).isNull()) return true;
        if (!getClass().equals(object.getClass())) return false;
        MultipleValue value = (MultipleValue) object;
        if (container.size() != value.container.size()) return false;
        return container.containsAll(value.container);
    }
}
