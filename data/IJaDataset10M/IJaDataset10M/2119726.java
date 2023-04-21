package uk.ac.lkl.common.util.collections;

/**
 * A wrapper of an object so that .equals is based on '=='.
 * 
 * This is useful when working with HashMaps for example since such classes use
 * .equals() and .hashCode() to determine whether the same element is being
 * referred to. In cases where what is really wanted is for identical (under
 * .equals) objects to be stored separately in the HashMap, this class can be
 * used.
 * 
 * @author $Author: darren.pearce $
 * @version $Revision: 6154 $
 * @version $Date: 2010-06-27 16:40:08 -0400 (Sun, 27 Jun 2010) $
 * 
 */
public class GenericObjectWrapper<O> {

    private O object;

    public GenericObjectWrapper(O object) {
        this.object = object;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ObjectWrapper) {
            GenericObjectWrapper<?> other = (GenericObjectWrapper<?>) object;
            return this.object == other.object;
        } else return this.object == object;
    }

    public O getObject() {
        return object;
    }

    @Override
    public int hashCode() {
        return object.hashCode();
    }

    public String toString() {
        return object.toString();
    }
}
