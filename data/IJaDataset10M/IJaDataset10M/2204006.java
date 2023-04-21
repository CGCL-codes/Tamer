package tgreiner.amy.chess.book;

import java.util.Comparator;
import tgreiner.amy.chess.book.ComparableComparator;
import junit.framework.TestCase;

/**
 * A test case for ComparableComparator.
 *
 * @author <a href="mailto:thorsten.greiner@googlemail.com">Thorsten Greiner</a>
 */
public class ComparableComparatorTest extends TestCase {

    /**
     * Test the compare method.
     */
    public void testCompare() {
        Long one = new Long(1);
        Long five = new Long(2);
        Comparator c = new ComparableComparator();
        assertTrue(c.compare(one, five) < 0);
        assertTrue(c.compare(five, one) > 0);
        assertTrue(c.compare(one, one) == 0);
    }
}
