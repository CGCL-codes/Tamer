package info.absu.snow.reflection;

/**
 * An interface for an object which can create an instance of a specific class.
 * The class is determined at the time when this object is created.
 * @author Denys Rtveliashvili
 *
 */
public interface Instantiator<T> {

    T getInstance() throws CannotInstantiateException;
}
