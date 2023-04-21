package br.org.databasetools.core.view.table;

import java.util.Comparator;
import java.util.Vector;
import br.org.databasetools.core.util.BeanProvider;

@SuppressWarnings("unchecked")
class SortColumnComparator implements Comparator {

    protected int index;

    protected String column;

    protected boolean ascending;

    public SortColumnComparator(int index, String column, boolean ascending) {
        this.index = index;
        this.column = column;
        this.ascending = ascending;
    }

    public int compare(Object one, Object two) {
        if (one instanceof Vector && two instanceof Vector) {
            Vector vOne = (Vector) one;
            Vector vTwo = (Vector) two;
            Object oOne = vOne.elementAt(index);
            Object oTwo = vTwo.elementAt(index);
            if (oOne instanceof Comparable && oTwo instanceof Comparable) {
                Comparable cOne = (Comparable) oOne;
                Comparable cTwo = (Comparable) oTwo;
                if (ascending) {
                    return cOne.compareTo(cTwo);
                } else {
                    return cTwo.compareTo(cOne);
                }
            }
        } else if (column != null) {
            Object oOne = BeanProvider.getProperty(one, column);
            Object oTwo = BeanProvider.getProperty(two, column);
            if (oOne instanceof Comparable && oTwo instanceof Comparable) {
                Comparable cOne = (Comparable) oOne;
                Comparable cTwo = (Comparable) oTwo;
                if (ascending) {
                    return cOne.compareTo(cTwo);
                } else {
                    return cTwo.compareTo(cOne);
                }
            }
        }
        return 1;
    }
}
