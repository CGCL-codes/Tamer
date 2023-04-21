package name.huzhenbo.java.algorithm.talent;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumberOfInversionsTest {

    @Test
    public void test() {
        assertEquals(1, new NumberOfInversions(new int[] { 2, 1 }).numberOfInversions());
        assertEquals(3, new NumberOfInversions(new int[] { 3, 2, 1 }).numberOfInversions());
        assertEquals(6, new NumberOfInversions(new int[] { 4, 3, 2, 1 }).numberOfInversions());
        assertEquals(10, new NumberOfInversions(new int[] { 5, 4, 3, 2, 1 }).numberOfInversions());
        assertEquals(0, new NumberOfInversions(new int[] { 1, 2 }).numberOfInversions());
        assertEquals(2, new NumberOfInversions(new int[] { 2, 3, 1 }).numberOfInversions());
        assertEquals(4, new NumberOfInversions(new int[] { 2, 4, 3, 1 }).numberOfInversions());
    }
}
