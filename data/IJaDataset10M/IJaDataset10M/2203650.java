package net.sf.jawp.util.test;

/**
 * Each test case used by {@link net.sf.jawp.util.test.PerformanceTestRunner}
 * class is supposed to implement this interface.
 * 
 * @author maciek
 * @version $Revision: 1.1 $
 */
public interface TestCase {

    /**
	 * This method will be called before test case is executed. This method will
	 * be called only once and then all test method will be executed
	 * sequentialy.
	 */
    void init();

    /**
	 * This method will be called at the end of test case execution. This method
	 * will be called only once per test case, after all test case methods are
	 * executed.
	 */
    void destroy();

    /**
	 * This method is called before each test method. If this method throws an
	 * exception, test method won't be executed at all as well as
	 * {@link #after()} method.
	 */
    void before();

    /**
	 * This method is called after each test method.
	 * 
	 */
    void after();
}
