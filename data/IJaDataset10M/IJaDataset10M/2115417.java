package playground.johannes.sna.graph;

/**
 * An extension to AbstractSparseGraphBuilder to build SparseGraphs.
 * 
 * @author illenberger
 * 
 */
public class SparseGraphBuilder extends AbstractSparseGraphBuilder<SparseGraph, SparseVertex, SparseEdge> {

    /**
	 * Creates a new SparseGraphBuilder.
	 */
    public SparseGraphBuilder() {
        super(new SparseGraphFactory());
    }
}
