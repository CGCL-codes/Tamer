package name.huzhenbo.java.patterns.strategy;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class StringComparatorTest {

    @Test
    public void test1() {
        StringComparator comparator = StringComparator.getInstance();
        assertEquals(1, comparator.compare("abcd", "bcd", StringLengthComparator.getInstance()));
        assertEquals(-1, comparator.compare("abcd", "bcd", LexicographicallyComparator.getInstance()));
    }
}
