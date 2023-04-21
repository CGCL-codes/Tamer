package at.wwu.tunes2db.utils;

import java.util.Comparator;

public class PercentageComparator implements Comparator<Double> {

    public int compare(Double d1, Double d2) {
        return d2.compareTo(d1);
    }
}
