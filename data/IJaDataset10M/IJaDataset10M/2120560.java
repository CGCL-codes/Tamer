package org.nakedobjects.reflector.java.reflect;

import java.util.Vector;

public class JavaObjectWithVector {

    JavaReferencedObject added;

    JavaReferencedObject removed;

    Vector collection = new Vector();

    public Vector getMethod() {
        return collection;
    }

    public void addToMethod(JavaReferencedObject person) {
        added = person;
    }

    public void removeFromMethod(JavaReferencedObject person) {
        removed = person;
    }
}
