package sketch.ounit.fuzz;

import java.util.LinkedList;
import java.util.List;
import randoop.ComponentManager;
import randoop.RConstructor;
import randoop.StatementKind;
import sketch.ounit.Values;
import sketch.specs.RandomSequenceCreator;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AssembledSequenceObjectCreationWithArgsTest extends TestCase {

    public static Test suite() {
        return new TestSuite(AssembledSequenceObjectCreationWithArgsTest.class);
    }

    public void testAssembledSequence() {
        int time_limit = 5;
        int num_to_create = 10;
        SequencePool pool = new SequencePool(time_limit, treemap.TreeMap.class);
        ComponentManager components = pool.component_manager;
        RandomSequenceCreator creator = pool.getRandomCreator();
        List<StatementKind> model = creator.getModelInRandoop();
        Class<treemap.TreeMap> clazz = treemap.TreeMap.class;
        List<RConstructor> constructors = this.getRConstructors(clazz, model);
        assertEquals(1, constructors.size());
        while (num_to_create-- > 0) {
            AssembledSequenceObjectCreationWithArgs<treemap.TreeMap> obj_creation = new AssembledSequenceObjectCreationWithArgs<treemap.TreeMap>(clazz, components, constructors, 999, 22);
            treemap.TreeMap map = obj_creation.assemble_and_execute_sequence();
            System.out.println("TreeMap: " + map);
            System.out.println("code: ");
            System.out.println(obj_creation.toCodeString());
        }
    }

    public void testArtificiallyMadeExample() {
        int time_limit = 1;
        int num_to_create = 10;
        Class<PseduoProgram> clz = PseduoProgram.class;
        SequencePool pool = new SequencePool(time_limit, clz);
        ComponentManager components = pool.component_manager;
        RandomSequenceCreator creator = pool.getRandomCreator();
        List<StatementKind> model = creator.getModelInRandoop();
        List<RConstructor> constructors = this.getRConstructors(clz, model);
        assertEquals(4, constructors.size());
        while (num_to_create-- > 0) {
            AssembledSequenceObjectCreationWithArgs<PseduoProgram> obj_creation = new AssembledSequenceObjectCreationWithArgs<PseduoProgram>(clz, components, constructors, 999, "hello-world");
            obj_creation.setOutputOffset(0, new String[] { "intVar", "stringVar" }, new String[] { "999", "\"hello-world\"" });
            PseduoProgram p = obj_creation.assemble_and_execute_sequence();
            if (obj_creation.isValidSequence()) {
                System.out.println("PseduoProgram: " + p);
                System.out.println("has exception: " + obj_creation.getException());
                System.out.println("code: ");
                System.out.println(obj_creation.toCodeString());
            } else {
                System.out.println("Fail to create...");
            }
            System.out.println();
        }
    }

    public void testAssembledSequenceSimualate() {
        int time_limit = 1;
        int num_to_create = 10;
        Class<?>[] classes = new Class<?>[] { PseduoProgram.class };
        SharedSequencePool.init_pool(time_limit, classes);
        while (num_to_create-- > 0) {
            System.out.println(" \n\n\n***** start a new round: " + num_to_create);
            SharedSequencePool.startNewRound();
            Throwable exception = null;
            try {
                PseduoProgram p1 = Values.randomCreate(PseduoProgram.class, 999, "hello-world");
                PseduoProgram p2 = Values.randomCreate(PseduoProgram.class, 999, "hello-world");
                System.out.println(p1 + ", " + p2);
            } catch (Exception e) {
                exception = e;
            }
            if (exception == null) {
                if (!SharedSequencePool.allValidSequence()) {
                    System.out.println("several created sequences are not valid.");
                } else {
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
                        System.out.println(seq.toCodeString(11, "p", new String[] { "varint", "varstr" }, new String[] { "999", "\"hello-world\"" }));
                    }
                }
            } else {
                System.out.println("fail to create.");
            }
        }
    }

    private List<RConstructor> getRConstructors(Class<?> clz, List<StatementKind> model) {
        List<RConstructor> constructors = new LinkedList<RConstructor>();
        for (StatementKind stmt : model) {
            if (stmt instanceof RConstructor) {
                RConstructor rconst = (RConstructor) stmt;
                if (rconst.getConstructor().getDeclaringClass().equals(clz)) {
                    constructors.add(rconst);
                }
            }
        }
        return constructors;
    }
}
