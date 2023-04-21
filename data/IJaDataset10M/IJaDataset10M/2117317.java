package org.charvolant.tmsnet.client;

import org.charvolant.tmsnet.model.RecordingDescription;
import org.charvolant.tmsnet.model.Timer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for the {@link SetRecordingDurationTransaction} class.
 *
 * @author Doug Palmer &lt;doug@charvolant.org&gt;
 *
 */
public class SetRecordingDurationTransactionTest extends TransactionTest<SetRecordingDurationTransaction> {

    /** The slot */
    private static final int SLOT = 0;

    /** The duration to add */
    private static final int DURATION = 30;

    /**
   * @throws java.lang.Exception
   */
    @Before
    public void setUp() throws Exception {
        RecordingDescription rec;
        this.transactable = new TestTransactable();
        rec = this.transactable.getPvr().getState().getRecording(this.SLOT).toRecordingDescription();
        this.transactable.getState().getRecordings().add(new Timer(this.transactable.getState(), rec, this.transactable.getState().getRecordings().size()));
        this.transaction = new SetRecordingDurationTransaction(this.SLOT, this.DURATION);
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
        int oldDuration = this.transactable.getPvr().getState().getRecording(this.SLOT).getDuration();
        Timer recording;
        this.transaction.execute();
        this.waitForEnd();
        Assert.assertEquals("Executed", this.transaction.getStateName());
        Assert.assertTrue(this.transaction.isIdle());
        Assert.assertFalse(this.transaction.isStopped());
        recording = this.transactable.getState().getRecording(this.SLOT);
        Assert.assertNotNull(recording);
        Assert.assertFalse(recording.getDuration() == oldDuration);
        Assert.assertEquals(this.DURATION, recording.getDuration());
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.client.Transaction#commit()}.
   */
    @Test
    public void testCommit() throws Exception {
        int oldDuration = this.transactable.getPvr().getState().getRecording(this.SLOT).getDuration();
        Timer recording;
        this.transaction.execute();
        this.waitForEnd();
        this.transaction.commit();
        this.waitForEnd();
        Assert.assertEquals("Committed", this.transaction.getStateName());
        Assert.assertTrue(this.transaction.isStopped());
        recording = this.transactable.getState().getRecording(this.SLOT);
        Assert.assertNotNull(recording);
        Assert.assertFalse(recording.getDuration() == oldDuration);
        Assert.assertEquals(this.DURATION, recording.getDuration());
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.client.Transaction#rollback()}.
   */
    @Test
    public void testRollback() throws Exception {
        int oldDuration = this.transactable.getPvr().getState().getRecording(this.SLOT).getDuration();
        Timer recording;
        this.transaction.execute();
        this.waitForEnd();
        this.transaction.rollback();
        this.waitForEnd();
        Assert.assertEquals("RolledBack", this.transaction.getStateName());
        Assert.assertTrue(this.transaction.isStopped());
        recording = this.transactable.getState().getRecording(this.SLOT);
        Assert.assertNotNull(recording);
        Assert.assertEquals(oldDuration, recording.getDuration());
    }
}
