package com.nepxion.swing.list;

import java.awt.Point;
import javax.swing.JList;

public class ListManager {

    /**
	 * Sets the preference style for the list.
	 * @param list the instance of JList
	 */
    public static void setPreferenceStyle(JList list) {
    }

    /**
	 * Gets the index by a point.
	 * @param list the instance of JList
	 * @param point the instance of Point
	 * @return the index value
	 */
    public static int getIndex(JList list, Point point) {
        int index = list.getUI().locationToIndex(list, point);
        return index;
    }
}
