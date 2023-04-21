package junit.extensions.jfunc;

import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestListener;
import junit.framework.AssertionFailedError;
import java.util.Vector;
import java.util.Enumeration;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationHandler;

/**
 * JFuncResult main function is to pass verbose assertions to the
 * <code>AssertListener</code>s.  
 *
 * <p>Another more experimental feature is it allows tests to run
 * without a suite through a proxy mechanism.  Since these tests are
 * running "live", the result of the test can be passed back
 * immediately, which is useful in cases where you might run test B
 * only if test A passes, or other more complicated situations that
 * come up during functional testing.
 * 
 * @see AssertListener
 * @author Shane Celis <shane@terraspring.com>
 **/
public class JFuncResult extends TestResult implements AssertListener {

    public static final int UNDEF = -1;

    public static final int PASSED = 0;

    public static final int FAILED = 1;

    public static final int ERRORED = 2;

    public JFuncResult() {
        super();
    }

    public synchronized void addAssert(Test test, String message, boolean condition) {
        for (Enumeration e = cloneListeners().elements(); e.hasMoreElements(); ) {
            Object listen = e.nextElement();
            if (listen instanceof AssertListener) ((AssertListener) listen).addAssert(test, message, condition);
        }
    }

    private synchronized Vector cloneListeners() {
        return (Vector) fListeners.clone();
    }

    /**
     * Returns a proxy of the test that will actually be run when
     * called.
     **/
    public Test getTestProxy(Test test) {
        InvocationHandler handler = new RunningTestProxy(this, test);
        Class cl = test.getClass();
        if (test instanceof JFuncAssert) {
            ((JFuncAssert) test).setResult(this);
        }
        try {
            Class proxy = TestletWrapper.getProxy(new Class[0], cl);
            Constructor cons = proxy.getConstructor(new Class[] { InvocationHandler.class });
            return (Test) cons.newInstance(new Object[] { handler });
        } catch (InstantiationException ie) {
            throw new RuntimeException(ie.toString());
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    class RunningTestProxy implements InvocationHandler {

        Test test;

        TestResult result;

        Listener listener;

        public RunningTestProxy(TestResult r, Test t) {
            result = r;
            test = t;
            listener = new Listener();
            result.addListener(listener);
        }

        public Object invoke(Object proxy, Method method, Object[] args) {
            listener.init();
            new TestletWrapper(test, method, args).run(result);
            return new Integer(listener.status());
        }
    }

    class Listener implements AssertListener {

        boolean gotAssert;

        boolean gotFailure;

        boolean gotError;

        Listener() {
            init();
        }

        public void init() {
            gotAssert = false;
            gotFailure = false;
            gotError = false;
        }

        /**
         * An assert happened.
         **/
        public void addAssert(Test test, String msg, boolean condition) {
            gotAssert = true;
        }

        public void addError(Test test, Throwable t) {
            gotError = true;
        }

        public void addFailure(Test test, AssertionFailedError t) {
            gotFailure = true;
        }

        public void endTest(Test test) {
        }

        public void startTest(Test test) {
        }

        public int status() {
            if (gotError) {
                return ERRORED;
            } else if (gotFailure) {
                return FAILED;
            } else {
                return PASSED;
            }
        }
    }
}
