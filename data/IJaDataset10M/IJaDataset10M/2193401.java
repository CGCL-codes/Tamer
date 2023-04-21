package jmud.jgp;

import jgp.predicate.UnaryPredicate;
import jmud.UniqueId;

public class IdRangeVerifier implements UnaryPredicate {

    private int low;

    private int high;

    public IdRangeVerifier(int min, int max) {
        set(min, max);
    }

    public void setMin(int min) {
        low = min;
    }

    public void setMax(int max) {
        high = max;
    }

    public void set(int min, int max) {
        setMin(min);
        setMax(max);
    }

    public boolean execute(Object obj) {
        int id = ((UniqueId) obj).getId();
        return id >= low && id <= high;
    }
}
