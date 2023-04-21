package dovetaildb.dbservice;

import dovetaildb.iter.Iter;

public abstract class WrappingIter extends AbstractIter {

    protected Iter subIter;

    protected WrappingIter(Iter subIter) {
        this.subIter = subIter;
    }

    public void close() {
    }
}
