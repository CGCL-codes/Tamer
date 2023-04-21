package org.fishwife.jrugged.examples;

import java.util.Random;

public class BreakerResponseTweaker {

    public int delay() throws Exception {
        Random r = new Random();
        int count = r.nextInt(2001);
        if (count > 1000) {
            throw new Exception("Count was over the limit.");
        }
        try {
            Thread.sleep(count);
        } catch (InterruptedException e) {
        }
        return count;
    }
}
