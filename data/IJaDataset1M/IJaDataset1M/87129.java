package de.grogra.xl.impl.base;

import de.grogra.xl.util.EHashMap.Entry;

public final class EdgeData extends Entry {

    public Object source;

    public Object target;

    int add;

    int undirectedAdd;

    int delete;

    int bits;

    public void set(Object source, Object target) {
        this.source = source;
        this.target = target;
        hashCode = source.hashCode() * 31 + target.hashCode();
    }

    @Override
    protected void clear() {
        source = null;
        target = null;
    }

    @Override
    protected void copyValue(Entry e) {
        EdgeData n = (EdgeData) e;
        if (((add & RuntimeModel.SPECIAL_MASK) != 0) && ((n.add & RuntimeModel.SPECIAL_MASK) != 0) && (((add ^ n.add) & RuntimeModel.SPECIAL_MASK) != 0)) {
            throw new RuntimeException();
        }
        add |= n.add;
        if (((undirectedAdd & RuntimeModel.SPECIAL_MASK) != 0) && ((n.undirectedAdd & RuntimeModel.SPECIAL_MASK) != 0) && (((undirectedAdd ^ n.undirectedAdd) & RuntimeModel.SPECIAL_MASK) != 0)) {
            throw new RuntimeException();
        }
        undirectedAdd |= n.undirectedAdd;
    }

    @Override
    protected boolean keyEquals(Entry e) {
        EdgeData n = (EdgeData) e;
        return (source == n.source) && (target == n.target);
    }

    @Override
    public String toString() {
        return source + " -" + bits + "-> " + target + " +" + add + " -" + delete;
    }
}
