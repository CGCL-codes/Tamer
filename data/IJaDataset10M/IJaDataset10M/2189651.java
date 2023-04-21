package org.gamegineer.common.internal.core.services.logging;

import org.easymock.EasyMock;
import org.eclipse.osgi.framework.log.FrameworkLog;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * A fixture for testing the
 * {@link org.gamegineer.common.internal.core.services.logging.FrameworkLogHandlerFactory}
 * class.
 */
public final class FrameworkLogHandlerFactoryTest {

    /** The framework log handler factory under test in the fixture. */
    private FrameworkLogHandlerFactory factory_;

    /**
     * Initializes a new instance of the {@code FrameworkLogHandlerFactoryTest}
     * class.
     */
    public FrameworkLogHandlerFactoryTest() {
        super();
    }

    /**
     * Sets up the test fixture.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Before
    public void setUp() throws Exception {
        factory_ = new FrameworkLogHandlerFactory();
    }

    /**
     * Tears down the test fixture.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @After
    public void tearDown() throws Exception {
        factory_ = null;
    }

    /**
     * Ensures the {@code bindFrameworkLog} method throws an exception when
     * passed a {@code null} framework log service.
     */
    @Test(expected = NullPointerException.class)
    public void testBindFrameworkLog_FrameworkLog_Null() {
        factory_.bindFrameworkLog(null);
    }

    /**
     * Ensures the {@code bindFrameworkLog} method throws an exception when a
     * framework log service is already bound.
     */
    @Test(expected = IllegalStateException.class)
    public void testBindFrameworkLog_ServiceAlreadyBound() {
        factory_.bindFrameworkLog(EasyMock.createMock(FrameworkLog.class));
        factory_.bindFrameworkLog(EasyMock.createMock(FrameworkLog.class));
    }

    /**
     * Ensures the {@code unbindFrameworkLog} method throws an exception when
     * passed a {@code null} framework log service.
     */
    @Test(expected = NullPointerException.class)
    public void testUnbindFrameworkLog_FrameworkLog_Null() {
        factory_.unbindFrameworkLog(null);
    }

    /**
     * Ensures the {@code unbindFrameworkLog} method throws an exception when a
     * different framework log service is already bound.
     */
    @Test(expected = IllegalStateException.class)
    public void testUnbindFrameworkLog_DifferentServiceBound() {
        factory_.bindFrameworkLog(EasyMock.createMock(FrameworkLog.class));
        factory_.unbindFrameworkLog(EasyMock.createMock(FrameworkLog.class));
    }
}
