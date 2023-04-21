package test.client.bpmModel.bpelCommands;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import api.client.bpmModel.bpelCommands.Empty;
import api.client.bpmModel.bpelCommands.Sequence;

public class SequenceTest {

    Sequence s;

    @Before
    public void setUp() throws Exception {
        s = new Sequence();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSequence() {
        Assert.assertNotNull(s);
        Assert.assertFalse(s.myUtility.isEmpty());
    }

    @Test
    public void testAddBPELCommand() {
    }
}
