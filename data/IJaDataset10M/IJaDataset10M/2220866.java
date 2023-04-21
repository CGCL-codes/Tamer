package org.nakedobjects.nof.reflect.spec;

import org.nakedobjects.noa.adapter.Naked;
import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.reflect.NakedObjectField;
import org.nakedobjects.noa.spec.NakedObjectSpecification;

public abstract class AbstractNakedObjectField extends AbstractNakedObjectMember implements NakedObjectField {

    private final NakedObjectSpecification specification;

    public AbstractNakedObjectField(final String fieldId, final NakedObjectSpecification specification) {
        super(fieldId);
        if (specification == null) {
            throw new IllegalArgumentException("field type for '" + fieldId + "' must exist");
        }
        this.specification = specification;
    }

    public abstract Naked get(final NakedObject fromObject);

    public Naked[] getOptions(final NakedObject target) {
        return new NakedObject[0];
    }

    /**
     * Return the specification of the object (or objects) that this field holds. For a value are one-to-one
     * reference this will be type that the accessor returns. For a collection it will be the type of element,
     * not the type of collection.
     */
    public NakedObjectSpecification getSpecification() {
        return specification;
    }

    /**
     * Returns true if this field is for a collection
     */
    public boolean isCollection() {
        return false;
    }

    /**
     * Returns true if this field is persisted, and not calculated from other data in the object or used transiently.
     */
    public abstract boolean isPersisted();

    public abstract boolean isEmpty(final NakedObject adapter);

    /**
     * Returns true if this field is for an object, not a collection.
     */
    public boolean isObject() {
        return false;
    }

    /**
     * Returns true if this field is for a value
     */
    public boolean isValue() {
        return false;
    }

    public boolean isMandatory() {
        return false;
    }
}
