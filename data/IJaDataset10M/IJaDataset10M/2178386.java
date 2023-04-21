package org.jmlspecs.samples.misc;

/** Supply test data for the JML and JUnit based testing of 
 *  Counter.
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
public abstract class Counter_JML_TestData extends junit.framework.TestCase {

    /** Initialize this class. */
    public Counter_JML_TestData(java.lang.String name) {
        super(name);
    }

    public junit.framework.TestSuite overallTestSuite() {
        return new junit.framework.TestSuite("Overall tests for Counter");
    }

    public junit.framework.TestSuite emptyTestSuiteFor(java.lang.String methodName) {
        return new junit.framework.TestSuite(methodName);
    }

    protected org.jmlspecs.jmlunit.strategies.IndefiniteIterator vorg_jmlspecs_samples_misc_CounterIter(java.lang.String methodName, int loopsThisSurrounds) {
        return vorg_jmlspecs_samples_misc_CounterStrategy.iterator();
    }

    /** The strategy for generating test data of type
     * org.jmlspecs.samples.misc.Counter. */
    private org.jmlspecs.jmlunit.strategies.StrategyType vorg_jmlspecs_samples_misc_CounterStrategy = new org.jmlspecs.jmlunit.strategies.NewObjectAbstractStrategy() {

        protected Object make(int n) {
            switch(n) {
                case 0:
                    return null;
                case 1:
                    return new Meter();
                default:
                    break;
            }
            throw new java.util.NoSuchElementException();
        }
    };
}
