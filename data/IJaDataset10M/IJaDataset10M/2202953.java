package net.sf.asyncobjects.tests;

import junit.framework.TestSuite;
import net.sf.asyncobjects.AResolver;
import net.sf.asyncobjects.Promise;
import net.sf.asyncobjects.junit.AsyncTestCase;
import net.sf.asyncobjects.util.ASemaphore;
import net.sf.asyncobjects.util.Semaphore;
import net.sf.asyncobjects.util.callbacks.CallableAdapter;
import net.sf.asyncobjects.util.callbacks.ListenerAdapter;
import net.sf.asyncobjects.util.callbacks.MapperAdapter;
import net.sf.asyncobjects.util.callbacks.RunnableAdapter;

/**
 * An utilties test
 * @author const
 *
 */
public class UtilTest extends AsyncTestCase {

    /**
	 * Test constructor
	 * 
	 * @param name
	 *            a name of the test
	 */
    public UtilTest(String name) {
        super(name);
    }

    /**
	 * Test runnable adapter
	 * @return if test is successful
	 */
    public Promise<?> testRunnable() {
        final ASemaphore s = new Semaphore(0).export();
        new RunnableAdapter() {

            protected void run() throws Throwable {
                s.release();
            }
        }.export().run();
        return s.acquire();
    }

    /**
	 * Test runnable adapter
	 * @return if test is successful
	 */
    public Promise<?> testCallable() {
        return expectEquals(42, new CallableAdapter<Integer>() {

            protected Promise<Integer> call() throws Throwable {
                return Promise.with(42);
            }
        }.export().call());
    }

    /**
	 * Test mapper adapter
	 * @return if test is successful
	 */
    public Promise<?> testMapper() {
        return expectEquals(42, new MapperAdapter<Integer, Integer>() {

            protected Promise<Integer> map(Integer value) throws Throwable {
                return Promise.with(30 + value);
            }
        }.export().map(12));
    }

    /**
	 * Test listner adapter
	 * @return if test is successful
	 */
    public Promise<?> testListener() {
        Promise<Integer> rc = new Promise<Integer>();
        final AResolver<Integer> resolver = rc.resolver();
        new ListenerAdapter<Integer>() {

            protected void onEvent(Integer event) throws Throwable {
                resolver.resolve(event);
            }
        }.export().onEvent(42);
        return expectEquals(42, rc);
    }

    /**
	 * This method creates test suite since default test creation does not works
	 * 
	 * @return test suite
	 */
    public static TestSuite suite() {
        return collectTests(UtilTest.class);
    }
}
