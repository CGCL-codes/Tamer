package org.jaqlib.db.java.typehandler;

import junit.framework.TestCase;
import org.jaqlib.Account;
import org.jaqlib.AccountImpl;

public class DefaultJavaTypeHandlerRegistryTest extends TestCase {

    private DefaultJavaTypeHandlerRegistry registry;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        registry = new DefaultJavaTypeHandlerRegistry();
    }

    public void testGetTypeHandler_Null() {
        try {
            registry.getTypeHandler(null);
            fail("Did not throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
   * No java type handler for given class available. A NullJavaTypeHandler must
   * be returned.
   */
    public void testGetTypeHandler_NoneAvailable() {
        assertEquals(NullJavaTypeHandler.class, registry.getTypeHandler(Account.class).getClass());
    }

    public void testRegisterTypeHandler_Null() {
        try {
            registry.registerTypeHandler(null, new CreditRatingTypeHandler());
            fail("Did not throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            registry.registerTypeHandler(AccountImpl.class, null);
            fail("Did not throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testRegisterTypeHandler() {
        CreditRatingTypeHandler th = new CreditRatingTypeHandler();
        registry.registerTypeHandler(Integer.class, th);
        assertSame(th, registry.getTypeHandler(Integer.class));
    }
}
