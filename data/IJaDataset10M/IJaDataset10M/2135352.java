package ar.com.datos.compresion.api.contexts.types;

import java.util.Iterator;

public abstract class AbstractContext implements Iterator<ContextNode> {

    public abstract int getGap();

    public abstract void restart();

    public abstract long getTotalLeido();
}
