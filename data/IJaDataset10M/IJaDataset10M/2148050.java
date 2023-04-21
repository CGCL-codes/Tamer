package org.hibernate.test.cache.jbc2.entity;

import org.hibernate.cache.access.AccessType;
import org.jboss.cache.transaction.BatchModeTransactionManager;

/**
 * Base class for tests of TRANSACTIONAL access.
 * 
 * @author <a href="brian.stansberry@jboss.com">Brian Stansberry</a>
 * @version $Revision: 1 $
 */
public abstract class AbstractReadOnlyAccessTestCase extends AbstractEntityRegionAccessStrategyTestCase {

    /**
     * Create a new AbstractTransactionalAccessTestCase.
     * 
     */
    public AbstractReadOnlyAccessTestCase(String name) {
        super(name);
    }

    @Override
    protected AccessType getAccessType() {
        return AccessType.READ_ONLY;
    }

    @Override
    public void testPutFromLoad() throws Exception {
        putFromLoadTest(false);
    }

    @Override
    public void testPutFromLoadMinimal() throws Exception {
        putFromLoadTest(true);
    }

    private void putFromLoadTest(boolean minimal) throws Exception {
        final String KEY = KEY_BASE + testCount++;
        long txTimestamp = System.currentTimeMillis();
        BatchModeTransactionManager.getInstance().begin();
        assertNull(localAccessStrategy.get(KEY, System.currentTimeMillis()));
        if (minimal) localAccessStrategy.putFromLoad(KEY, VALUE1, txTimestamp, new Integer(1), true); else localAccessStrategy.putFromLoad(KEY, VALUE1, txTimestamp, new Integer(1));
        sleep(250);
        Object expected = isUsingInvalidation() ? null : VALUE1;
        assertEquals(expected, remoteAccessStrategy.get(KEY, System.currentTimeMillis()));
        BatchModeTransactionManager.getInstance().commit();
        assertEquals(VALUE1, localAccessStrategy.get(KEY, System.currentTimeMillis()));
        assertEquals(expected, remoteAccessStrategy.get(KEY, System.currentTimeMillis()));
    }

    @Override
    public void testUpdate() throws Exception {
        final String KEY = KEY_BASE + testCount++;
        try {
            localAccessStrategy.update(KEY, VALUE2, new Integer(2), new Integer(1));
            fail("Call to update did not throw exception");
        } catch (UnsupportedOperationException good) {
        }
    }
}
