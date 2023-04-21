package tudresden.ocl20.pivot.examples.pain.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.Arrays;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import tudresden.ocl20.pivot.interpreter.IInterpretationResult;
import tudresden.ocl20.pivot.model.ModelAccessException;

/**
 * <p>
 * Contains some test cases to test constraints defined on the context
 * <code>ServiceLevel3Choice</code>.
 * </p>
 * 
 * @author Claas Wilke
 */
public class TestServiceLevel3Choice extends AbstractPainTest {

    /** The name of the constraint directory for this test suite. */
    private static final String CONSTRAINT_DIRECTORY = "serviceLevel3Choice";

    /**
	 * <p>
	 * Initializes the test cases.
	 * </p>
	 * 
	 * @throws Exception
	 */
    @BeforeClass
    public static void setUp() throws Exception {
        AbstractPainTest.setUp();
    }

    /**
	 * <p>
	 * Tears down the test cases.
	 * </p>
	 * 
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @AfterClass
    public static void tearDown() throws IllegalArgumentException, ModelAccessException {
        AbstractPainTest.tearDown();
    }

    /**
	 * <p>
	 * Tests the invariant <code>epcSvcLvlCd</code>.
	 * </p>
	 * 
	 * @throws Throwable
	 */
    @Test
    public void testEpcSvcLvlCd01() throws Throwable {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(CONSTRAINT_DIRECTORY + "/epcSvcLvlCd", MODELINSTANCE_NAME_01, Arrays.asList(new String[] { "ServiceLevel3Choice" }));
        assertNotNull(results);
        assertEquals(2, results.size());
        this.assertIsFalse(results.get(0));
        this.assertIsFalse(results.get(1));
    }

    /**
	 * <p>
	 * Tests the invariant <code>epcSvcLvlCd</code>.
	 * </p>
	 * 
	 * @throws Throwable
	 */
    @Test
    public void testEpcSvcLvlCd02() throws Throwable {
    }

    /**
	 * <p>
	 * Tests the invariant <code>epcSvcLvlCd</code>.
	 * </p>
	 * 
	 * @throws Throwable
	 */
    @Test
    public void testEpcSvcLvlCd03() throws Throwable {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(CONSTRAINT_DIRECTORY + "/epcSvcLvlCd", MODELINSTANCE_NAME_03, Arrays.asList(new String[] { "ServiceLevel3Choice" }));
        assertNotNull(results);
        assertEquals(2, results.size());
        this.assertIsFalse(results.get(0));
        this.assertIsFalse(results.get(1));
    }
}
