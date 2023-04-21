package ch.unibe.jexample.internal.tests;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import ch.unibe.jexample.Given;
import ch.unibe.jexample.JExample;
import ch.unibe.jexample.internal.ExampleGraph;
import ch.unibe.jexample.internal.util.JExampleError;
import ch.unibe.jexample.internal.util.JExampleError.Kind;

public class ExampleGraphTest {

    private ExampleGraph graph;

    @Before
    public void setUp() throws Exception {
        graph = new ExampleGraph();
    }

    @Test
    public void testAddOneClass() throws JExampleError {
        graph.add(OneClass.class);
        assertEquals(4, graph.getExamples().size());
    }

    @Test
    public void testAddMethodsOfOneClass() throws JExampleError {
        graph.add(OneClass.class);
        assertEquals(4, graph.getExamples().size());
    }

    @Test
    public void testAddDependenciesOfOneClass() throws JExampleError, SecurityException, NoSuchMethodException {
        graph.add(OneClass.class);
        assertEquals(0, graph.findExample(OneClass.class, "testMethod").producers().size());
        assertEquals(1, graph.findExample(OneClass.class, "anotherTestMethod").producers().size());
        assertEquals(1, graph.findExample(OneClass.class, "depOnOtherTest").producers().size());
        assertEquals(0, graph.findExample(DependsParserTest.B.class, "otherTest").producers().size());
    }

    @Test
    public void detectCycles() throws JExampleError {
        Class<?>[] classes = { Cyclic.class };
        ExampleGraph g = new ExampleGraph();
        Result r = g.runJExample(classes);
        assertEquals(false, r.wasSuccessful());
        assertEquals(3, r.getRunCount());
        assertEquals(2, r.getFailureCount());
        JExampleError err;
        err = (JExampleError) r.getFailures().get(0).getException();
        assertEquals(Kind.RECURSIVE_DEPENDENCIES, err.getKind());
        err = (JExampleError) r.getFailures().get(1).getException();
        assertEquals(Kind.RECURSIVE_DEPENDENCIES, err.getKind());
    }

    @RunWith(JExample.class)
    private static class OneClass {

        public OneClass() {
        }

        @Test
        public void testMethod() {
        }

        @Given("testMethod")
        public void anotherTestMethod() {
        }

        @Given("DependsParserTest$B.otherTest")
        public void depOnOtherTest() {
        }
    }

    @RunWith(JExample.class)
    private static class Cyclic {

        @Test
        public void provider() {
        }

        @Given("provider;aaa")
        public void bbb() {
        }

        @Given("bbb")
        public void aaa() {
        }
    }

    @SuppressWarnings("unused")
    private static class CyclicOverClasses {

        public CyclicOverClasses() {
        }

        @Test
        public void testMethod() {
        }

        @Given("testMethod")
        public void anotherTestMethod() {
        }

        @Given("B.otherTestCyclic")
        public void depOnOtherTest() {
        }
    }
}
