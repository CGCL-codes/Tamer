package Files;

import java.util.Vector;
import java.io.*;

public class ObjectQueue extends Vector {

    public ObjectQueue() {
        super();
    }

    public void put(Object o) {
        addElement(o);
    }

    public Object get() {
        if (isEmpty()) return null;
        Object o = firstElement();
        removeElement(o);
        return o;
    }

    public Object peek() {
        if (isEmpty()) return null;
        return firstElement();
    }

    public void save(String file) {
    }
}
