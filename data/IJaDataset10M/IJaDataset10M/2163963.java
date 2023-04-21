package net.sf.asyncobjects.net.loopback;

import junit.framework.TestSuite;
import net.sf.asyncobjects.Promise;
import net.sf.asyncobjects.net.nio.NIOSocketFactory;

public class NIOSocketImplementationTest extends SocketImplementationTest {

    /** Using superclass constructor */
    public NIOSocketImplementationTest(String name) {
        super(name);
    }

    /** Using superclass setup */
    public Promise asyncSetUp() throws Throwable {
        return super.asyncSetUp();
    }

    /** Using superclass teardown */
    public Promise asyncTearDown() throws Throwable {
        return super.asyncTearDown();
    }

    public Promise testBind() {
        return super.testBind();
    }

    public Promise testConnect() {
        return super.testConnect();
    }

    public Promise testIO() {
        return super.testIO();
    }

    /**
	 * this accessor is the one that makes this test specific to NIO socket
	 * implementation
	 */
    Class getSocketFactoryClass() {
        return NIOSocketFactory.class;
    }

    /** test suite */
    public static TestSuite suite() {
        return collectTests(NIOSocketImplementationTest.class);
    }
}
