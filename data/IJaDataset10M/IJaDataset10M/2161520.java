package test.net.sf.molae.pipe.genimpl;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.List;
import net.sf.molae.pipe.genimpl.IndexedList;
import net.sf.molae.pipe.test.ListTest;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit TestCase.
 * @testedclass IndexedList
 * @since 1.1
 * @version 2.0
 * @author Ralph Wagner
 */
public class TestIndexedList extends TestCase {

    /**
     * Constructs a test case with the given name.
     * @param name name of the test case
     */
    public TestIndexedList(String name) {
        super(name);
    }

    private static final String VALUE1 = "Value1";

    private static final String VALUE2 = "Value2";

    private static final String VALUE3 = "Value3";

    List<String> base;

    IndexedList<String> l;

    @Before
    protected void setUp() {
        base = new ArrayList<String>();
        base.add(VALUE1);
        base.add(VALUE2);
        base.add(VALUE3);
        base.add(VALUE2);
        l = new IndexedList<String>(base);
    }

    /**
     * Main test method.
     * @throws AssertionFailedError if the test failed.
     */
    @Test
    public void testList() {
        l.set(2, VALUE1);
        assertEquals(base, l);
        ListTest test = ListTest.getInstance(l);
        test.performCycle();
    }
}
