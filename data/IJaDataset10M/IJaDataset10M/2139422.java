package com.pentagaia.tb.tx.testbase;

import org.junit.Assert;
import com.pentagaia.tb.tx.api.ITransaction;
import com.pentagaia.tb.tx.api.ITransactionManager;
import com.pentagaia.tb.tx.testbase.util.PersistentObject;

/**
 * A runnable to test the transaction startups
 *
 * @author mepeisen
 * @version 0.1.0
 * @since 0.1.0
 * 
 * @see AbstractTxBaseTest#testTransactionStart()
 */
public class RunnableTransactionStart extends RunnableDataStorage implements Runnable {

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        final ITransactionManager txManager = AbstractTxBaseTest.getTxManager();
        final ITransaction tx1 = txManager.current();
        Assert.assertNull("There must not be an active transaction", tx1);
        final ITransaction tx2 = txManager.begin(false);
        Assert.assertNotNull("begin() must not return null", tx2);
        Assert.assertTrue("begin() and current() must return the same object", tx2 == txManager.current());
        this.setObject("junit.foo", new PersistentObject(1));
        tx2.commit();
        final ITransaction tx3 = txManager.current();
        Assert.assertNull("There must not be an active transaction", tx3);
        final ITransaction tx4 = txManager.begin(false);
        Assert.assertNotNull("begin() must not return null", tx4);
        Assert.assertTrue("begin() and current() must return the same object", tx4 == txManager.current());
        final PersistentObject obj = this.getObject("junit.foo");
        Assert.assertNotNull("the persistent object was not commited. Binding returns null.", obj);
        Assert.assertEquals("Persistent object contains the wrong value", 1, obj.get());
        tx4.commit();
    }
}
