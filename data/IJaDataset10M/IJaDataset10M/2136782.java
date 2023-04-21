package eu.actorsproject.xlim.codegenerator;

import eu.actorsproject.util.IntrusiveList;

public class LocalScope {

    private IntrusiveList<TemporaryVariable> mTemporaries;

    public LocalScope() {
        mTemporaries = new IntrusiveList<TemporaryVariable>();
    }

    public void add(TemporaryVariable temp) {
        mTemporaries.addLast(temp);
    }

    public void remove(TemporaryVariable temp) {
        temp.out();
    }

    public Iterable<TemporaryVariable> getTemporaries() {
        return mTemporaries;
    }
}
