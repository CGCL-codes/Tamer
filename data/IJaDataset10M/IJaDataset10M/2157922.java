package org.charvolant.tmsnet.client;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for the {@link ConnectTransaction} class.
 *
 * @author Doug Palmer &lt;doug@charvolant.org&gt;
 *
 */
@SuppressWarnings("rawtypes")
public class ConnectTransactionTest extends TransactionTest<ConnectTransaction> {

    /**
   * @throws java.lang.Exception
   */
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.transactable = new TestTransactable();
        this.transaction = new ConnectTransaction<TestTransactable>();
        this.transaction.setClient(this.transactable);
        this.transactable.setTransaction(this.transaction);
        this.transactable.start();
    }

    /**
   * @throws java.lang.Exception
   */
    @After
    public void tearDown() throws Exception {
        this.transactable.stop();
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.client.Transaction#execute()}.
   */
    @Test
    public void testExecute() throws Exception {
        this.transaction.execute();
        this.waitForEnd();
        Assert.assertEquals("Committed", this.transaction.getStateName());
        Assert.assertEquals(2, this.transactable.getChanges());
        Assert.assertTrue(this.transactable.isConnected());
    }
}
