package sketch.ounit.fuzz;

import java.util.List;
import sketch.ounit.Values;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AssembledSequenceObjectMutationTest extends TestCase {

    public static Test suite() {
        return new TestSuite(AssembledSequenceObjectMutationTest.class);
    }

    public void testSequenceMutation() {
        int time_limit = 5;
        int num_to_create = 10;
        Class<?>[] classes = new Class<?>[] { treemap.TreeMap.class };
        SharedSequencePool.init_pool(time_limit, classes);
        while (num_to_create-- > 0) {
            System.out.println("******* a new round: " + num_to_create);
            SharedSequencePool.startNewRound();
            Throwable exception = null;
            try {
                treemap.TreeMap map1 = new treemap.TreeMap();
                map1 = Values.randomMutate(map1, treemap.TreeMap.class);
                treemap.TreeMap map2 = new treemap.TreeMap();
                map2 = Values.randomMutate(map2, treemap.TreeMap.class);
                System.out.println("map1: " + map1);
                System.out.println("map2: " + map2);
                System.out.println("Are they equal: " + map1.equals(map2));
            } catch (Exception e) {
                exception = e;
            }
            if (exception == null) {
                List<String> code = SharedSequencePool.getGeneratedCode();
                int count = 0;
                for (String c : code) {
                    System.out.println(count++ + ".");
                    System.out.println(c);
                }
                List<AssembledSequence<?>> sequences = SharedSequencePool.getGeneratedSequence();
                System.out.println("see sequence with receiver / arg names:");
                count = 0;
                for (AssembledSequence<?> seq : sequences) {
                    System.out.println(count++ + ".");
                    if (count == 1) {
                        System.out.println(seq.toCodeString(10, "map1", null, null));
                    }
                    if (count == 2) {
                        System.out.println(seq.toCodeString(20, "map2", null, null));
                    }
                }
            } else {
                System.out.println("fail to output tests, exception: " + exception);
            }
        }
    }
}
