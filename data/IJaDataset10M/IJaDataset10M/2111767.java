package tudresden.ocl20.pivot.interpreter.test.standardlibrary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.Arrays;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import tudresden.ocl20.pivot.interpreter.IInterpretationResult;
import tudresden.ocl20.pivot.model.ModelAccessException;
import tudresden.ocl20.pivot.parser.ParseException;

/**
 * <p>
 * Contains some test cases to test Standard Library operations on
 * <code>Boolean</code>s.
 * </p>
 * 
 * @author Claas Wilke
 */
public class TestBoolean extends AbstractInterpreterTest {

    /** The name of the constraint directory for this test suite. */
    private static final String CONSTRAINT_DIRECTORY = "standardlibrary/boolean";

    /**
	 * <p>
	 * Initializes the test cases.
	 * </p>
	 * 
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @BeforeClass
    public static void setUp() throws IllegalArgumentException, ModelAccessException {
        AbstractInterpreterTest.setUp();
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
        AbstractInterpreterTest.tearDown();
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.and(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testAnd01() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/and01", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsFalse(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.and(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testAnd02() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/and02", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsFalse(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.and(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testAnd03() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/and03", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsFalse(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.and(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testAnd04() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/and04", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsFalse(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.and(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testAnd05() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/and05", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsFalse(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.and(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testAnd06() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/and06", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsTrue(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.and(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testAnd07() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/and07", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsInvalid(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.and(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testAnd08() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/and08", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsInvalid(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.and(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testAnd09() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/and09", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsFalse(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.and(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testAnd10() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/and10", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsInvalid(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.and(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testAnd11() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/and11", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsInvalid(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.and(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testAnd12() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/and12", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsInvalid(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.and(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testAnd13() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/and13", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsFalse(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.and(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testAnd14() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/and14", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsInvalid(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.and(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testAnd15() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/and15", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsInvalid(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.and(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testAnd16() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/and16", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsInvalid(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.implies(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testImplies01() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/implies01", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsTrue(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.implies(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testImplies02() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/implies02", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsTrue(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.implies(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testImplies03() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/implies03", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsTrue(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.implies(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testImplies04() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/implies04", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsTrue(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.implies(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testImplies05() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/implies05", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsFalse(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.implies(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testImplies06() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/implies06", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsTrue(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.implies(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testImplies07() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/implies07", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsInvalid(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.implies(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testImplies08() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/implies08", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsInvalid(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.implies(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testImplies09() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/implies09", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsInvalid(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.implies(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testImplies10() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/implies10", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsTrue(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.implies(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testImplies11() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/implies11", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsInvalid(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.implies(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testImplies12() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/implies12", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsInvalid(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.implies(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testImplies13() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/implies13", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsInvalid(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.implies(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testImplies14() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/implies14", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsTrue(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.implies(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testImplies15() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/implies15", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsInvalid(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.implies(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testImplies16() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/implies16", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsInvalid(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.not()</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testNot01() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/not01", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsTrue(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.not()</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testNot02() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/not02", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsFalse(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.not()</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testNot03() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/not03", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsInvalid(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.not()</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testNot04() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/not04", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsInvalid(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.or(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testOr01() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/or01", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsFalse(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.or(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testOr02() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/or02", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsTrue(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.or(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testOr03() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/or03", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsInvalid(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.or(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testOr04() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/or04", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsInvalid(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.or(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testOr05() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/or05", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsTrue(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.or(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testOr06() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/or06", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsTrue(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.or(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testOr07() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/or07", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsTrue(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.or(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testOr08() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/or08", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsTrue(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.or(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testOr09() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/or09", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsInvalid(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.or(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testOr10() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/or10", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsTrue(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.or(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testOr11() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/or11", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsInvalid(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.or(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testOr12() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/or12", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsInvalid(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.or(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testOr13() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/or13", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsInvalid(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.or(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testOr14() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/or14", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsTrue(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.or(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testOr15() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/or15", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsInvalid(results.get(0));
    }

    /**
	 * <p>
	 * Tests the operation <code>Boolean.or(Boolean)</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testOr16() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/or16", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        this.assertIsInvalid(results.get(0));
    }
}
