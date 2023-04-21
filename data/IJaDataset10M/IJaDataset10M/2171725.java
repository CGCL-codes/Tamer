package de.cs12.reliability.common;

/**
 * The {@code Occurrence} represents an occurrence or event, e.g., the failure
 * or repair of an object at a given time.
 * 
 * @author glass
 * 
 * @param <T>
 *            the object that is effected by the occurrence
 */
public abstract class Occurrence<T> implements Comparable<Occurrence<T>> {

    protected final T t;

    protected final Double time;

    /**
	 * Constructs an {@code Occurrence} with a given object and the time.
	 * 
	 * @param t
	 *            the object
	 * @param time
	 *            the time
	 */
    public Occurrence(T t, double time) {
        this.t = t;
        this.time = time;
    }

    /**
	 * Returns the object.
	 * 
	 * @return the object
	 */
    public T getObject() {
        return t;
    }

    /**
	 * Returns the time.
	 * 
	 * @return the time
	 */
    public double getTime() {
        return time;
    }

    public int compareTo(Occurrence<T> other) {
        return time.compareTo(other.time);
    }
}
