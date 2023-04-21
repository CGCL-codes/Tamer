package kahweh.algorithm.sort;

import static org.junit.Assert.*;
import org.junit.Test;

public class QuickSortTest extends SortTest {

    @Test
    public void testSort() {
        assertTrue(checkOrder(new QuickSort().sort(original)));
    }
}
