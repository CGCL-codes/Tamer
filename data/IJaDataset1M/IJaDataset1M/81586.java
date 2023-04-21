package org.hibernate.test.cache.jbc2.collection;

import org.hibernate.test.util.CacheTestUtil;
import junit.framework.Test;
import junit.framework.TestSuite;

/** 
 * Tests TRANSACTIONAL access when optimistic locking and invalidation are used.
 * 
 * @author <a href="brian.stansberry@jboss.com">Brian Stansberry</a>
 * @version $Revision: 1 $
 */
public class OptimisticInvalidatedTransactionalTestCase extends AbstractTransactionalAccessTestCase {

    /**
     * Create a new TransactionalAccessTestCase.
     * 
     * @param name
     */
    public OptimisticInvalidatedTransactionalTestCase(String name) {
        super(name);
    }

    public static Test suite() throws Exception {
        TestSuite suite = CacheTestUtil.createFailureExpectedSuite(OptimisticInvalidatedTransactionalTestCase.class);
        return getTestSetup(suite, "optimistic-entity");
    }

    @Override
    public void testCacheConfiguration() {
        assertTrue("Using Invalidation", isUsingInvalidation());
        assertTrue("Using Optimistic locking", isUsingOptimisticLocking());
        assertTrue("Synchronous mode", isSynchronous());
    }
}
