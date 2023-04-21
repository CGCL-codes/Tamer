package org.vizzini.ai.geneticalgorithm;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.vizzini.util.TestFinder;

/**
 * Provides unit tests for the <code>AbstractContext</code> class.
 *
 * <p>By default, all test methods (methods names beginning with <code>
 * test</code>) are run. Run individual tests from the command line using the
 * <code>main()</code> method. Specify individual test methods to run using the
 * <code>suite()</code> method.</p>
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @see      TestFinder
 * @since    v0.3
 */
public class AbstractContextTest extends TestCase {

    /** First fitness case. */
    private static final IFitnessCase FITNESS_CASE0 = createFitnessCase0();

    /** Second fitness case. */
    private static final IFitnessCase FITNESS_CASE1 = createFitnessCase1();

    /** First input name. */
    private static final String INPUT_NAME0 = "x0";

    /** First output name. */
    private static final String OUTPUT_NAME0 = "y0";

    /** First input value. */
    private static final Double INPUT_VALUE0 = new Double(123.0);

    /** First output value. */
    private static final Double OUTPUT_VALUE0 = new Double(456.0);

    /** Second input value. */
    private static final Double INPUT_VALUE1 = new Double(987.0);

    /** Second output value. */
    private static final Double OUTPUT_VALUE1 = new Double(654.0);

    /** Second state name. */
    private static final String STATE_NAME1 = "s1";

    /** Third state name. */
    private static final String STATE_NAME2 = "s2";

    /** Fourth state name. */
    private static final String STATE_NAME3 = "s3";

    /** Second state variable. */
    private static final Double STATE_VARIABLE1 = new Double(111.0);

    /** Third state variable. */
    private static final Double STATE_VARIABLE2 = new Double(222.0);

    /** Fourth state variable. */
    private static final Double STATE_VARIABLE3 = new Double(333.0);

    /** First context. */
    private IContext _context0;

    /**
     * Construct this object with the given parameter.
     *
     * @param  method  Method to run.
     *
     * @since  v0.3
     */
    public AbstractContextTest(String method) {
        super(method);
    }

    /**
     * Application method.
     *
     * @param  args  Application arguments.
     *
     * @since  v0.3
     */
    public static void main(String[] args) {
        TestFinder.getInstance().run(AbstractContextTest.class, args);
    }

    /**
     * @return  a suite of tests to run.
     *
     * @since   v0.3
     */
    public static TestSuite suite() {
        TestSuite suite = new TestSuite(AbstractContextTest.class);
        return suite;
    }

    /**
     * Test the <code>getCurrentFitnessCase()</code> method.
     *
     * @since  v0.3
     */
    public void testGetCurrentFitnessCase() {
        assertEquals(FITNESS_CASE0, _context0.getCurrentFitnessCase());
        _context0.incrementCurrentFitnessCase();
        assertEquals(FITNESS_CASE1, _context0.getCurrentFitnessCase());
        _context0.incrementCurrentFitnessCase();
        assertEquals(FITNESS_CASE0, _context0.getCurrentFitnessCase());
    }

    /**
     * Test the <code>getCurrentFitnessCaseIndex()</code> method.
     *
     * @since  v0.3
     */
    public void testGetCurrentFitnessCaseIndex() {
        assertEquals(0, _context0.getCurrentFitnessCaseIndex());
        _context0.incrementCurrentFitnessCase();
        assertEquals(1, _context0.getCurrentFitnessCaseIndex());
        _context0.incrementCurrentFitnessCase();
        assertEquals(0, _context0.getCurrentFitnessCaseIndex());
    }

    /**
     * Test the <code>getFitnessCaseCount()</code> method.
     *
     * @since  v0.3
     */
    public void testGetFitnessCaseCount() {
        assertEquals(2, _context0.getFitnessCaseCount());
    }

    /**
     * Test the <code>getStateVariable()</code> method.
     *
     * @since  v0.3
     */
    public void testGetStateVariable() {
        assertEquals(STATE_VARIABLE1, _context0.getStateVariable(STATE_NAME1));
        assertEquals(STATE_VARIABLE1, _context0.getStateVariable(STATE_NAME1));
        assertEquals(STATE_VARIABLE1, _context0.getStateVariable(STATE_NAME1));
        assertNull(_context0.getStateVariable("bogus"));
    }

    /**
     * @see  junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() {
        _context0 = new DefaultContext();
        _context0.add(FITNESS_CASE0);
        _context0.add(FITNESS_CASE1);
        _context0.setStateVariable(STATE_NAME1, STATE_VARIABLE1);
        _context0.setStateVariable(STATE_NAME2, STATE_VARIABLE2);
        _context0.setStateVariable(STATE_NAME3, STATE_VARIABLE3);
    }

    /**
     * @see  junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() {
        _context0 = null;
    }

    /**
     * @return  a fitness case.
     *
     * @since   v0.3
     */
    private static IFitnessCase createFitnessCase0() {
        IFitnessCase answer = new DefaultFitnessCase();
        answer.setInput(INPUT_NAME0, INPUT_VALUE0);
        answer.setOutput(OUTPUT_NAME0, OUTPUT_VALUE0);
        return answer;
    }

    /**
     * @return  a fitness case.
     *
     * @since   v0.3
     */
    private static IFitnessCase createFitnessCase1() {
        IFitnessCase answer = new DefaultFitnessCase();
        answer.setInput(INPUT_NAME0, INPUT_VALUE1);
        answer.setOutput(OUTPUT_NAME0, OUTPUT_VALUE1);
        return answer;
    }
}
