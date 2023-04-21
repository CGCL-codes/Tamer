package comparators;

import java.util.Comparator;

@SuppressWarnings("rawtypes")
public class DMLTableNameComparator implements Comparator {

    public final int compare(Object a, Object b) {
        String subA = (String) a;
        String subB = (String) b;
        return (subA.compareToIgnoreCase(subB));
    }
}
