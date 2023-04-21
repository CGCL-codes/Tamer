package ie.ucd.lis.ojax;

import java.util.Comparator;

public class CompositeComparator implements Comparator {

    private Comparator primary;

    private Comparator secondary;

    public CompositeComparator(Comparator primary, Comparator secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    public int compare(Object obj1, Object obj2) {
        int result = primary.compare(obj1, obj2);
        if (result != 0) {
            return result;
        }
        return secondary.compare(obj1, obj2);
    }
}
