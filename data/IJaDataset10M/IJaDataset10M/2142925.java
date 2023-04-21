package nzdis.util.event;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * 
 * 
 *<br>
 * TestDispatcherSupport.java<br>
 * <br>
 * Created: Sun Oct 31 18:06:34 1999<br>
 *
 * @author Mariusz Nowostawski
 * @version 0.1 $Revision: 1.1 $
 */
public class TestDispatcherSupport extends TestCase {

    Dispatcher _mainDispatcher;

    DispatcherSupport _support;

    Boolean _mainResult;

    public TestDispatcherSupport(String name) {
        super(name);
    }

    /** Set up the testing environment. */
    protected void setUp() {
        _mainDispatcher = new Dispatcher() {

            public void success(Object o) {
                _mainResult = new Boolean(true);
            }

            public void failure(Object o) {
                _mainResult = new Boolean(false);
            }
        };
        _mainResult = null;
    }

    /**
   * Tests ALL_SUCCESS mode when all clients respponded with success. */
    public void testAllSuccessTrue() {
        _support = new DispatcherSupport(DispatcherSupport.ALL_SUCCESS, 2, _mainDispatcher);
        _support.success(null);
        assertNull(_mainResult);
        _support.success(null);
        assertNotNull(_mainResult);
        assertEquals(new Boolean(true), _mainResult);
    }

    /**
   * Tests ALL_SUCCESS mode when the first client failed. */
    public void testAllSuccessFalse() {
        _support = new DispatcherSupport(DispatcherSupport.ALL_SUCCESS, 2, _mainDispatcher);
        _support.failure(null);
        assertNotNull(_mainResult);
        assertEquals(new Boolean(false), _mainResult);
    }

    /**
   * Tests ANY_SUCCESS mode when first client failed but second succeeded. */
    public void testAnySuccessFirstFalse() {
        _support = new DispatcherSupport(DispatcherSupport.ANY_SUCCESS, 2, _mainDispatcher);
        _support.failure(null);
        assertNull(_mainResult);
        _support.success(null);
        assertNotNull(_mainResult);
        assertEquals(new Boolean(true), _mainResult);
    }

    /**
   * Tests ANY_SUCCESS mode when first client succeeded. */
    public void testAnySuccessTrue() {
        _support = new DispatcherSupport(DispatcherSupport.ANY_SUCCESS, 2, _mainDispatcher);
        _support.failure(null);
        assertNull(_mainResult);
        _support.success(null);
        assertNotNull(_mainResult);
        assertEquals(new Boolean(true), _mainResult);
    }

    /**
   * Tests ANY_SUCCESS mode when both clients failed. */
    public void testAnySuccessFalse() {
        _support = new DispatcherSupport(DispatcherSupport.ANY_SUCCESS, 2, _mainDispatcher);
        _support.failure(null);
        assertNull(_mainResult);
        _support.failure(null);
        assertNotNull(_mainResult);
        assertEquals(new Boolean(false), _mainResult);
    }

    /**
   * Tests ALL_FAILURE mode when both clients failed. */
    public void testAllFailureTrue() {
        _support = new DispatcherSupport(DispatcherSupport.ALL_FAILURE, 2, _mainDispatcher);
        _support.failure(null);
        assertNull(_mainResult);
        _support.failure(null);
        assertNotNull(_mainResult);
        assertEquals(new Boolean(true), _mainResult);
    }

    /**
   * Tests ALL_FAILURE mode when first client succeeded. */
    public void testAllFailureFalse() {
        _support = new DispatcherSupport(DispatcherSupport.ALL_FAILURE, 2, _mainDispatcher);
        _support.success(null);
        assertNotNull(_mainResult);
        assertEquals(new Boolean(false), _mainResult);
    }

    /**
   * Tests ANY_FAILURE mode when first client succeeded but second failed. */
    public void testAnyFailureFirstTrue() {
        _support = new DispatcherSupport(DispatcherSupport.ANY_FAILURE, 2, _mainDispatcher);
        _support.success(null);
        assertNull(_mainResult);
        _support.failure(null);
        assertNotNull(_mainResult);
        assertEquals(new Boolean(true), _mainResult);
    }

    /**
   * Tests ANY_FAILURE mode when first client failed. */
    public void testAnyFailureTrue() {
        _support = new DispatcherSupport(DispatcherSupport.ANY_FAILURE, 2, _mainDispatcher);
        _support.failure(null);
        assertNotNull(_mainResult);
        assertEquals(new Boolean(true), _mainResult);
    }

    /**
   * Tests ANY_FAILURE mode when both clients succeeded. */
    public void testAnyFailureFalse() {
        _support = new DispatcherSupport(DispatcherSupport.ANY_FAILURE, 2, _mainDispatcher);
        _support.success(null);
        assertNull(_mainResult);
        _support.success(null);
        assertNotNull(_mainResult);
        assertEquals(new Boolean(false), _mainResult);
    }

    /**
   * Test suite for Dispatcher and DispatcherSupport  classes. */
    public static Test suite() {
        return new TestSuite(TestDispatcherSupport.class);
    }
}
