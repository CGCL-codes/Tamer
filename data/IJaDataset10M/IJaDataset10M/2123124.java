package bova.desenv.lib.checkbox;

import java.util.Vector;

/**
*
* @author Roberto
*/
public class NamedVector extends Vector {

    String name;

    public NamedVector(String name) {
        this.name = name;
    }

    public NamedVector(String name, Object elements[]) {
        this.name = name;
        for (int i = 0, n = elements.length; i < n; i++) {
            add(elements[i]);
        }
    }

    @Override
    public String toString() {
        return "[" + name + "]";
    }
}
