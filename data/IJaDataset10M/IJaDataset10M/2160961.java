package de.htwg.flowchartgenerator.editor;

/**
 * The factory returning the actual graph builder.
 * 
 * @author Aldi Alimucaj
 * 
 */
public class GraphBuilderFactory {

    private static IGraphBuilder graphBuilder = new GraphBuilder();

    private GraphBuilderFactory() {
    }

    /**
	 * The instance to the Graph builder algorithm
	 * 
	 * @return graphBuilder Algorithm to build the graph
	 */
    public static IGraphBuilder getInstance() {
        return graphBuilder;
    }
}
