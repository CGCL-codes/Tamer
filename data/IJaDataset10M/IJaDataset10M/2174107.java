package test.net.sf.molae.pipe.genimpl;

import java.util.Map;
import java.util.Vector;
import junit.framework.TestCase;
import net.sf.molae.pipe.genimpl.ListsBackedMap;
import net.sf.molae.pipe.test.MapTest;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit TestCase.
 * @testedclass ListsBackedMap
 * @since 1.1
 * @version 2.0
 * @author Ralph Wagner
 */
public class TestListsBackedMap extends TestCase {

    /**
     * Constructs a test case with the given name.
     * @param name name of the test case
     */
    public TestListsBackedMap(String name) {
        super(name);
    }

    Map<Integer, Integer> lbm;

    @Before
    protected void setUp() {
        lbm = new ListsBackedMap<Integer, Integer>(new Vector<Integer>());
    }

    /**
     * Method to test <code>ListsBackedMap</code>.
     * @throws AssertionFailedError if the test failed.
     */
    @Test
    public void testMap() {
        MapTest<Map<Integer, Integer>, Integer, Integer> test = MapTest.getInstance(lbm);
        test.setIntegerTestValues(test);
        test.performValuesTest();
    }
}
