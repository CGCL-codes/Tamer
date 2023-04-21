package pocu.data_structs;

import java.util.Iterator;

/**
 * Contract for classes that can be iterated upon.
 */
public interface Iterable {

    Iter iter();

    Iterator iterator();

    int size();
}
