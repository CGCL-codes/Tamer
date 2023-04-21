package newsatort.test;

import newsatort.schedule.ScheduleManager;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestExampleJUnit {

    private static final ScheduleManager scheduleManager = new ScheduleManager();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        scheduleManager.start();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        scheduleManager.stop();
    }

    @Test()
    public void testMinimumAdresse() {
        Assert.assertNotNull("Id not null", null);
    }
}
