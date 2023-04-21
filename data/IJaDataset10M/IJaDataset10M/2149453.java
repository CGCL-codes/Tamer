package uk.ac.ebi.intact.webapp.search.advancedSearch.powerSearch.business.graphdraw.graph;

import java.awt.geom.GeneralPath;
import java.util.*;

/**
 * Layout a hierarchical graph.
 * <p/>
 * Layout similar to the algorithm implemented by <a href="http://www.graphviz.org/">GraphViz, AT&T laboratories</a>.
 * </p>
 * <pre>
 * Usage:
 * Graph g=....
 * HierarchicalLayout layout = new HierarchicalLayout(g,HierarchicalLayout.PARENT_TOP);
 * layout.layout();
 * ...layout.getWidth()...layout.getHeight()...
 * </pre>
 * <p/>
 * See SimpleGraphDraw.java for an example. </p>
 * <p/>
 * The following documentation assumes orientation is PARENT_TOP, in which case the parent nodes are at the top and a
 * level is a horizontal group of nodes. Normally the magic field values should be left at their default values. </p>
 *
 * @author EGO
 * @version $Id: HierarchicalLayout.java 6452 2006-10-16 16:09:42Z baranda $
 * @since 27.04.2005
 */
public class HierarchicalLayout {

    /**
     * Parent nodes at top of graph layout
     */
    public static final int PARENT_TOP = 0;

    /**
     * Parent nodes at left of graph layout
     */
    public static final int PARENT_LEFT = 1;

    /**
     * Prepare for layout
     *
     * @param graph       Graph containing LayoutNodes and LayoutEdges, will not be modified.
     * @param orientation PARENT_TOP or PARENT_LEFT
     */
    public HierarchicalLayout(Graph graph, int orientation) {
        Map hnodes = new HashMap();
        for (Iterator i = graph.nodes.iterator(); i.hasNext(); ) {
            LayoutNode n = (LayoutNode) i.next();
            HierarchicalNode hnode;
            if (orientation == PARENT_TOP) {
                hnode = new HierarchicalNode(n, n.getHeight(), n.getWidth());
            } else {
                hnode = new HierarchicalNode(n, n.getWidth(), n.getHeight());
            }
            hnodes.put(n, hnode);
            hierarchicalGraph.nodes.add(hnode);
        }
        for (Iterator i = graph.edges.iterator(); i.hasNext(); ) {
            LayoutEdge e = (LayoutEdge) i.next();
            hierarchicalGraph.edges.add(new HierarchicalEdge((HierarchicalNode) hnodes.get(e.getParent()), (HierarchicalNode) hnodes.get(e.getChild()), e));
        }
    }

    /**
     * Compute layout. This method finally calls setLocation on all the nodes and setRoute on all the edges.
     */
    public void layout() {
        for (Iterator i = hierarchicalGraph.nodes.iterator(); i.hasNext(); ) {
            HierarchicalNode n = (HierarchicalNode) i.next();
            findLevel(maxLevelSize, n);
        }
        rationalise();
        for (Iterator i = levels.iterator(); i.hasNext(); ) {
            Level l = (Level) i.next();
            l.calcInitialPositions();
        }
        orderNodesInLevels();
        calcLevelLocations();
        int minStart = Integer.MAX_VALUE;
        for (Iterator i = levels.iterator(); i.hasNext(); ) {
            minStart = Math.min(minStart, ((Level) i.next()).getStart());
        }
        for (Iterator i = levels.iterator(); i.hasNext(); ) {
            ((Level) i.next()).shiftLeft(minStart);
        }
        storeLayout();
    }

    /**
     * After calling layout() call getWidth and getHeight <br/> All nodes will be in this bounding box. <br/>
     * 0&lt;node.x+/-node.getWidth/2&lt;layout.getWidth <br/> 0&lt;node.y+/-node.getHeight/2&lt;layout.getHeight <br/>
     * Noting that x and y are the centres of the nodes. All edge routes will also be in the bounding box.
     *
     * @return width of layout
     */
    public int getWidth() {
        int maxWidth = 0;
        for (Iterator i = levels.iterator(); i.hasNext(); ) {
            maxWidth = Math.max(maxWidth, ((Level) i.next()).getWidth());
        }
        return maxWidth + 1;
    }

    /**
     * See getWidth()
     */
    public int getHeight() {
        Level l = (Level) levels.get(levels.size() - 1);
        return (l.location + l.depth / 2) + 1;
    }

    /**
     * Ratio of maximum edge vertical distance to horizontal distance
     */
    public int edgeLengthHeightRatio = 6;

    /**
     * Number of passes up and down the levels to attempt to optimise node positions
     */
    public final int reorderIterations = 25;

    /**
     * Minimum gap between levels changed from 10 to 3 (afrie)
     */
    public final int minLevelGap = 3;

    /**
     * Levels may be split if they have more than this number of nodes
     */
    public final int maxLevelSize = 100;

    /**
     * Edges running though levels will be allocated this much horizontal space
     */
    public int insertedEdgeWidth = 20;

    /**
     * Horizontal gap between nodes changed from 20 to 0 (afrie)
     */
    public int withinLevelGap = 0;

    private Graph hierarchicalGraph = new Graph();

    private int orientation = PARENT_TOP;

    private ArrayList levels = new ArrayList();

    private Set originalEdges;

    private class HierarchicalNode implements Node {

        LayoutNode underlying;

        /**
         * Level on which node is located
         */
        Level level;

        /**
         * Location within level
         */
        int location;

        /**
         * Size of node expressed wrt level
         */
        int withinLevelSize;

        /**
         * Size of node expressed wrt level
         */
        int betweenLevelSize;

        /**
         * Create nodedata for node
         */
        HierarchicalNode(LayoutNode node, int betweenLevelSize, int withinLevelSize) {
            underlying = node;
            this.betweenLevelSize = betweenLevelSize;
            this.withinLevelSize = withinLevelSize;
        }

        HierarchicalNode(int betweenLevelSize, int withinLevelSize) {
            this.betweenLevelSize = betweenLevelSize;
            this.withinLevelSize = withinLevelSize;
        }
    }

    private class HierarchicalEdge implements Edge {

        HierarchicalNode parent, child;

        LayoutEdge underlying = null;

        List componentEdges = new ArrayList();

        public Node getParent() {
            return parent;
        }

        public Node getChild() {
            return child;
        }

        public HierarchicalEdge(HierarchicalNode parent, HierarchicalNode child) {
            this.parent = parent;
            this.child = child;
        }

        public HierarchicalEdge(HierarchicalNode parent, HierarchicalNode child, LayoutEdge underlying) {
            this.parent = parent;
            this.child = child;
            this.underlying = underlying;
        }
    }

    private class Level {

        int levelNumber;

        int location, depth;

        List nodes = new ArrayList();

        Graph hierarchicalGraph;

        public Level(Graph hierarchicalGraph, int levelNumber) {
            this.hierarchicalGraph = hierarchicalGraph;
            this.levelNumber = levelNumber;
        }

        void reorder(Level above, Level below) {
            for (int j = 0; j < nodes.size(); j++) {
                HierarchicalNode nj = (HierarchicalNode) nodes.get(j);
                double total = 0;
                int connected = 0;
                if (above != null) {
                    for (int i = 0; i < above.nodes.size(); i++) {
                        HierarchicalNode ni = (HierarchicalNode) above.nodes.get(i);
                        if (hierarchicalGraph.connected(ni, nj)) {
                            connected++;
                            total += ni.location;
                        }
                    }
                }
                if (below != null) {
                    for (int i = 0; i < below.nodes.size(); i++) {
                        HierarchicalNode ni = (HierarchicalNode) below.nodes.get(i);
                        if (hierarchicalGraph.connected(nj, ni)) {
                            connected++;
                            total += ni.location;
                        }
                    }
                }
                if (connected == 0) {
                    continue;
                } else {
                    total /= connected;
                }
                nj.location = (int) total;
            }
            while (true) {
                Collections.sort(nodes, nodeLayoutComparator);
                boolean foundOverlap = false;
                for (int i = 1; i < nodes.size(); i++) {
                    HierarchicalNode a = (HierarchicalNode) nodes.get(i - 1);
                    HierarchicalNode b = (HierarchicalNode) nodes.get(i);
                    int overlap = minLevelGap + (a.location + a.withinLevelSize / 2) - (b.location - b.withinLevelSize / 2);
                    if (overlap > 0) {
                        foundOverlap = true;
                        a.location = a.location - overlap / 2 - 1;
                        b.location = b.location + overlap / 2 + 1;
                    }
                }
                if (!foundOverlap) {
                    break;
                }
            }
        }

        void calcInitialPositions() {
            int width = 0;
            for (int i = 0; i < nodes.size(); i++) {
                HierarchicalNode n = (HierarchicalNode) nodes.get(i);
                n.location = width + n.withinLevelSize / 2;
                width += n.withinLevelSize + withinLevelGap;
            }
        }

        void shiftLeft(int delta) {
            for (int i = 0; i < nodes.size(); i++) {
                ((HierarchicalNode) nodes.get(i)).location -= delta;
            }
        }

        void setDepth(int depth) {
            this.depth = depth;
        }

        void setLocation(int location) {
            this.location = location;
        }

        int getWidth() {
            final HierarchicalNode nd = ((HierarchicalNode) nodes.get(nodes.size() - 1));
            return nd.location + nd.withinLevelSize / 2;
        }

        int getStart() {
            final HierarchicalNode nd = ((HierarchicalNode) nodes.get(0));
            return nd.location - nd.withinLevelSize / 2;
        }
    }

    private static final Comparator nodeLayoutComparator = new Comparator() {

        public int compare(Object o1, Object o2) {
            return ((HierarchicalNode) o1).location - ((HierarchicalNode) o2).location;
        }
    };

    private int findLevel(int maxLevelSize, HierarchicalNode node) {
        if (node.level != null) {
            return node.level.levelNumber;
        }
        int maxParentLevel = -1;
        Set parents = hierarchicalGraph.parents(node);
        for (Iterator i = parents.iterator(); i.hasNext(); ) {
            HierarchicalNode parent = (HierarchicalNode) i.next();
            if (parent == null) {
                continue;
            }
            int l = findLevel(maxLevelSize, parent);
            if (l > maxParentLevel) {
                maxParentLevel = l;
            }
        }
        int levelNumber = maxParentLevel + 1;
        while (true) {
            while (levelNumber >= levels.size()) {
                levels.add(new Level(hierarchicalGraph, levels.size()));
            }
            if (((Level) levels.get(levelNumber)).nodes.size() < maxLevelSize) {
                break;
            }
            levelNumber++;
        }
        node.level = (Level) levels.get(levelNumber);
        node.level.nodes.add(node);
        return levelNumber;
    }

    private void rationalise(HierarchicalEdge e, Graph g) {
        int parentLevel = ((HierarchicalNode) e.getParent()).level.levelNumber;
        int childLevel = ((HierarchicalNode) e.getChild()).level.levelNumber;
        if (parentLevel < childLevel - 1) {
            HierarchicalNode a = (HierarchicalNode) e.getParent();
            for (int i = parentLevel + 1; i <= childLevel; i++) {
                HierarchicalNode b;
                if (i == childLevel) {
                    b = (HierarchicalNode) e.getChild();
                } else {
                    b = new HierarchicalNode(-1, insertedEdgeWidth);
                    b.level = (Level) levels.get(i);
                    b.level.nodes.add(b);
                }
                HierarchicalEdge insertedEdge = new HierarchicalEdge(a, b);
                g.edges.add(insertedEdge);
                g.nodes.add(b);
                e.componentEdges.add(insertedEdge);
                a = b;
            }
        } else {
            e.componentEdges.add(e);
            g.edges.add(e);
        }
    }

    private void rationalise() {
        originalEdges = new HashSet(hierarchicalGraph.edges);
        hierarchicalGraph.edges.clear();
        for (Iterator i = originalEdges.iterator(); i.hasNext(); ) {
            HierarchicalEdge e = (HierarchicalEdge) i.next();
            rationalise(e, hierarchicalGraph);
        }
    }

    private void orderNodesInLevels() {
        for (int j = 0; j < reorderIterations; j++) {
            int s = levels.size();
            for (int i = 0; i < s; i++) {
                Level p = (i == 0) ? null : (Level) levels.get(i - 1);
                Level l = (Level) levels.get(i);
                Level n = (i == s - 1) ? null : (Level) levels.get(i + 1);
                l.reorder(p, n);
            }
            for (int i = s - 1; i >= 0; i--) {
                Level p = (i == 0) ? null : (Level) levels.get(i - 1);
                Level l = (Level) levels.get(i);
                Level n = (i == s - 1) ? null : (Level) levels.get(i + 1);
                l.reorder(p, n);
            }
        }
    }

    private void calcLevelLocations() {
        int height = 0;
        Level p = null;
        for (Iterator i = levels.iterator(); i.hasNext(); ) {
            Level l = (Level) i.next();
            int maxLength = 0;
            if (p != null) {
                for (Iterator i2 = l.nodes.iterator(); i2.hasNext(); ) {
                    HierarchicalNode n1 = (HierarchicalNode) i2.next();
                    for (Iterator i3 = p.nodes.iterator(); i3.hasNext(); ) {
                        HierarchicalNode n2 = (HierarchicalNode) i3.next();
                        if (hierarchicalGraph.connected(n1, n2)) {
                            maxLength = Math.max(maxLength, Math.abs(n1.location - n2.location));
                        }
                    }
                }
                height += Math.max(minLevelGap, maxLength / edgeLengthHeightRatio);
            }
            int maxHeight = 0;
            for (Iterator i2 = l.nodes.iterator(); i2.hasNext(); ) {
                maxHeight = Math.max(maxHeight, ((HierarchicalNode) i2.next()).betweenLevelSize);
            }
            l.setDepth(maxHeight);
            height += l.depth / 2;
            l.setLocation(height);
            height += maxHeight;
            p = l;
        }
    }

    private void storeLayout() {
        for (Iterator it = hierarchicalGraph.nodes.iterator(); it.hasNext(); ) {
            HierarchicalNode n = (HierarchicalNode) it.next();
            if (n.underlying == null) {
                continue;
            }
            if (orientation == PARENT_TOP) {
                n.underlying.setLocation(n.location, n.level.location);
            } else {
                n.underlying.setLocation(n.level.location, n.location);
            }
        }
        for (Iterator it = originalEdges.iterator(); it.hasNext(); ) {
            HierarchicalEdge e = (HierarchicalEdge) it.next();
            GeneralPath shape = new GeneralPath();
            for (int i = 0; i < e.componentEdges.size(); i++) {
                HierarchicalEdge edge = (HierarchicalEdge) e.componentEdges.get(i);
                HierarchicalNode parent = (HierarchicalNode) edge.getParent();
                HierarchicalNode child = (HierarchicalNode) edge.getChild();
                int parentLocation = parent.location;
                int childLocation = child.location;
                int levelParent = parent.level.location + parent.level.depth / 2;
                int levelChild = child.level.location - child.level.depth / 2;
                int levelCentre = (levelParent + levelChild) / 2;
                int nodeParent = parent.level.location + parent.betweenLevelSize / 2;
                int nodeChild = child.level.location - child.betweenLevelSize / 2;
                if (orientation == PARENT_TOP) {
                    shape.moveTo(parentLocation, nodeParent);
                    shape.lineTo(parentLocation, levelParent);
                    shape.curveTo(parentLocation, levelCentre, childLocation, levelCentre, childLocation, levelChild);
                    shape.lineTo(childLocation, nodeChild);
                } else {
                    shape.moveTo(nodeParent, parentLocation);
                    shape.lineTo(levelParent, parentLocation);
                    shape.curveTo(levelCentre, parentLocation, levelCentre, childLocation, levelChild, childLocation);
                    shape.lineTo(nodeChild, childLocation);
                }
            }
            e.underlying.setRoute(shape);
        }
    }
}
