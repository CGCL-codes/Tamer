package de.fzi.mapaco.lattice;

import org.junit.Test;
import de.fzi.mapaco.lattice.Vertex;
import de.fzi.mapaco.lattice.VertexFactory;

/**
 * @author bock
 *
 */
public class StartVertexTest {

    /**
     * Test method for {@link de.fzi.mapaco.lattice.StartVertex#addIncomingEdge(de.fzi.mapaco.lattice.Vertex)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public final void testAddIncomingEdge() {
        Vertex startVertex = VertexFactory.getInstance().getStartVertex();
        Vertex v = VertexFactory.getInstance().getVertex(1, 2);
        startVertex.addIncomingEdge(v);
    }
}
