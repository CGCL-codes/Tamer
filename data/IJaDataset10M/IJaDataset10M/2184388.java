package edu.ucla.mbi.xml.MIF.elements.comparators;

import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: Apr 6, 2006
 * Time: 5:51:02 PM
 */
public class SimpleObjectComparator implements Comparator {

    public int compare(Object o1, Object o2) {
        return o1.toString().compareTo(o2.toString());
    }
}
