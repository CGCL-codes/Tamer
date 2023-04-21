package prajna.viz.arrange;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Iterator;
import java.util.Random;
import prajna.util.Graph;
import prajna.util.Tree;

/**
 * Arrangement class which will arrange all nodes in random locations within
 * the working area. Each invocation of one of the arrange() methods will
 * re-randomize the node locations
 * 
 * @author <a href="http://www.ganae.com/edswing">Edward Swing</a>
 * @param <N> The node class
 * @param <E> The edge class
 */
public class RandomArrangement<N, E> extends AbstractArrangement<N> implements GraphArrangement<N, E>, TreeArrangement<N> {

    private Random random = new Random();

    /**
     * Arranges the specified Graph
     * 
     * @param graph the graph to be arranged
     */
    public void arrange(Graph<N, E> graph) {
        Dimension dim = getSize();
        int margin = getMargin();
        int xSpan = dim.width - (margin * 2);
        int ySpan = dim.height - (margin * 2);
        for (N node : graph.getNodeSet()) {
            Point loc = new Point(random.nextInt(xSpan) + margin, random.nextInt(ySpan) + margin);
            setNodeLocation(node, loc);
        }
    }

    /**
     * Arrange the specified tree
     * 
     * @param tree the tree to arrange
     */
    public void arrange(Tree<N> tree) {
        Dimension dim = getSize();
        int margin = getMargin();
        int xSpan = dim.width - (margin * 2);
        int ySpan = dim.height - (margin * 2);
        for (Iterator<N> iter = tree.depthFirstIterator(); iter.hasNext(); ) {
            N node = iter.next();
            Point loc = new Point(random.nextInt(xSpan) + margin, random.nextInt(ySpan) + margin);
            setNodeLocation(node, loc);
        }
    }

    /**
     * Arranges only those nodes which have not already had a location
     * determined.
     * 
     * @param graph the graph to arrange
     */
    public void updateArrangement(Graph<N, E> graph) {
        Dimension dim = getSize();
        int margin = getMargin();
        int xSpan = dim.width - (margin * 2);
        int ySpan = dim.height - (margin * 2);
        for (N node : graph.getNodeSet()) {
            Point loc = getNodeLocation(node);
            if (loc == null) {
                new Point(random.nextInt(xSpan) + margin, random.nextInt(ySpan) + margin);
                setNodeLocation(node, loc);
            }
        }
    }

    /**
     * Arranges only those nodes which have not already had a location
     * determined.
     * 
     * @param tree the tree to arrange
     */
    public void updateArrangement(Tree<N> tree) {
        Dimension dim = getSize();
        int margin = getMargin();
        int xSpan = dim.width - (margin * 2);
        int ySpan = dim.height - (margin * 2);
        for (Iterator<N> iter = tree.depthFirstIterator(); iter.hasNext(); ) {
            N node = iter.next();
            Point loc = getNodeLocation(node);
            if (loc == null) {
                loc = new Point(random.nextInt(xSpan) + margin, random.nextInt(ySpan) + margin);
                setNodeLocation(node, loc);
            }
        }
    }
}
