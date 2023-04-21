package engine.distribution.master;

import java.util.List;
import junit.framework.Assert;
import org.testng.annotations.Test;

/** 
 * Tests for {@link SlaveManager}. 
 * @author Karol Stosiek (karol.stosiek@gmail.com)
 * @author Michal Anglart (anglart.michal@gmail.com)
 */
public class SlaveManagerTest {

    /** Self-explanatory. */
    @Test
    public void getCurrentSlavesTest() {
        String slave1 = new String("slave1");
        String slave2 = new String("slave2");
        String slave3 = new String("slave3");
        SlaveInformation info1 = new SlaveInformation();
        SlaveInformation info2 = new SlaveInformation();
        SlaveInformation info3 = new SlaveInformation();
        SlaveManager slaveManager = new SlaveManager(5, -1);
        slaveManager.addSlave(slave1, info1);
        slaveManager.addSlave(slave2, info2);
        slaveManager.addSlave(slave3, info3);
        final int totalSlaves = 3;
        List<String> registry = slaveManager.getAvailableSlaves(totalSlaves);
        Assert.assertTrue(registry.contains(slave1));
        Assert.assertTrue(registry.contains(slave2));
        Assert.assertTrue(registry.contains(slave3));
    }

    /** Self-explanatory. */
    @Test
    public void slaveTimeoutTest() {
        String slave1 = new String("slave1");
        String slave2 = new String("slave2");
        String slave3 = new String("slave3");
        SlaveInformation info1 = new SlaveInformation(100);
        SlaveInformation info2 = new SlaveInformation(200);
        SlaveInformation info3 = new SlaveInformation(150);
        SlaveManager slaveManager = new SlaveManager(3, 50);
        slaveManager.addSlave(slave1, info1);
        slaveManager.addSlave(slave2, info2);
        slaveManager.addSlave(slave3, info3);
        Assert.assertTrue(slaveManager.isTimedOut(slave1, 200));
        Assert.assertFalse(slaveManager.isTimedOut(slave2, 200));
        Assert.assertFalse(slaveManager.isTimedOut(slave3, 200));
    }
}
