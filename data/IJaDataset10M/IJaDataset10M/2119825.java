package rafa.math.filter;

import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author  rafa
 */
public class DeletionFilterTest {

    private DeletionFilter filter;

    private List<? extends Number> numbers;

    private FunctionMutationTargeter targeter;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        targeter = new FunctionMutationTargeter("x", 0);
        filter = new DeletionFilter(targeter);
        numbers = Arrays.asList(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 });
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testFilter() {
        targeter.setTargeterFunction("-abs((x-2)*(x-4)*(x-5))");
        List<? extends Number> filtered = filter.filter(numbers);
        assertEquals(7, filtered.size());
        assertEquals(Arrays.asList(0, 1, 3, 6, 7, 8, 9), filtered);
        targeter.setTargeterFunction("-abs((x-1)*(x-4))");
        filtered = filter.filter(numbers);
        assertEquals(8, filtered.size());
        assertEquals(Arrays.asList(0, 2, 3, 5, 6, 7, 8, 9), filtered);
    }
}
