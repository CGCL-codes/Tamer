package samples;

import junit.framework.*;

public class LongTimeExecutionTest1 extends TestCase {

    public void test1() throws Exception {
        Thread.sleep(5000);
    }

    public void test2() throws Exception {
        Thread.sleep(5000);
    }

    public void testA() throws Exception {
        Thread.sleep(5000);
    }

    public void testB() throws Exception {
        Thread.sleep(5000);
    }
}
