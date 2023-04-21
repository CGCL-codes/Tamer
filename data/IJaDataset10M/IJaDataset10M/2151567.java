package hu.akarnokd.reactiv4java;

/**
 * A simple action-like interface with one parameter.
 * @author akarnokd
 * @param <T> the action parameter
 */
public interface Action1<T> {

    /**
	 * The action body.
	 * @param value the value
	 */
    void invoke(T value);
}
