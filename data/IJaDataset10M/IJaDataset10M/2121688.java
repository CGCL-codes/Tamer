package org.ode4j.ode.internal;

import java.util.ArrayList;

/**
 * this comes from the `reuse' library. copy any changes back to the source.
 *
 * Variable sized array template. The array is always stored in a contiguous
 * chunk. The array can be resized. A size increase will cause more memory
 * to be allocated, and may result in relocation of the array memory.
 * A size decrease has no effect on the memory allocation.
 *
 * Array elements with constructors or destructors are not supported!
 * But if you must have such elements, here's what to know/do:
 *   - Bitwise copy is used when copying whole arrays.
 *   - When copying individual items (via push(), insert() etc) the `='
 *     (equals) operator is used. Thus you should define this operator to do
 *     a bitwise copy. You should probably also define the copy constructor.
 *     
 *     
 * (TZ) @deprecated Remove if possible
 */
class DArray<T> {

    private ArrayList<T> _data = new ArrayList<T>();

    public void setSize(int i) {
        if (i == 0) {
            _data.clear();
            return;
        }
        if (i > size()) {
            return;
        }
        while (size() > i) {
            remove(size() - 1);
        }
    }

    public T get(int i) {
        return _data.get(i);
    }

    public void push(T item) {
        _data.add(item);
    }

    public void remove(int i) {
        _data.remove(i);
    }

    public int size() {
        return _data.size();
    }

    public void set(int index, T element) {
        if (index == size()) {
            _data.add(element);
            return;
        }
        _data.set(index, element);
    }
}
