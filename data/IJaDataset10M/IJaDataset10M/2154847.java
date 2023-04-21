package net.rootnode.loomchild.util.testing;

import static junit.framework.Assert.assertEquals;
import java.util.Iterator;
import java.util.List;
import junit.framework.AssertionFailedError;

public class Util {

    /**
	 * Asserts that list has given contents. Otherwise throws exception. To be
	 * used in testing.
	 * 
	 * @param <T>
	 *            List type
	 * @param expectedArray
	 *            Expected list contents.
	 * @param actualList
	 *            List to be checked.
	 * @throws AssertionFailedError
	 *             Thrown when list are not equal.
	 */
    public static <T> void assertListEquals(String message, T[] expectedArray, List<T> actualList) {
        assertEquals(message, expectedArray.length, actualList.size());
        Iterator<T> actualIterator = actualList.iterator();
        for (T expected : expectedArray) {
            T actual = actualIterator.next();
            assertEquals(message, expected, actual);
        }
    }

    public static <T> void assertListEquals(T[] expectedArray, List<T> actualList) {
        assertListEquals("", expectedArray, actualList);
    }
}
