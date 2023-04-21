package fitlibrary.specify.setParser;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import fitlibrary.specify.eg.Count;

@SuppressWarnings("rawtypes")
public class Match {

    @SuppressWarnings("unchecked")
    public Set getSetOfStringAbc() {
        return new HashSet(Arrays.asList(new String[] { "a", "b", "c" }));
    }

    public Set getSetOf123() {
        return set(new Integer[] { Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3) });
    }

    @SuppressWarnings("unchecked")
    private HashSet set(Object[] array) {
        return new HashSet(Arrays.asList(array));
    }

    public Set getSetOfCount123() {
        return set(new Count[] { new Count(1), new Count(2), new Count(3) });
    }

    public Set getSetEmpty() {
        return new HashSet();
    }

    public Set getSetOfCounts23() {
        return set(new Count[] { new Count(2), new Count(3) });
    }
}
