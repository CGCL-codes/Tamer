package net.sourceforge.signal.tools.core.context.loader;

import java.util.Vector;

public class VectorHolder {

    private Vector<?> vector;

    public VectorHolder(Vector<?> vector) {
        this.vector = vector;
    }

    public Vector<?> getVector() {
        return vector;
    }
}
