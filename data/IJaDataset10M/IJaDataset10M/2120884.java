package test.client.bpmModel.bpelCommands;

import static org.junit.Assert.fail;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import api.client.bpmModel.bpelCommands.Pick;

public class PickTest {

    Pick p;

    @Before
    public void setUp() throws Exception {
        p = new Pick("Instance1");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testPick() {
        Assert.assertNotNull(p);
        Assert.assertFalse(p.myUtility.isEmpty());
    }
}
