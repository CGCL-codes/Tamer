package gate.fsm;

import gate.*;
import gate.jape.*;
import gate.util.*;
import java.util.*;
import java.io.*;
import junit.framework.*;
import EDU.auburn.VGJ.graph.*;
import EDU.auburn.VGJ.gui.*;
import EDU.auburn.VGJ.examplealg.ExampleAlg2;
import EDU.auburn.VGJ.algorithm.tree.TreeAlgorithm;
import EDU.auburn.VGJ.algorithm.cgd.CGDAlgorithm;
import EDU.auburn.VGJ.algorithm.shawn.Spring;
import EDU.auburn.VGJ.algorithm.cartegw.BiconnectGraph;

/** JUnit style test code for the gate.fsm package.
  * Unfortunatley it doesn't actually do any testing because of the difficulty
  * to test this kind of stuff.
  * However the main() method runs a visual test that will print the graphs
  * (deterministic and nod-~) resulted form parsing a Jape file.
  * The real functionality of this package is tested in the jape package that
  * make extensive use of the services provided by his package.
  */
public class TestFSM extends TestCase {

    /** Debug flag */
    private static final boolean DEBUG = false;

    /** Construction */
    public TestFSM(String name) {
        super(name);
    }

    public void setUp() throws JapeException, IOException, GateException {
        Gate.init();
        batch = new Batch(TestFSM.class.getResource(Files.getResourcePath() + "/jape/combined/brian-soc-loc1.jape"), "UTF-8");
        transducer = (MultiPhaseTransducer) batch.getTransducer();
        transducer.finish();
    }

    public void tearDown() {
    }

    /** Does some kind of a test that verifies whether parsing the jape file
    * results in a graph. It doesn't check the structure graph.
    */
    public void testOne() {
        Enumeration phases = transducer.getPhases().elements();
        while (phases.hasMoreElements()) {
            FSM aFSM = new FSM((SinglePhaseTransducer) phases.nextElement());
            String gml = aFSM.getGML();
            assert (gml.startsWith("graph["));
        }
    }

    /** Will try to parse a .jape file and display the graphs resulted. */
    public void graphTest() throws java.io.IOException, EDU.auburn.VGJ.graph.ParseError {
        Enumeration phases = transducer.getPhases().elements();
        while (phases.hasMoreElements()) {
            SinglePhaseTransducer phase = (SinglePhaseTransducer) phases.nextElement();
            FSM aFSM = new FSM(phase);
            showGraph("Non-deterministic (" + phase.getName() + ")", aFSM.getGML());
            aFSM.eliminateVoidTransitions();
            showGraph("Deterministic (" + phase.getName() + ")", aFSM.getGML());
        }
    }

    /** Opens anew window containing the visual representation of a FSM and
    * having a given title
    */
    public static void showGraph(String title, String gml) throws java.io.IOException, EDU.auburn.VGJ.graph.ParseError {
        GMLlexer gl = new GMLlexer(new ByteArrayInputStream(gml.getBytes()));
        GMLobject go = new GMLobject(gl, null);
        Graph graph = new Graph(go.getGMLSubObject("graph", GMLobject.GMLlist, false));
        GraphWindow graph_editing_window = new GraphWindow(graph);
        TreeAlgorithm talg = new TreeAlgorithm('d');
        graph_editing_window.addAlgorithm(talg, "Tree Down");
        talg = new TreeAlgorithm('u');
        graph_editing_window.addAlgorithm(talg, "Tree Up");
        talg = new TreeAlgorithm('l');
        graph_editing_window.addAlgorithm(talg, "Tree Left");
        talg = new TreeAlgorithm('r');
        graph_editing_window.addAlgorithm(talg, "Tree Right");
        graph_editing_window.setTitle(title);
        graph_editing_window.pack();
        graph_editing_window.show();
        graph_editing_window.applyAlgorithm("Tree Right");
    }

    /** runs the graphical test*/
    public static void main(String[] args) {
        try {
            TestFSM testFSM = new TestFSM("TestFSM");
            testFSM.setUp();
            testFSM.graphTest();
            testFSM.tearDown();
        } catch (Exception e) {
            e.printStackTrace(Err.getPrintWriter());
        }
    }

    /** Test suite routine for the test runner */
    public static Test suite() {
        return new TestSuite(TestFSM.class);
    }

    private Batch batch;

    private MultiPhaseTransducer transducer;
}
