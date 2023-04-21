package jseki.common.lang;

import org.junit.Assert;
import org.junit.Test;

public class BlockingListTest extends Assert {

    @Test
    public void testList() throws InterruptedException {
        final BlockingList<String> list = new BlockingList<String>();
        Thread t1 = new Thread() {

            @Override
            public void run() {
                try {
                    assertEquals("three", list.get(2));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        t1.start();
        Thread t2 = new Thread() {

            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                list.add("one");
                list.add("two");
                list.add("three");
            }
        };
        t2.start();
        Thread t3 = new Thread() {

            @Override
            public void run() {
                try {
                    assertEquals("one", list.get(0));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        t3.start();
        t1.join();
        t2.join();
        t3.join();
    }
}
