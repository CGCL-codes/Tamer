package com.exjali;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>Allow simple iterations on a range of Integers using the foreach loop.</p>
 * 
 * <p>This construct in inspired by languages as <em>groovy</em> or <em>ruby</em>
 * that allow users to type code like : </p>
 * <p>'<code>for i in 1..10</code> do'<br /></p>
 * <p>With the <tt>Range</tt> class you can write (in java) : </p>
 * 
 * <p><code>
 * for (int i : range(1, 10)) {<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;System.out.println(i);<br />
 * }<br />
 * </code>
 * </p>
 * 
 * <p>Because the <code>range</code> method returns a list, you can also 
 * write : </p>
 * <code>
 * if (range(min, max - 1).contains(myIntToTest)) { 
 * </code>
 * 
 * @author Raphael Lemaire
 *
 */
public final class Range {

    private Range() {
    }

    /**
	 * Returns an unmodifiable list containing all integers of the given range
	 * in natural order.
	 * @param start First value of the range, included.
	 * @param stop Last value of the range, excluded. 
	 * @return Returns an unmodifiable list containing all integers of the given
	 * range in natural order.
	 */
    public static List<Integer> range(Integer start, Integer stop) {
        List<Integer> result = new ArrayList<Integer>();
        for (int i = start; i <= stop; i++) {
            result.add(i);
        }
        return Collections.unmodifiableList(result);
    }
}
