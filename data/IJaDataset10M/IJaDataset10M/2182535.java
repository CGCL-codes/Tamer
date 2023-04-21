package main;

/**
 * This interface is implemented by classes that respect Java Beans
 * and that wraps an object that does not respect it. It allows to convert
 * the wrapped object into a bean and reciprocally.
 * @author Julien Gouesse
 *
 */
public interface XMLTransportableWrapper<T> {

    public T getWrappedObject();

    public void wrap(T t);
}
