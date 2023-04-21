package ro.pub.cs.minerva.graph;

/**
 * 
 * @author Stefan Bucur
 *
 * @param <N>
 */
public interface GraphSearchHandler<N extends GraphNode> {

    public void nodeStart(GraphSearchInfo<N> info);

    public void nodeEnd(GraphSearchInfo<N> info);
}
