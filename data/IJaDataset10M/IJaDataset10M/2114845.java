package ao.util.data.primitive;

import ao.util.math.rand.Rand;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Arrays;
import java.util.List;
import static org.testng.Assert.*;

/**
 * User: aostrovsky
 * Date: 19-Feb-2010
 * Time: 3:01:39 PM
 */
public class PrimitiveListTest {

    @Test
    public void shortListTest() {
        List<Short> list = new ShortList();
        int nValues = 5 + Rand.nextInt(20);
        for (int i = 0; i < nValues; i++) {
            list.add((short) i);
        }
        assertEquals(list.size(), nValues, "Values added");
        for (int i = 0; i < nValues; i++) {
            assertEquals(i, (short) list.get(i), "Value in order");
        }
        int nextIndex = 0;
        for (Short s : list) {
            assertEquals(s, list.get(nextIndex++), "Iterator works");
        }
        list.clear();
        assertTrue(list.isEmpty(), "Values cleared");
        List<Short> values = Arrays.asList((short) 1, (short) 2, (short) 3);
        list.addAll(values);
        assertTrue(list.containsAll(values), "addAll works");
    }
}
