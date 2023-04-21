package org.xactor.test.tm.test;

import javax.transaction.Transaction;

/**
 * A simple transaction local stress test.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 37406 $
 */
public class SimpleUncontendedTransactionLocalStressTestCase extends AbstractTransactionLocalStressTest {

    public void testSimpleTransactionLocalStressTestcase() throws Throwable {
        SimpleTransactionLocalRunnable[] runnables = new SimpleTransactionLocalRunnable[getThreadCount()];
        for (int i = 0; i < runnables.length; ++i) runnables[i] = new SimpleTransactionLocalRunnable();
        runConcurrentTest(runnables, null);
    }

    public class SimpleTransactionLocalRunnable extends ConcurrentRunnable {

        Transaction tx;

        public void doStart() {
            try {
                tm.setTransactionTimeout(0);
                tm.begin();
                tx = tm.getTransaction();
            } catch (Throwable t) {
                failure = t;
            }
        }

        public void doEnd() {
            try {
                tm.commit();
            } catch (Throwable t) {
                failure = t;
            }
        }

        public void doRun() {
            try {
                local.lock(tx);
                try {
                    local.set(this);
                    local.get();
                } finally {
                    local.unlock(tx);
                }
            } catch (Throwable t) {
                failure = t;
            }
        }
    }

    public SimpleUncontendedTransactionLocalStressTestCase(String name) {
        super(name);
    }
}
