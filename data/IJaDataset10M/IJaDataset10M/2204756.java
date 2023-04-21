package net.sf.persistant.util;

import junit.framework.TestCase;
import static org.easymock.EasyMock.*;

/**
 * <p>
 * <code>TestChainImpl</code> provides test cases for {@link ChainImpl}.
 * </p>
 */
public class TestChainImpl extends TestCase {

    /**
     * <p>
     * Construct a {@link TestChainImpl} instance.
     * </p>
     */
    public TestChainImpl() {
        super();
    }

    /**
     * <p>
     * Verify that the {@link Chain#handle(Object)} implementation iterates through handlers until (and only until) one
     * handles the request. 
     * </p>
     */
    public void testContinueUntilHandled() {
        final String mockRequest = "request";
        final Handler<String> mockHandler1 = createStrictMock(Handler.class);
        expect(mockHandler1.handle(same(mockRequest))).andReturn(false).once();
        replay(mockHandler1);
        final Handler<String> mockHandler2 = createStrictMock(Handler.class);
        expect(mockHandler2.handle(same(mockRequest))).andReturn(true).once();
        replay(mockHandler2);
        final Handler<String> mockHandler3 = createStrictMock(Handler.class);
        replay(mockHandler3);
        final Chain<String> instance = new ChainImpl(mockHandler1, mockHandler2, mockHandler3);
        assertTrue(instance.handle(mockRequest));
        verify(mockHandler1, mockHandler2, mockHandler3);
    }

    /**
     * <p>
     * Verify that an empty chain always returns <code>false</code> from its {@link Chain#handle(Object)}
     * implementation. 
     * </p>
     */
    public void testEmptyChain() {
        final Chain<String> instance = new ChainImpl();
        assertFalse(instance.handle("myRequest"));
    }
}
