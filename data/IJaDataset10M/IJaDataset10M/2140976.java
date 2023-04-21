package org.nakedobjects.object.reflect;

import org.nakedobjects.object.Naked;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectField;
import org.nakedobjects.object.NakedObjectSpecification;

public abstract class AbstractNakedObjectField extends AbstractNakedObjectMember implements NakedObjectField {

    private final NakedObjectSpecification specification;

    public AbstractNakedObjectField(String name, NakedObjectSpecification type) {
        super(name);
        if (type == null) {
            throw new IllegalArgumentException("type cannot be null for " + name);
        }
        this.specification = type;
    }

    public abstract Naked get(NakedObject fromObject);

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

    public abstract boolean isDerived();

    public abstract boolean isEmpty(NakedObject adapter);

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

    public abstract Class[] getExtensions();
}
