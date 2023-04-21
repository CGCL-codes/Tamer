package org.jaqlib.db;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import org.easymock.EasyMock;
import org.jaqlib.Account;
import org.jaqlib.AccountImpl;
import org.jaqlib.core.ElementPredicate;
import org.jaqlib.core.reflect.MethodInvocation;

public class DatabaseQueryCacheTest extends TestCase {

    private ElementPredicate<Account> predicate;

    private DatabaseQueryCache<Account> cache;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        predicate = EasyMock.createMock(ElementPredicate.class);
        cache = new DatabaseQueryCache<Account>(predicate);
    }

    public void testDatabaseQueryCache_Null() {
        try {
            new DatabaseQueryCache<Account>(null);
            fail("Did not throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testIsFilled() {
        assertFalse(cache.isFilled());
        cache.setFilled();
        assertTrue(cache.isFilled());
    }

    public void testAddResults_Collection() {
        AccountImpl account1 = addAccount();
        AccountImpl account2 = addAccount();
        AccountImpl account3 = addAccount();
        EasyMock.expect(predicate.matches(account1)).andReturn(false);
        EasyMock.expect(predicate.matches(account2)).andReturn(true);
        EasyMock.expect(predicate.matches(account3)).andReturn(false);
        EasyMock.replay(predicate);
        List<Account> results = new ArrayList<Account>();
        cache.addResults(results);
        assertEquals(1, results.size());
        assertSame(account2, results.get(0));
        EasyMock.verify(predicate);
    }

    private AccountImpl addAccount() {
        AccountImpl account = new AccountImpl();
        cache.add(account);
        return account;
    }

    public void testAddResults_Map() throws NoSuchMethodException {
        AccountImpl account1 = addAccount();
        AccountImpl account2 = addAccount();
        AccountImpl account3 = addAccount();
        EasyMock.expect(predicate.matches(account1)).andReturn(false);
        EasyMock.expect(predicate.matches(account2)).andReturn(true);
        EasyMock.expect(predicate.matches(account3)).andReturn(false);
        EasyMock.replay(predicate);
        MethodInvocation invocation = getMethodInvocation();
        Map<Long, Account> results = new HashMap<Long, Account>();
        cache.addResults(results, invocation);
        assertEquals(1, results.size());
        assertTrue(results.containsValue(account2));
        EasyMock.verify(predicate);
    }

    private MethodInvocation getMethodInvocation() throws NoSuchMethodException {
        Method method = AccountImpl.class.getMethod("getId", new Class[0]);
        return new MethodInvocation(method, new Object[0]);
    }
}
