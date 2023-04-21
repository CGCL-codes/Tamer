package andreafrancia.util.remote;

import java.util.List;

/**
 * A {@link RemoteList} that act as a proxy for a local {@link List}.
 * All method invocations are delegated to the {@code target} List.
 * @param <T> 
 * @author Andrea Francia
 */
public class ListAsRemoteList<T> implements RemoteList<T> {

    private final List<T> adaptee;

    /**
     * Create a new instance.
     * @param adaptee the target {@link List}
     * @throws IllegalArgumentException if {@code target} is {@code null}
     */
    public ListAsRemoteList(List<T> adaptee) {
        if (adaptee == null) {
            throw new IllegalArgumentException("Paremeter 'target' can not be null.");
        }
        this.adaptee = adaptee;
    }

    /**
     * Return the size() of the list, invoke {@code size()} on the target.
     * @return the size of the {@code target} list.
     */
    public int size() {
        return adaptee.size();
    }

    /**
     * Returns the element at the specified position in this list.
     * Invoke the {@code get()} on the {@code target} list.
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the {@code target.get()} does.
     */
    public T get(int index) {
        return adaptee.get(index);
    }
}
