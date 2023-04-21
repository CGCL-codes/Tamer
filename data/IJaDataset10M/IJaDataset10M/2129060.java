package org.jmlspecs.samples.prelimdesign;

/** Supply test data for the JML and JUnit based testing of 
 *  IntMathOps4.
 *
 *  <p>Test data is supplied by overriding methods in this class.  See
 *  the JML documentation and the comments below about how to do this.
 *
 *  <p>This class is also the place to override the <kbd>setUp()</kbd>
 *  and <kbd>tearDown()</kbd> methods if your testing needs some
 *  actions to be taken before and after each test is executed.
 *
 *  <p>This class is never rewritten by jmlunit.
 *
 */
public abstract class IntMathOps4_JML_TestData extends junit.framework.TestCase {

    /** Initialize this class. */
    public IntMathOps4_JML_TestData(java.lang.String name) {
        super(name);
    }

    public junit.framework.TestSuite overallTestSuite() {
        return new junit.framework.TestSuite("Overall tests for IntMathOps4");
    }

    public junit.framework.TestSuite emptyTestSuiteFor(java.lang.String methodName) {
        return new junit.framework.TestSuite(methodName);
    }

    protected org.jmlspecs.jmlunit.strategies.IntIterator vintIter(java.lang.String methodName, int loopsThisSurrounds) {
        return vintStrategy.intIterator();
    }

    /** The strategy for generating test data of type
     * int. */
    private org.jmlspecs.jmlunit.strategies.IntStrategyType vintStrategy = new org.jmlspecs.jmlunit.strategies.IntBigStrategy() {

        protected int[] addData() {
            return new int[] {};
        }
    };
}
