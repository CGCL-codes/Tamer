package net.jadoth.test.collections;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import net.jadoth.collections.BulkList;
import net.jadoth.experimental.ChainVarList;
import net.jadoth.math.JaMath;

/**
 * @author Thomas Muenz
 *
 */
public class MainTestVarListSort {

    static final int SIZE = 1000000;

    static final Integer[] createInts() {
        final Integer[] ints = new Integer[SIZE];
        for (int i = 0; i < SIZE; i++) {
            ints[i] = JaMath.random(SIZE);
        }
        return ints;
    }

    static final Comparator<Integer> ORDER_INTS = new Comparator<Integer>() {

        public int compare(final Integer o1, final Integer o2) {
            if (o1 == null) {
                return o2 == null ? 0 : 1;
            }
            if (o2 == null) {
                return -1;
            }
            if (o1.intValue() < o2.intValue()) {
                return -1;
            } else if (o1.intValue() > o2.intValue()) {
                return 1;
            }
            return 0;
        }
    };

    /**
	 * @param args
	 */
    public static void main(final String[] args) {
        for (int n = 10; n-- > 0; ) {
            final ChainVarList<Integer> vl = new ChainVarList<Integer>();
            final BulkList<Integer> fl = new BulkList<Integer>();
            final LinkedList<Integer> ll = new LinkedList<Integer>();
            final Integer[] ints = createInts();
            for (final Integer i : ints) {
                vl.add(i);
                fl.add(i);
                ll.add(i);
            }
            long tStart, tStop;
            tStart = System.nanoTime();
            Collections.sort(ll, ORDER_INTS);
            tStop = System.nanoTime();
            System.out.println("LL Elapsed Time: " + new java.text.DecimalFormat("00,000,000,000").format(tStop - tStart));
            tStart = System.nanoTime();
            vl.sort(ORDER_INTS);
            tStop = System.nanoTime();
            System.out.println("VL Elapsed Time: " + new java.text.DecimalFormat("00,000,000,000").format(tStop - tStart));
            tStart = System.nanoTime();
            fl.sort(ORDER_INTS);
            tStop = System.nanoTime();
            System.out.println("FL Elapsed Time: " + new java.text.DecimalFormat("00,000,000,000").format(tStop - tStart));
            System.out.println("");
        }
    }
}
