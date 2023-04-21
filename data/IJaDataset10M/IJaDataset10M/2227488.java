package trans.cel;

import java.util.*;

/**For sorting MultiSetQuantile objects by value or chromosome number and position*/
public class ComparatorMultiSetQuantile implements Comparator {

    private boolean sortByValue = true;

    /**Set boolean sortByValue = true to sort by value, false to sort by chromosome number and position.*/
    public ComparatorMultiSetQuantile(boolean sortByValue) {
        this.sortByValue = sortByValue;
    }

    /**Sorts by value (default) or chipNumber and position based on sortByValue boolean, all smallest to biggest.*/
    public int compare(Object arg0, Object arg1) {
        MultiSetQuantile first = (MultiSetQuantile) arg0;
        MultiSetQuantile second = (MultiSetQuantile) arg1;
        if (sortByValue) {
            if (first.value > second.value) return 1;
            if (first.value < second.value) return -1;
        } else {
            if (first.chromosomeNumber > second.chromosomeNumber) return 1;
            if (first.chromosomeNumber < second.chromosomeNumber) return -1;
            if (first.position > second.position) return 1;
            if (first.position < second.position) return -1;
        }
        return 0;
    }

    public void setSortByValue(boolean sortByValue) {
        this.sortByValue = sortByValue;
    }
}
