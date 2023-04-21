package name.huzhenbo.java.collection;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SortedListImpTest {

    private SortedListImp list;

    @Before
    public void setUp() {
        list = new SortedListImp();
        list.insert(8);
        list.insert(8);
        list.insert(5);
        list.insert(9);
    }

    @Test
    public void test_list() {
        assertEquals(5, list.get(0));
        assertEquals(8, list.get(1));
        assertEquals(8, list.get(2));
        assertEquals(9, list.get(3));
    }

    @Test
    public void test_iterator() {
        SortedListIterator iterator = list.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
