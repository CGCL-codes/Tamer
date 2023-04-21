package java.util;

import junit.framework.Test;
import junit.framework.TestSuite;

public class RandomTest extends junit.framework.TestCase {

    public void test_subclassing() throws Exception {
        class MyRandom extends Random {

            public String state;

            public MyRandom() {
                super();
            }

            public MyRandom(long l) {
                super(l);
            }

            @Override
            protected synchronized int next(int bits) {
                return state.length();
            }

            @Override
            public synchronized void setSeed(long seed) {
                state = Long.toString(seed);
            }
        }
        MyRandom r1 = new MyRandom();
        r1.nextInt();
        assertNotNull(r1.state);
        MyRandom r2 = new MyRandom(123L);
        r2.nextInt();
        assertNotNull(r2.state);
    }
}
