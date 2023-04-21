package joj.web.context;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import joj.web.ContainerHookedObjectsTestBase;
import org.jmock.Expectations;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>Test class for {@link ContextAccessorFactory}.</p>
 *
 * @author Jason Miller (heinousjay@gmail.com)
 *
 * @see ContextAccessorFactory
 */
public class ContextAccessorFactoryTest extends ContainerHookedObjectsTestBase {

    public static interface ContextAccessorInterface extends ContextAccessor {

        public String getBroken();

        public int getInt();

        public String getString();

        public boolean isBoolean();

        public void setBoolean(boolean bool);

        public void setBroken(int broken);

        public void setInt(int integer);

        public void setString(String string);
    }

    private static final String INT_NAME = "int";

    private static final String BOOLEAN_NAME = "boolean";

    private static final String STRING_NAME = "string";

    private static final String BROKEN_NAME = "broken";

    final String stringValue = "value";

    final int intValue = 1;

    final boolean boolValue = true;

    /**
	 * This is ugly, but surefire isn't running the befores in base classes
	 */
    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testBrokenDeclarations() {
        context.checking(new Expectations() {

            {
                one(request).setAttribute(BROKEN_NAME, intValue);
                one(request).getAttribute(BROKEN_NAME);
                will(returnValue(intValue));
            }
        });
        final ContextAccessorInterface accessor = new ContextAccessorFactory().wrapContext(request, ContextAccessorInterface.class);
        accessor.setBroken(intValue);
        try {
            accessor.getBroken();
            fail("Should have thrown an exception here.");
        } catch (final Exception e) {
        }
    }

    /**
	 * Test method for {@link joj.web.context.ContextAccessorFactory#wrapContext(javax.servlet.http.HttpServletRequest, java.lang.Class)}.
	 */
    @Test
    public void testWrapHttpServletRequest() {
        context.checking(new Expectations() {

            {
                one(request).setAttribute(STRING_NAME, stringValue);
                one(request).getAttribute(STRING_NAME);
                will(returnValue(stringValue));
                one(request).setAttribute(BOOLEAN_NAME, boolValue);
                one(request).getAttribute(BOOLEAN_NAME);
                will(returnValue(boolValue));
                one(request).setAttribute(INT_NAME, intValue);
                one(request).getAttribute(INT_NAME);
                will(returnValue(intValue));
            }
        });
        final ContextAccessorInterface accessor = new ContextAccessorFactory().wrapContext(request, ContextAccessorInterface.class);
        accessor.setString(stringValue);
        assertThat(stringValue, is(accessor.getString()));
        accessor.setBoolean(boolValue);
        assertThat(boolValue, is(accessor.isBoolean()));
        accessor.setInt(intValue);
        assertThat(intValue, is(accessor.getInt()));
    }

    /**
	 * Test method for {@link joj.web.context.ContextAccessorFactory#wrapContext(javax.servlet.http.HttpSession, java.lang.Class)}.
	 */
    @Test
    public void testWrapHttpSession() {
        context.checking(new Expectations() {

            {
                one(session).setAttribute(STRING_NAME, stringValue);
                one(session).getAttribute(STRING_NAME);
                will(returnValue(stringValue));
                one(session).setAttribute(BOOLEAN_NAME, boolValue);
                one(session).getAttribute(BOOLEAN_NAME);
                will(returnValue(boolValue));
                one(session).setAttribute(INT_NAME, intValue);
                one(session).getAttribute(INT_NAME);
                will(returnValue(intValue));
            }
        });
        final ContextAccessorInterface accessor = new ContextAccessorFactory().wrapContext(session, ContextAccessorInterface.class);
        accessor.setString(stringValue);
        assertThat(stringValue, is(accessor.getString()));
        accessor.setBoolean(boolValue);
        assertThat(boolValue, is(accessor.isBoolean()));
        accessor.setInt(intValue);
        assertThat(intValue, is(accessor.getInt()));
    }

    /**
	 * Test method for {@link joj.web.context.ContextAccessorFactory#wrapContext(javax.servlet.ServletContext, java.lang.Class)}.
	 */
    @Test
    public void testWrapServletContext() {
        context.checking(new Expectations() {

            {
                one(servletContext).setAttribute(STRING_NAME, stringValue);
                one(servletContext).getAttribute(STRING_NAME);
                will(returnValue(stringValue));
                one(servletContext).setAttribute(BOOLEAN_NAME, boolValue);
                one(servletContext).getAttribute(BOOLEAN_NAME);
                will(returnValue(boolValue));
                one(servletContext).setAttribute(INT_NAME, intValue);
                one(servletContext).getAttribute(INT_NAME);
                will(returnValue(intValue));
            }
        });
        final ContextAccessorInterface accessor = new ContextAccessorFactory().wrapContext(servletContext, ContextAccessorInterface.class);
        accessor.setString(stringValue);
        assertThat(stringValue, is(accessor.getString()));
        accessor.setBoolean(boolValue);
        assertThat(boolValue, is(accessor.isBoolean()));
        accessor.setInt(intValue);
        assertThat(intValue, is(accessor.getInt()));
    }
}
