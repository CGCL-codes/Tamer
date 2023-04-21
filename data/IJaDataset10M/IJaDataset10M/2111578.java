package ru.spbau.bytecode.projectobserver;

import static org.junit.Assert.assertTrue;
import java.io.File;
import org.junit.Before;
import org.junit.Test;
import ru.spbau.bytecode.graph.CompositeVertex;
import ru.spbau.bytecode.graph.Edge;
import ru.spbau.bytecode.graph.Graph;
import ru.spbau.bytecode.graph.GraphException;

public class ClassesObserverTest {

    private final Graph expectedGraph = new Graph();

    @Before
    public void setUp() {
        CompositeVertex vGraph = new CompositeVertex("Graph");
        CompositeVertex vVertex = new CompositeVertex("Vertex");
        CompositeVertex vCompositeVertex = new CompositeVertex("CompositeVertex");
        CompositeVertex vEdge = new CompositeVertex("Edge");
        CompositeVertex vClassesObserver = new CompositeVertex("ClassesObserver");
        CompositeVertex vPackagesObserver = new CompositeVertex("PackagesObserver");
        CompositeVertex vCompositeObserver = new CompositeVertex("CompositeObserver");
        CompositeVertex vGraphBuilder = new CompositeVertex("GraphBuilder");
        CompositeVertex vProjectObserver = new CompositeVertex("ProjectObserver");
        CompositeVertex vProjectObserver1 = new CompositeVertex("ProjectObserver$1");
        CompositeVertex vProjectObserver2 = new CompositeVertex("ProjectObserver$2");
        CompositeVertex vBytecodeReader = new CompositeVertex("BytecodeReader");
        expectedGraph.addVertex(vGraph);
        expectedGraph.addVertex(vVertex);
        expectedGraph.addVertex(vCompositeVertex);
        expectedGraph.addVertex(vEdge);
        expectedGraph.addVertex(vClassesObserver);
        expectedGraph.addVertex(vPackagesObserver);
        expectedGraph.addVertex(vCompositeObserver);
        expectedGraph.addVertex(vGraphBuilder);
        expectedGraph.addVertex(vProjectObserver);
        expectedGraph.addVertex(vBytecodeReader);
        expectedGraph.addVertex(vProjectObserver1);
        expectedGraph.addVertex(vProjectObserver2);
        Edge eGraphVertex = new Edge(vGraph, vVertex);
        Edge eGraphCompositeVertex = new Edge(vGraph, vCompositeVertex);
        Edge eGraphEdge = new Edge(vGraph, vEdge);
        Edge eCompositeVertexVertex = new Edge(vCompositeVertex, vVertex);
        Edge eEdgeVertex = new Edge(vEdge, vVertex);
        Edge eCompositeObserverClassesObserver = new Edge(vCompositeObserver, vClassesObserver);
        Edge eProjectObserverProjectObserver1 = new Edge(vProjectObserver, vProjectObserver1);
        Edge eProjectObserverProjectObserver2 = new Edge(vProjectObserver, vProjectObserver2);
        Edge eProjectObserver1ProjectObserver = new Edge(vProjectObserver1, vProjectObserver);
        Edge eProjectObserver2ProjectObserver = new Edge(vProjectObserver2, vProjectObserver);
        Edge eClassesObserverProjecteObserver = new Edge(vClassesObserver, vProjectObserver);
        Edge ePackagesObserverProjecteObserver = new Edge(vPackagesObserver, vProjectObserver);
        Edge eCompositeObserverProjecteObserver = new Edge(vCompositeObserver, vProjectObserver);
        Edge eProjectObserverGraphBuilder = new Edge(vProjectObserver, vGraphBuilder);
        expectedGraph.addEdge(eGraphVertex);
        expectedGraph.addEdge(eGraphCompositeVertex);
        expectedGraph.addEdge(eGraphEdge);
        expectedGraph.addEdge(eCompositeVertexVertex);
        expectedGraph.addEdge(eEdgeVertex);
        expectedGraph.addEdge(eCompositeObserverClassesObserver);
        expectedGraph.addEdge(eProjectObserverProjectObserver1);
        expectedGraph.addEdge(eProjectObserverProjectObserver2);
        expectedGraph.addEdge(eProjectObserver1ProjectObserver);
        expectedGraph.addEdge(eProjectObserver2ProjectObserver);
        expectedGraph.addEdge(eClassesObserverProjecteObserver);
        expectedGraph.addEdge(ePackagesObserverProjecteObserver);
        expectedGraph.addEdge(eCompositeObserverProjecteObserver);
        expectedGraph.addEdge(eProjectObserverGraphBuilder);
        Edge eClassesObserverGraph = new Edge(vClassesObserver, vGraph);
        Edge eClassesObserverCompositeVertex = new Edge(vClassesObserver, vCompositeVertex);
        Edge eClassesObserverEdge = new Edge(vClassesObserver, vEdge);
        Edge ePackagesObserverGraph = new Edge(vPackagesObserver, vGraph);
        Edge ePackagesObserverCompositeVertex = new Edge(vPackagesObserver, vCompositeVertex);
        Edge ePackagesObserverEdge = new Edge(vPackagesObserver, vEdge);
        Edge eCompositeObserverGraph = new Edge(vCompositeObserver, vGraph);
        Edge eCompositeObserverCompositeVertex = new Edge(vCompositeObserver, vCompositeVertex);
        Edge eCompositeObserverEdge = new Edge(vCompositeObserver, vEdge);
        Edge eProjectObserverGraph = new Edge(vProjectObserver, vGraph);
        Edge eClassesObserverBytecodeReader = new Edge(vClassesObserver, vBytecodeReader);
        Edge ePackagesObserverBytecodeReader = new Edge(vPackagesObserver, vBytecodeReader);
        Edge eCompositeObserverVertex = new Edge(vCompositeObserver, vVertex);
        expectedGraph.addEdge(eClassesObserverGraph);
        expectedGraph.addEdge(eClassesObserverCompositeVertex);
        expectedGraph.addEdge(eClassesObserverEdge);
        expectedGraph.addEdge(ePackagesObserverGraph);
        expectedGraph.addEdge(ePackagesObserverCompositeVertex);
        expectedGraph.addEdge(ePackagesObserverEdge);
        expectedGraph.addEdge(eCompositeObserverGraph);
        expectedGraph.addEdge(eCompositeObserverCompositeVertex);
        expectedGraph.addEdge(eCompositeObserverEdge);
        expectedGraph.addEdge(eProjectObserverGraph);
        expectedGraph.addEdge(eClassesObserverBytecodeReader);
        expectedGraph.addEdge(ePackagesObserverBytecodeReader);
        expectedGraph.addEdge(eCompositeObserverVertex);
    }

    @Test
    public void testBuildGraph() throws GraphException {
        ClassesObserver observer = new ClassesObserver();
        Graph resultGraph = observer.buildGraph(new File("resources/testData/testForClassesObserver"));
        GraphComparator cmp = new GraphComparator();
        assertTrue(cmp.equals(expectedGraph, resultGraph));
    }
}
