package org.skycastle.util.spatialcontainer;

/**
 * A mutable {@link SpatialContainer}.  Allows adding and removing elements.
 *
 * @author Hans Haggstrom
 * @param <T> type of objects that are stored in the {@link MutableSpatialContainer}.
 */
public interface MutableSpatialContainer<T extends LocatedObject> extends SpatialContainer<T> {

    /**
     * Adds the specified element to this {@link MutableSpatialContainer}.
     *
     * @param element the element to add.  Should not be null or already added.
     */
    void addElement(T element);

    /**
     * Removes the specified element from this {@link MutableSpatialContainer}.
     *
     * @param element the element to remove.  Should not be null, and should be in the container.
     */
    void removeElement(T element);
}
