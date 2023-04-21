package com.ibm.tuningfork.infra.data;

import java.util.ArrayList;
import java.util.Collection;

/**
 *  A general representation of a relation that is not in a stream (see also ITimedRelation and TimedRelation, which pair a relation with
 *   a timestamp to create a specialized stream of exactly one column which is a relation).
 */
@SuppressWarnings("serial")
public class Relation extends ArrayList<ITuple> implements IRelation {

    private boolean sealed = false;

    public Relation() {
        super();
    }

    public Relation(int size) {
        super(size);
    }

    public Relation(ITuple[] in) {
        super(in.length);
        for (int i = 0; i < in.length; i++) super.add(i, in[i]);
    }

    public boolean isSealed() {
        return sealed;
    }

    public Relation seal() {
        sealed = true;
        return this;
    }

    protected final void checkSeal() {
        if (sealed) {
            throw new SealingException(this);
        }
    }

    public ITuple[] toArray() {
        return (ITuple[]) super.toArray();
    }

    public boolean add(ITuple arg0) {
        checkSeal();
        return super.add(arg0);
    }

    public boolean addAll(Collection<? extends ITuple> arg0) {
        checkSeal();
        return super.addAll(arg0);
    }

    public void clear() {
        checkSeal();
        super.clear();
    }

    public boolean remove(Object arg0) {
        checkSeal();
        return super.remove(arg0);
    }

    public boolean removeAll(Collection<?> arg0) {
        checkSeal();
        return super.removeAll(arg0);
    }

    public boolean retainAll(Collection<?> arg0) {
        checkSeal();
        return super.retainAll(arg0);
    }
}
