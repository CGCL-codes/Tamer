package sketch.ounit.fuzz;

import java.util.List;
import randoop.ComponentManager;
import sketch.ounit.Values;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AssembledSequenceObjectCreationTest extends TestCase {

    public static Test suite() {
        return new TestSuite(AssembledSequenceObjectCreationTest.class);
    }

    public void testAssembledSequence() {
        int time_limit = 5;
        int num_to_create = 10;
        SequencePool pool = new SequencePool(time_limit, treemap.TreeMap.class);
        ComponentManager components = pool.component_manager;
        while (num_to_create-- > 0) {
            System.out.println("----------------");
            AssembledSequenceObjectCreation<treemap.TreeMap> obj_creation = new AssembledSequenceObjectCreation<treemap.TreeMap>(treemap.TreeMap.class, components);
            treemap.TreeMap map = obj_creation.assemble_and_execute_sequence();
            System.out.println("TreeMap: " + map);
            System.out.println("code: ");
            System.out.println(obj_creation.toCodeString());
            System.out.println();
            System.out.println("code with offset: " + num_to_create);
            System.out.println(obj_creation.toCodeString(num_to_create, null, null, null));
        }
    }

    public void testAssembledSequenceSimualate() {
        int time_limit = 5;
        int num_to_create = 10;
        Class<?>[] classes = new Class<?>[] { treemap.TreeMap.class };
        SharedSequencePool.init_pool(time_limit, classes);
        while (num_to_create-- > 0) {
            System.out.println(" ***** start a new round: " + num_to_create);
            SharedSequencePool.startNewRound();
            Throwable exception = null;
            try {
                treemap.TreeMap map = Values.randomCreate(treemap.TreeMap.class);
                map.put(-999);
                System.out.println("size of map: " + map.size() + ", map: " + map);
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
                    System.out.println(seq.toCodeString(10, "map", null, null));
                }
            } else {
                System.out.println("fail to create.");
            }
        }
    }
}
