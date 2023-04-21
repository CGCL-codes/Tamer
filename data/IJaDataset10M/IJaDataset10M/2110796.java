package org.gbif.profiler;

import java.util.Random;

public class IntStringFactory implements ObjFactory {

    private Random rnd = new Random();

    @Override
    public Object create() {
        String x = "";
        int i = 10;
        boolean first = true;
        while (i > 0) {
            i--;
            int val = rnd.nextInt();
            if (first) {
                first = false;
                x = String.valueOf(val);
            } else {
                x += ";" + String.valueOf(val);
            }
        }
        return x;
    }
}
