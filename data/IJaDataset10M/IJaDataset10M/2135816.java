package net.taylor.quartz;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.taylor.embedded.Bootstrap;
import net.taylor.embedded.ContainerTestCase;
import net.taylor.quartz.MyJobScheduler.MyJob;

public class QuartzTest extends ContainerTestCase {

    public QuartzTest(String name) {
        super(name);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new QuartzTest("testJob"));
        return new Bootstrap(suite);
    }

    public void testJob() throws Exception {
        new ComponentTest() {

            protected void testComponents() throws Exception {
                Thread.sleep(2000);
                Integer count = MyJob.instance().getCount();
                System.out.println("JOB COUNTER: " + count);
                assertTrue(count > 1);
            }
        }.run();
    }
}
