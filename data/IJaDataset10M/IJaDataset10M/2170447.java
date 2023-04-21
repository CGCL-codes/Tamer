package jmetric.util;

import java.util.Vector;

/**
 * This class extends the functionality of Vector so that everything is ordered through the use of the sortable
 * interface rather than by the order they were added.
 * 
 * To use this class call addSortedElement rather than addElement. This must be the case as addElement is Final Vector
 * :-(. Any element added to this vector must implement sortable to be sorted.
 * 
 * @Author Andrew AJ Cain
 */
public class SortedVector extends Vector {

    /**
	 * Place the element at the position based in the sorted array.
	 * 
	 */
    public void addSortedElement(Sortable sort) {
        if (size() == 0) {
            addElement(sort);
            return;
        }
        int posLow = 0, posHigh = size() - 1;
        int posMid = posHigh / 2;
        if (sort.lessThan(elementAt(posLow)) || sort.equals(elementAt(posLow))) {
            insertElementAt(sort, 0);
            return;
        }
        if (sort.greaterThan(elementAt(posHigh)) || sort.equals(elementAt(posHigh))) {
            addElement(sort);
            return;
        }
        while (posLow != posHigh) {
            if (sort.greaterThan(elementAt(posMid))) {
                if (posLow == posHigh - 1) {
                    posLow = posHigh;
                } else posLow = posMid;
            } else {
                if (posHigh == posLow + 1) posHigh = posLow; else posHigh = posMid;
            }
            posMid = posLow + ((posHigh - posLow) / 2);
        }
        if (posMid == size()) addElement(sort); else insertElementAt(sort, posMid);
    }
}
