package com.lts.util;

public class MoreArrayUtils {

    /**
	 * Does the array contain a particular object?
	 * 
	 * <P>
	 * This method returns true if at least one element in the array is identity
	 * equivalent (==) to the provided object.  Note that null is considered a 
	 * valid value for the target (does the array contain null?)
	 * 
	 * <P>
	 * The method returns false if the input array is null or none of its elements
	 * are identity equivalent to the target.
	 * 
	 * @param o The element to try and find in the array.
	 * @param array The array to search.
	 * @return true if the array contains the target, false otherwise.
	 */
    public static boolean contains(Object o, Object[] array) {
        if (null == array) return false;
        for (int i = 0; i < array.length; i++) {
            if (o == array[i]) return true;
        }
        return false;
    }
}
