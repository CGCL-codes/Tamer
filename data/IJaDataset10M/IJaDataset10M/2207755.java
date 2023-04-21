package org.jaqlib.core.bean;

import java.sql.SQLException;
import junit.framework.TestCase;
import org.jaqlib.AccountImpl;
import org.jaqlib.AccountSetup;
import org.jaqlib.CreditRating;
import org.jaqlib.CreditRatingTypeHandler;
import org.jaqlib.core.MockDsResultSet;

public class BeanMappingTest extends TestCase {

    private BeanMapping<AccountImpl> mapping;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mapping = new BeanMapping<AccountImpl>(AccountImpl.class);
        mapping.setMappingStrategy(new BeanConventionMappingStrategy());
    }

    public void testGetValue() throws SQLException {
        final Long id = AccountSetup.HUBER_ACCOUNT.getId();
        final String lastName = AccountSetup.HUBER_ACCOUNT.getLastName();
        MockDsResultSet rs = new MockDsResultSet();
        AccountImpl account = mapping.getValue(rs);
        assertNotNull(account);
        assertEquals(id, account.getId());
        assertEquals(lastName, account.getLastName());
    }

    public void testBeanMapping() {
        try {
            new BeanMapping<AccountImpl>(null);
            fail("Did not throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testSetBeanFactory() {
        try {
            mapping.setFactory(null);
            fail("Did not throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testRegisterJavaTypeHandler() {
        try {
            mapping.registerJavaTypeHandler(null);
            fail("Did not throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testSetJavaTypeHandlerRegistry() {
        try {
            mapping.setJavaTypeHandlerRegistry(null);
            fail("Did not throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testApplyJavaTypeHandler_JavaTypeHandlerRegistered() {
        mapping.registerJavaTypeHandler(new CreditRatingTypeHandler());
        Object result = mapping.applyJavaTypeHandler("creditRating", CreditRating.GOOD.intValue());
        assertEquals(CreditRating.GOOD, result);
    }

    public void testApplyJavaTypeHandler_NoJavaTypeHandler() {
        assertEquals("abc", mapping.applyJavaTypeHandler("lastName", "abc"));
    }
}
