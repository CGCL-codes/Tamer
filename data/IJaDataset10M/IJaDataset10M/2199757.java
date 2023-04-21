package prajna.viz.arrange;

import java.awt.Dimension;
import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import prajna.util.Graph;

/**
 * <P>
 * Arrangement class which will arrange all nodes in a directed fashion,
 * similar to a tree, but with multiple possible roots. This arrangement is
 * designed for work-flow or process diagrams. It relies upon having directed
 * edges within the arrangement.
 * </p>
 * <P>
 * The arrangement can be set to either arrange left-to-right (the default) or
 * top-to-bottom. Any nodes which have no directed edges are placed at the same
 * level as the lowest level edge they connect to; if none of their neighbors
 * are in the directed portion of the graph, they are set to level zero (the
 * top/left side).
 * </p>
 * 
 * @author <a href="http://www.ganae.com/edswing">Edward Swing</a>
 * @param <N> The node type for the graph
 * @param <E> The edge type for the graph
 */
public class DirectedArrangement<N, E> extends AbstractArrangement<N> implements GraphArrangement<N, E> {

    private boolean horizontal;

    private boolean useShortest = true;

    /**
     * Arranges the specified Graph
     * 
     * @param graph the graph to be arranged
     * @throws ArrangementException Exception thrown if the graph cannot be
     *             arranged
     */
    public void arrange(Graph<N, E> graph) throws ArrangementException {
        invalidate();
        if (!graph.isDirected()) {
            throw new ArrangementException("DirectedArrangement does not work on undirected graphs");
        }
        HashSet<Graph.Segment<N, E>> directed = new HashSet<Graph.Segment<N, E>>();
        for (Graph.Segment<N, E> seg : graph.getSegmentSet()) {
            N orig = seg.getOrigin();
            N dest = seg.getDestination();
            if (graph.getEdge(dest, orig) == null) {
                directed.add(seg);
            }
        }
        if (directed.size() == 0) {
            throw new ArrangementException("No directed edges!");
        }
        HashMap<N, Integer> levels = new HashMap<N, Integer>();
        int maxLevel = 0;
        for (Graph.Segment<N, E> seg : directed) {
            levels.put(seg.getOrigin(), 0);
        }
        if (useShortest) {
            for (Graph.Segment<N, E> seg : directed) {
                levels.put(seg.getDestination(), 999);
            }
            for (int i = 0; i < directed.size(); i++) {
                for (Graph.Segment<N, E> seg : directed) {
                    Integer origIndex = levels.get(seg.getOrigin());
                    Integer destIndex = levels.get(seg.getDestination());
                    if ((origIndex == i) && (destIndex > (i + 1))) {
                        levels.put(seg.getDestination(), i + 1);
                        maxLevel = i + 1;
                    }
                }
            }
        } else {
            for (Graph.Segment<N, E> seg : directed) {
                levels.remove(seg.getDestination());
            }
            if (levels.size() == 0) {
                throw new ArrangementException("Graph Loops make arrangement " + "indeterminant. No clear starting node");
            }
            for (int i = 0; i < directed.size() && i <= maxLevel; i++) {
                for (Graph.Segment<N, E> seg : directed) {
                    Integer origIndex = levels.get(seg.getOrigin());
                    if (origIndex != null && origIndex == i) {
                        levels.put(seg.getDestination(), i + 1);
                        maxLevel = i + 1;
                    }
                }
            }
        }
        int[] levelCount = new int[maxLevel + 1];
        Set<Graph.Segment<N, E>> segSet = graph.getSegmentSet();
        segSet.removeAll(directed);
        int lastUpdate = -1;
        int updateCnt = 0;
        while (updateCnt != lastUpdate) {
            lastUpdate = updateCnt;
            updateCnt = 0;
            for (Graph.Segment<N, E> seg : segSet) {
                N orig = seg.getOrigin();
                N dest = seg.getDestination();
                Integer origIndex = levels.get(seg.getOrigin());
                Integer destIndex = levels.get(seg.getDestination());
                if (origIndex == null && destIndex != null) {
                    levels.put(orig, destIndex);
                    updateCnt++;
                } else if (origIndex != null && destIndex == null) {
                    levels.put(dest, origIndex);
                    updateCnt++;
                }
            }
        }
        for (N node : graph.getNodeSet()) {
            Integer levelInx = levels.get(node);
            if (levelInx == null) {
                levels.put(node, 0);
                levelCount[0]++;
            } else {
                levelCount[levelInx]++;
            }
        }
        int[] spans = new int[maxLevel + 1];
        int[] coords = new int[maxLevel + 1];
        Dimension dim = getSize();
        int margin = getMargin();
        int gap = horizontal ? (dim.width - (margin * 2)) / (maxLevel + 1) : (dim.height - (margin * 2)) / (maxLevel + 1);
        for (int i = 0; i < (maxLevel + 1); i++) {
            spans[i] = horizontal ? (dim.height - (margin * 2)) / levelCount[i] : (dim.width - (margin * 2)) / levelCount[i];
            coords[i] = margin + (spans[i] / 2);
        }
        for (N node : graph.getNodeSet()) {
            setNodeLocation(node, gap, levels, coords, spans, graph);
        }
    }

    /**
     * Returns the value of the shortFlag, which is used to determine how
     * compact to make the arrangement
     * 
     * @return whether the arrangement uses a shortened arrangement.
     */
    public boolean getShortFlag() {
        return useShortest;
    }

    /**
     * Returns whether the arrangement is oriented horizontally or vertically
     * 
     * @return true if the arrangement is oriented horizontally, false if it is
     *         oriented vertically
     */
    public boolean isHorizontal() {
        return horizontal;
    }

    /**
     * Sets the direction for the arrangement. If horizontal is true, the nodes
     * are arranged left to right. If false, the nodes are arranged top to
     * bottom.
     * 
     * @param horizontalFlag whether to arrange horizontally or vertically.
     */
    public void setHorizontal(boolean horizontalFlag) {
        horizontal = horizontalFlag;
    }

    /**
     * Set node location, recursing to minimize crossings (hopefully)
     * 
     * @param node the node to set
     * @param gap the spacing between levels
     * @param levels a map which maps each node to a level on the graph
     * @param coords current coordinates of each level
     * @param spans increment between nodes of each level
     */
    private void setNodeLocation(N node, int gap, HashMap<N, Integer> levels, int[] coords, int[] spans, Graph<N, E> graph) {
        if (getNodeLocation(node) == null) {
            Integer level = levels.get(node);
            if (horizontal) {
                setNodeLocation(node, new Point((gap / 2) + (level * gap), coords[level]));
            } else {
                setNodeLocation(node, new Point(coords[level], (gap / 2) + (level * gap)));
            }
            coords[level] += spans[level];
            for (Graph.Segment<N, E> seg : graph.getSegmentSet()) {
                N orig = seg.getOrigin();
                N dest = seg.getDestination();
                if (node.equals(orig) && getNodeLocation(dest) == null) {
                    setNodeLocation(dest, gap, levels, coords, spans, graph);
                } else if (node.equals(dest) && getNodeLocation(orig) == null) {
                    setNodeLocation(dest, gap, levels, coords, spans, graph);
                }
            }
        }
    }

    /**
     * Sets the ShortFlag. The ShortFlag determines whether to use the shortest
     * possible path to determine the layer of a particular node, or the
     * longest. if shortFlag is true, the arrangement will be more compact; if
     * false, the arrangement will be more stretched out. The default value is
     * true.
     * 
     * @param shortFlag whether to use a shortened arrangement
     */
    public void setShortFlag(boolean shortFlag) {
        useShortest = shortFlag;
    }
}
