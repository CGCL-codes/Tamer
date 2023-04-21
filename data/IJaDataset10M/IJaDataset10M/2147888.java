package annas.graph.util.traversal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import annas.graph.ArcInterface;
import annas.graph.GraphInterface;

/**
 * Performs a depth first traversal
 * 
 * @author Sam Wilson
 * 
 * @param <N>
 *            Node type
 * @param <A>
 *            Arc type
 */
public class DepthFirst<N, A extends ArcInterface<N>> implements Traversal<N, A> {

    private GraphInterface<N, A> graph;

    private LinkedList<N> Order;

    private int depth;

    private int tmp;

    /**
	 * 
	 */
    public DepthFirst() {
        super();
        this.depth = 0;
        this.tmp = 0;
        this.Order = new LinkedList<N>();
    }

    public DepthFirst(GraphInterface<N, A> g) {
        super();
        this.depth = 0;
        this.tmp = 0;
        this.graph = g;
        this.Order = new LinkedList<N>();
    }

    /**
	 * Performs a Depth first traversal on the graph from the source node
	 * 
	 * @param s
	 *            Source node
	 * @return Iterator of the traversal
	 */
    public Iterator<N> run(N s) {
        this.DF(s);
        return this.Order.iterator();
    }

    /**
	 * Performs a Depth first traversal on the graph from the source node to the
	 * destination node
	 * 
	 * @param s
	 *            Source node
	 * @param tar
	 *            destination node
	 * 
	 * @return Iterator of the traversal
	 */
    public Iterator<N> run(N s, N tar) {
        this.run(s);
        this.DF(s, tar);
        return this.Order.iterator();
    }

    private void DF(N n) {
        if (!this.Order.contains(n)) {
            this.Order.add(n);
            ArrayList<A> arcs = this.graph.getArc(n);
            if (arcs.size() == 0 && this.tmp > this.depth) {
                this.depth = this.tmp;
            }
            for (int i = 0; i < arcs.size(); i++) {
                this.tmp++;
                this.DF(arcs.get(i).getHead());
            }
            this.tmp--;
        }
    }

    private void DF(N n, N Target) {
        LinkedList<N> h = new LinkedList<N>();
        for (int i = 0; i < this.Order.size(); i++) {
            h.add(this.Order.get(i));
            if (h.get(h.size() - 1).equals(Target)) {
                this.Order = h;
                return;
            }
        }
    }

    public GraphInterface<N, A> getGraph() {
        return graph;
    }

    public void setGraph(GraphInterface<N, A> g) {
        this.graph = g;
    }

    public int getDepth() {
        return depth;
    }
}
