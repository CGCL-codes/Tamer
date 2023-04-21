package org.openconcerto.utils.change;

import org.openconcerto.utils.ExceptionUtils;
import java.lang.reflect.Constructor;
import java.util.Collection;

/**
 * A creator which clones by calling the Collection constructor of the passed instance.
 * 
 * @author Sylvain CUAZ
 */
public class ConstructorCreator extends CollectionChangeEventCreator {

    /**
     * Construct a new instance.
     * 
     * @param src the source.
     * @param propName the property name.
     * @param oldVal the old collection to be cloned.
     * @throws IllegalArgumentException if the constructor cannot be accessed.
     * @throws IllegalStateException if the cloning fails.
     */
    public ConstructorCreator(Object src, String propName, Collection oldVal) {
        super(src, propName, oldVal);
    }

    protected Collection clone(Collection col) {
        Constructor ctor;
        try {
            ctor = col.getClass().getConstructor(new Class[] { Collection.class });
        } catch (SecurityException e) {
            throw new IllegalArgumentException("oldVal has not accessible constructor");
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("oldVal has not a constructor with a single argument of type Collection");
        }
        try {
            return (Collection) ctor.newInstance(new Object[] { col });
        } catch (Exception e) {
            throw ExceptionUtils.createExn(IllegalStateException.class, "pb using " + ctor + " with " + col, e);
        }
    }
}
