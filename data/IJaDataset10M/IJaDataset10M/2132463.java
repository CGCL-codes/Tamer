package com.blommesteijn.uva.sc.saf.utils;

import java.util.List;

public class ListUtil {

    /**
	 * Calculate distance between smallest and largest entry
	 * @param asList sorted list of integers
	 * @return distance
	 */
    public static int distance(List<Integer> asList) {
        if (asList.isEmpty()) return Integer.MIN_VALUE;
        return (asList.get(asList.size() - 1) - asList.get(0));
    }
}
