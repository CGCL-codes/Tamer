package bonneville.tests.capacity;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import bonneville.util.Globals;
import bonneville.util.Timer;

/**
 * Test whether specifying an explicit, large collection capacity is faster than allowing the JVM to increase capacity
 * when necessary. Compare results to TestAutoboxing, which is identical except that the size of the ArrayList is set
 * automatically in TestAutoboxing and explicitly in TestArrayListExplicitCapacity.
 */
public class TestArrayListExplicitCapacity {

    public static void main(String[] args) {
        final String JVM_MODE = args[0];
        final String TEST_NAME = args[1].substring(args[1].lastIndexOf(".") + 1);
        final long OUTER_LOOPS = Long.parseLong(args[2]);
        final long INTERMEDIATE_LOOPS = Long.parseLong(args[3]);
        final long INNER_LOOPS = Long.parseLong(args[4]);
        SortedMap<String, String> testParms = new TreeMap<String, String>();
        testParms.put(Globals.JVM_MODE_KEY, JVM_MODE);
        testParms.put(Globals.TEST_NAME_KEY, TEST_NAME);
        testParms.put(Globals.OUTER_LOOPS_KEY, new Long(OUTER_LOOPS).toString());
        testParms.put(Globals.INTERMEDIATE_LOOPS_KEY, new Long(INTERMEDIATE_LOOPS).toString());
        testParms.put(Globals.INNER_LOOPS_KEY, new Long(INNER_LOOPS).toString());
        Timer timer = new Timer(testParms);
        for (long i = 0; i < OUTER_LOOPS; i++) doWork(timer, INNER_LOOPS, INTERMEDIATE_LOOPS);
        timer.printAverageResults();
    }

    private static void doWork(Timer timer, long innerLoops, long intermediateLoops) {
        List<Long> list = null;
        long sum = 0;
        timer.start();
        for (long j = 0; j < intermediateLoops; j++) {
            list = new ArrayList<Long>((int) innerLoops);
            for (long k = 0; k < innerLoops; k++) {
                list.add(k);
                sum += list.get(list.size() / 2);
            }
        }
        timer.stopAndPrintResult();
        System.out.println(" | sum = " + sum);
    }
}
