package org.gbif.profiler;

import java.util.Random;
import gnu.trove.TIntHashSet;

public class IntSetFactory implements ObjFactory {

    private Random rnd = new Random();

    @Override
    public Object create() {
        TIntHashSet x = new TIntHashSet();
        int i = 10;
        while (i > 0) {
            i--;
            x.add(rnd.nextInt());
        }
        return x;
    }
}
