package org.nakedobjects.distribution;

import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.io.Memento;
import java.io.Serializable;

public class ObjectUpdateMessage implements Serializable {

    private static int nextId = 0;

    private static final long serialVersionUID = 1L;

    protected final int id;

    private Memento memento;

    public ObjectUpdateMessage(NakedObject object) {
        id = nextId++;
        memento = new Memento(object);
    }

    public String toString() {
        return "ObjectUpdateMessage#" + id + " [" + memento + "]";
    }
}
