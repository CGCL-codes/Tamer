package org.charvolant.tmsnet.client;

import org.charvolant.tmsnet.model.ServiceType;
import org.charvolant.tmsnet.model.Station;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for the {@link ChangeChannelTransaction} class.
 *
 * @author Doug Palmer &lt;doug@charvolant.org&gt;
 *
 */
public class ChangeChannelTransactionTest extends TransactionTest<ChangeChannelTransaction> {

    private static final int STATION = 1;

    /** The event */
    private Station station;

    /**
   * @throws java.lang.Exception
   */
    @Before
    public void setUp() throws Exception {
        this.transactable = new TestTransactable();
        this.station = this.transactable.getPvr().getState().getStation(ServiceType.TV, this.STATION);
        this.transaction = new ChangeChannelTransaction(this.station);
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
        int oldChannel = this.transactable.getState().getCurrentChannel().getNumber();
        this.transaction.execute();
        this.waitForEnd();
        Assert.assertEquals("Committed", this.transaction.getStateName());
        Assert.assertTrue(this.transaction.isStopped());
        Assert.assertFalse(oldChannel == this.STATION);
        Assert.assertEquals(this.STATION, this.transactable.getPvr().getState().getCurrentChannel().getNumber());
        Assert.assertEquals(this.STATION, this.transactable.getState().getCurrentChannel().getNumber());
    }
}
