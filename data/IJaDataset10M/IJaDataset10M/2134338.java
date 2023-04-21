package net.jadoth.test.collections;

import java.util.HashMap;
import net.jadoth.collections.EqHashTable;
import net.jadoth.collections.X;

public class MainTestMapInstantiation {

    public static void main(final String[] args) {
        final int size = 1000 * 100;
        final Object[] array = new Object[size];
        new HashMap<Object, Object>();
        X.<Object, Object>ValueMap();
        new EqHashTable<Object, Object>();
        long tStart, tStop;
        for (int r = 10000; r-- > 0; ) {
            tStart = System.nanoTime();
            for (int i = 0; i < size; i++) {
                array[i] = new EqHashTable<Object, Object>();
            }
            tStop = System.nanoTime();
            System.out.println("Elapsed Time: " + new java.text.DecimalFormat("00,000,000,000").format(tStop - tStart));
        }
    }
}
