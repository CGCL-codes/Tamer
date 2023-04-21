package royere.cwi.layout;

import royere.cwi.structure.Graph;
import royere.cwi.structure.Node;
import royere.cwi.layout.Coordinates;
import royere.cwi.structure.OrderedSet;
import royere.cwi.structure.Traversal;
import royere.cwi.structure.MultiTraversal;
import java.util.Iterator;

/**
 * This class produces 2D layout for an acyclic digraph
 * and tries minimizing the number of crossings using the barycenter
 * heuristic. This is acheived by inserting dummy nodes along edges crossing multiple
 * levels in the dag, in order to work on consecutive layers of nodes <i>only</i>.
 * Adapted from Graph Drawing book by Battista et al., Chap 9.2.3.
 *
 * @author Guy Melan�on, October 1999
 * @see Layout
 */
public class Barycenter extends Sugiyama {

    public static final String Name = "Barycentric";

    private int totalNbOfDummies;

    /** 
   * Constructor  
   */
    public Barycenter() {
        super();
        ojbConcreteClass = "royere.cwi.layout.Barycenter";
    }

    public Integer nbDummies() {
        return new Integer(totalNbOfDummies);
    }

    /**
  * Must define setDefaultNodeSize() method, called by super().
  */
    protected double setDefaultNodeSize() {
        return 1.0;
    }

    public String getName() {
        return Name;
    }

    /**
   * This method implements the barycenter method to minimize
   * crossings when laying out an acyclic digraph. See the book
   * Graph Drawing by Battista et al., Prentice-Hall, 1999, chapter 9.
   */
    public void computePositions(Graph dag) {
        totalNbOfDummies = dag.getEntryNodesSet().size();
        OrderedSet topLayer = new OrderedSet();
        Iterator nodeIterator = dag.getEntryNodes();
        double currCoord = 0.0;
        double min = 0.0;
        double max = 0.0;
        while (nodeIterator.hasNext()) {
            Node node = (Node) nodeIterator.next();
            topLayer.add(new FakeNode(0, node, currCoord));
            currCoord += defaultNodeSize;
            max = currCoord;
        }
        OrderedSet bottomLayer = nextLayer(dag, topLayer);
        totalNbOfDummies += bottomLayer.size();
        while (bottomLayer.size() > 0) {
            Iterator bottomIterator = bottomLayer.iterator();
            while (bottomIterator.hasNext()) {
                FakeNode fakeNode = (FakeNode) bottomIterator.next();
                fakeNode.Xcoord = barycenter(fakeNode, fakeNode.predecessors);
            }
            adjustCoord(bottomLayer, min, max);
            Iterator topIterator = topLayer.iterator();
            while (topIterator.hasNext()) {
                FakeNode fakeNode = (FakeNode) topIterator.next();
                fakeNode.Xcoord = barycenter(fakeNode, fakeNode.successors);
            }
            adjustCoord(topLayer, min, max);
            assignCoordinates(dag, topLayer);
            topLayer = bottomLayer;
            bottomLayer = nextLayer(dag, topLayer);
            totalNbOfDummies += bottomLayer.size();
        }
        assignCoordinates(dag, topLayer);
        squareBox(dag);
    }

    /**
   * Computes the barycentric coordinates of nodes.
   */
    protected double barycenter(FakeNode node, OrderedSet neighbors) {
        if (neighbors.size() == 0) {
            return node.Xcoord;
        } else {
            double barycenter = 0.0;
            Iterator predIterator = neighbors.iterator();
            while (predIterator.hasNext()) {
                FakeNode predecessor = (FakeNode) predIterator.next();
                barycenter += predecessor.Xcoord;
            }
            return barycenter / neighbors.size();
        }
    }

    /**
   * Utility method: swaps two consecutive elements in a layer as well
   * as the associated coordinates. The variables min and max descibe the interval
   * of values for the nodes on the above layer, over which the current layer should
   * be centered.
   */
    private void adjustCoord(OrderedSet layer, double min, double max) {
        Iterator rowIter = layer.iterator();
        while (rowIter.hasNext()) {
            FakeNode rowFake = (FakeNode) rowIter.next();
            Iterator columnIter = layer.iterator();
            while (columnIter.hasNext()) {
                FakeNode columnFake = (FakeNode) columnIter.next();
                if ((rowFake != columnFake) && (rowFake.Xcoord == columnFake.Xcoord)) {
                    rowFake.Xcoord -= 0.5 * getDefaultNodeSize();
                    columnFake.Xcoord += 0.5 * getDefaultNodeSize();
                }
            }
        }
    }
}
