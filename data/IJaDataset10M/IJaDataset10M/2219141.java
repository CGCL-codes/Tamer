package pl.edu.mimuw.loxim.data;

import java.util.ArrayList;
import java.util.Collection;

public abstract class GenericCollection implements Struct, Sequence, Bag {

    protected Collection<Object> data = new ArrayList<Object>();

    protected Class<?> globalType;

    protected GenericCollection() {
    }

    @Override
    public Collection<Object> getData() {
        return data;
    }

    @Override
    public Class<?> getGlobalType() {
        return globalType;
    }

    @Override
    public void setGlobalType(Class<?> globalType) {
        this.globalType = globalType;
    }

    @Override
    public abstract String toString();
}
