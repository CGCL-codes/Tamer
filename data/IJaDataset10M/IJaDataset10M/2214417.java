package visualgraph.gui;

import visualgraph.graph.GraphModel;

/**
 * This is an interface of a builder class that returns DIFFERENT instances
 * of VGComponents for a node or an edge for different graph elements.
 * <BR><BR>
 * The following example shows a simple implementation:
 * <pre>
 * 	public class DefaultVGComponentBuilder implements VGComponentBuilder {
 * 
 * 		public VGNodeComponent buildVGNodeComponent(Graph graph, Object node) {
 *	  		return new SimpleVGNode(SimpleVGNode.ROUNDED_RECTANGLE);
 *		}
 *
 *		public VGEdgeComponent buildVGEdgeComponent(Graph graph, Object edge) {
 *			return new SimpleVGEdge();
 *		}
 * 	}
 * </pre>
 * 
 * @author Micha
 *
 */
public interface VGComponentBuilder {

    /**
	 * Must return DIFFERENT instances of VGNodeComponent for different nodes.
	 * An easy implementation returns a new instance for every call. 
	 * 
	 * @param graph The graph the node belongs to
	 * @param node A specific node
	 * @return Returns a instance of VGComponentNode for the given node   
	 */
    public VGNodeComponent buildVGNodeComponent(GraphModel graph, Object node);

    /**
	 * Must return DIFFERENT instances of VGEdgeComponent for different edges.
	 * An easy implementation returns a new instance for every call. 
	 * 
	 * @param graph The graph the edge belongs to
	 * @param edge A specific edge
	 * @return Returns a instance of VGComponentEdge for the given edge   
	 */
    public VGEdgeComponent buildVGEdgeComponent(GraphModel graph, Object edge);
}
