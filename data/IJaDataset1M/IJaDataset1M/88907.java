package org.jaxlib.vmtest;

import java.util.ArrayList;
import java.util.Iterator;
import org.jaxlib.junit.Benchmark;
import org.jaxlib.junit.PerformanceMeter;

/**
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: VariousVMBenchmarks.java 2267 2007-03-16 08:33:33Z joerg_wassmer $
 */
public final class VariousVMBenchmarks extends Benchmark {

    public static void main(String[] argv) {
        runSuite(VariousVMBenchmarks.class);
    }

    public VariousVMBenchmarks(String name) {
        super(name);
    }

    public void testIntComplement_useMinusPlus() {
        reportDescription("v = -(v + 1)");
        final int loops = 1000000000;
        int v;
        PerformanceMeter m = initBenchmark();
        m.start();
        for (int i = loops; --i >= 0; ) {
            v = -(i + 1);
        }
        m.stop(loops);
    }

    public void testIntComplement_useOperator() {
        reportDescription("v ~= v");
        final int loops = 1000000000;
        int v;
        PerformanceMeter m = initBenchmark();
        m.start();
        for (int i = loops; --i >= 0; ) {
            v = ~i;
        }
        m.stop(loops);
    }

    public void testStringIntern_AllreadyIntern() {
        final int loops = 1000000;
        String[] a = new String[loops];
        for (int i = loops; --i >= 0; ) a[i] = "abcdef";
        PerformanceMeter m = initBenchmark();
        m.start();
        for (int i = loops; --i >= 0; ) {
            a[i].intern();
        }
        m.stop(loops);
    }

    public void testStringIntern_NewString() {
        final int loops = 10000;
        String[] a = new String[loops];
        for (int i = loops; --i >= 0; ) a[i] = Integer.toString(i + 1000);
        PerformanceMeter m = initBenchmark();
        m.start();
        for (int i = loops; --i >= 0; ) {
            a[i].intern();
        }
        m.stop(loops);
    }

    public void testIntegerNew() {
        final int LOOPS = 20000000;
        Integer v;
        PerformanceMeter m = initBenchmark();
        m.start();
        for (int i = LOOPS; --i >= 0; ) {
            v = new Integer(i);
        }
        m.stop(LOOPS);
    }

    public void testIntegerValueOf() {
        final int LOOPS = 20000000;
        Integer v;
        PerformanceMeter m = initBenchmark();
        m.start();
        for (int i = LOOPS; --i >= 0; ) {
            v = Integer.valueOf(1);
        }
        m.stop(LOOPS);
    }

    public void testInvertBoolean1_useNot() {
        reportDescription("v = !v");
        final int loops = 1000000000;
        boolean b = false;
        PerformanceMeter m = initBenchmark();
        m.start();
        for (int i = loops; --i >= 0; ) {
            b = !b;
        }
        m.stop(loops);
    }

    public void testInvertBoolean_useXOr() {
        reportDescription("v ^= v");
        final int loops = 1000000000;
        boolean b = false;
        PerformanceMeter m = initBenchmark();
        m.start();
        for (int i = loops; --i >= 0; ) {
            b ^= b;
        }
        m.stop(loops);
    }

    public void testIterateArrayList_avoidHasNext() {
        final int size = 5000000;
        ArrayList list = new ArrayList(size);
        for (int i = size; --i >= 0; ) list.add(list);
        long ops = 0;
        PerformanceMeter m = initBenchmark();
        m.start();
        int r = size;
        for (Iterator it = list.iterator(); --r >= 0; ) it.next();
        m.stop(size);
    }

    public void testIterateArrayList_useHasNext() {
        final int size = 5000000;
        ArrayList list = new ArrayList(size);
        for (int i = size; --i >= 0; ) list.add(list);
        long ops = 0;
        PerformanceMeter m = initBenchmark();
        m.start();
        for (Iterator it = list.iterator(); it.hasNext(); ) it.next();
        m.stop(size);
    }

    public void testSystemCurrentTimeMillis() {
        final int LOOPS = 500000;
        PerformanceMeter m = initBenchmark();
        m.start();
        for (int i = LOOPS; --i >= 0; ) {
            System.currentTimeMillis();
        }
        m.stop(LOOPS);
    }

    public void testSystemNanoTime() {
        final int LOOPS = 500000;
        PerformanceMeter m = initBenchmark();
        m.start();
        for (int i = LOOPS; --i >= 0; ) {
            System.nanoTime();
        }
        m.stop(LOOPS);
    }

    public void testThreadCurrentThread() {
        final int LOOPS = 20000000;
        Thread t = null;
        PerformanceMeter m = initBenchmark();
        m.start();
        for (int i = LOOPS; --i >= 0; ) {
            t = Thread.currentThread();
        }
        m.stop(LOOPS);
    }

    public void testThreadLocalGet() {
        final int LOOPS = 20000000;
        ThreadLocal t = new ThreadLocal();
        PerformanceMeter m = initBenchmark();
        m.start();
        for (int i = LOOPS; --i >= 0; ) {
            t.get();
        }
        m.stop(LOOPS);
    }

    public void testThreadLocalSet() {
        final int LOOPS = 20000000;
        ThreadLocal t = new ThreadLocal();
        PerformanceMeter m = initBenchmark();
        m.start();
        for (int i = LOOPS; --i >= 0; ) {
            t.set(null);
        }
        m.stop(LOOPS);
    }
}
