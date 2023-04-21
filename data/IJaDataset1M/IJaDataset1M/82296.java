package net.sourceforge.combean.samples.simplegraphs;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import net.sourceforge.combean.util.Pair;

/**
 * @author schickin
 *
 */
public class CompositeNumberGraph extends NumberGraph {

    private Collection<Pair> childGraphs;

    /**
	 * 
	 */
    public CompositeNumberGraph() {
        super();
        this.childGraphs = new LinkedList<Pair>();
    }

    /**
	 * @see net.sourceforge.combean.samples.simplegraphs.NumberGraph#calcNextNode(int, int)
	 */
    protected int calcNextNode(int sourceNodeNum, int minNodeNum) {
        int nextNodeCand = NumberGraph.NOSUCHNODE;
        Iterator itChild = this.childGraphs.iterator();
        while (itChild.hasNext()) {
            Pair offsetAndGraph = (Pair) itChild.next();
            Integer offset = (Integer) offsetAndGraph.getFirst();
            NumberGraph g = (NumberGraph) offsetAndGraph.getSecond();
            int startNodeInG = sourceNodeNum - offset.intValue();
            if (0 <= startNodeInG && startNodeInG < g.getNumNodes()) {
                int currNodeInG = minNodeNum - offset.intValue();
                int nextNodeOfG = g.calcNextNode(startNodeInG, currNodeInG);
                if (nextNodeOfG != NumberGraph.NOSUCHNODE) {
                    nextNodeOfG += offset.longValue();
                    if (nextNodeCand == NumberGraph.NOSUCHNODE) {
                        nextNodeCand = nextNodeOfG;
                    } else {
                        nextNodeCand = Math.min(nextNodeCand, nextNodeOfG);
                    }
                }
            }
        }
        return nextNodeCand;
    }

    /**
	 * @see net.sourceforge.combean.interfaces.graph.prop.GlobalNodesGraphProp#getNumNodes()
	 */
    public int getNumNodes() {
        int maxNodeOffset = 0;
        Iterator itChild = this.childGraphs.iterator();
        while (itChild.hasNext()) {
            Pair offsetAndGraph = (Pair) itChild.next();
            Integer offset = (Integer) offsetAndGraph.getFirst();
            NumberGraph g = (NumberGraph) offsetAndGraph.getSecond();
            maxNodeOffset = Math.max(maxNodeOffset, offset.intValue() + g.getNumNodes());
        }
        return maxNodeOffset;
    }

    public void add(NumberGraph g) {
        add(g, 0);
    }

    public void add(NumberGraph g, int offset) {
        this.childGraphs.add(new Pair(new Integer(offset), g));
    }

    public int getNumGraphs() {
        return this.childGraphs.size();
    }
}
