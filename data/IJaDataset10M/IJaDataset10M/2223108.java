package gnu.testlet.java2.util.Random;

import gnu.testlet.Testlet;
import gnu.testlet.TestHarness;
import java.util.Random;

public class basic implements Testlet {

    public void test(TestHarness harness) {
        Random rand;
        rand = new Random(122760);
        harness.check(rand.nextInt(), -1524104671);
        harness.check(rand.nextLong(), 2785759620113032781L);
        harness.check(String.valueOf(rand.nextDouble()), "0.8173322904425151");
        harness.check(String.valueOf(rand.nextFloat()), "0.8239248");
        byte[] b = new byte[0];
        rand.nextBytes(b);
        harness.check(rand.nextInt(), -899478426);
        rand = new Random(122760);
        rand.nextInt();
        rand.nextLong();
        rand.nextDouble();
        rand.nextFloat();
        b = new byte[3];
        rand.nextBytes(b);
        harness.check(b[0], 102);
        harness.check(b[1], 12);
        harness.check(b[2], 99);
        harness.check(rand.nextInt(), -1550323395);
        rand = new Random(122760);
        rand.nextInt();
        rand.nextLong();
        rand.nextDouble();
        rand.nextFloat();
        b = new byte[4];
        rand.nextBytes(b);
        harness.check(b[0], 102);
        harness.check(b[1], 12);
        harness.check(b[2], 99);
        harness.check(b[3], -54);
        harness.check(rand.nextInt(), -1550323395);
        rand = new Random(122760);
        rand.nextInt();
        rand.nextLong();
        rand.nextDouble();
        rand.nextFloat();
        b = new byte[5];
        rand.nextBytes(b);
        harness.check(b[0], 102);
        harness.check(b[1], 12);
        harness.check(b[2], 99);
        harness.check(b[3], -54);
        harness.check(b[4], 61);
        harness.check(rand.nextInt(), -270809961);
        boolean ok = true;
        rand = new Random(0);
        for (int i = 0; i < 1000000; ++i) {
            int x = rand.nextInt(1000);
            if (x < 0 || x >= 1000) ok = false;
        }
        harness.check(ok);
    }
}
