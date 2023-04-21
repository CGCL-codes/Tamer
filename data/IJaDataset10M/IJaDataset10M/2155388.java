package org.yacmmf.core;

/**
 * An {@link Attribute} with more than one value that can be counted,
 * particularly {@link java.util.Collection}s but not {@link java.util.Map}s.
 * 
 * @see CountableAttribute
 */
public interface IterableAttribute extends Attribute {

    /**
	 * @return an iterable for all the values in this
	 *         {@link CollectionAttribute}.
	 */
    public abstract Iterable<Component> getIterable(Component container);

    /**
	 * Set an iterable for all the values in this {@link CollectionAttribute}.
	 */
    public abstract void setIterable(Component container, Iterable<Component> iterable);
}
