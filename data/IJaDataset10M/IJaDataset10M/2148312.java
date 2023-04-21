package net.sourceforge.combean.test.adapters.jgraph;

import net.sourceforge.combean.adapters.jgraph.JGraphModelAsGraph;
import net.sourceforge.combean.adapters.jgraph.JGraphUtils;
import net.sourceforge.combean.interfaces.graph.Graph;
import net.sourceforge.combean.test.graph.generic.AbstractTestDirectedPath;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphCell;
import org.jgraph.graph.GraphLayoutCache;

/**
 * @author schickin
 *
 */
public class TestJGraphDirectedPath extends AbstractTestDirectedPath {

    private static final int PATHLEN = 4;

    private DefaultGraphModel jgModel = null;

    private GraphLayoutCache jgLayout = null;

    private JGraph jg = null;

    private JGraphModelAsGraph g = null;

    private GraphCell startNode = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(TestJGraphDirectedPath.class);
    }

    protected void setUp() throws Exception {
        this.jgModel = new DefaultGraphModel();
        this.jgLayout = new GraphLayoutCache(this.jgModel, new DefaultCellViewFactory());
        this.jg = new JGraph(this.jgModel, this.jgLayout);
        assert this.jg != null;
        DefaultGraphCell[] cells = JGraphUtils.createGraphCells(PATHLEN);
        this.jgLayout.insert(cells);
        GraphCell[] edges = new GraphCell[PATHLEN - 1];
        for (int pos = 0; pos < cells.length - 1; pos++) {
            edges[pos] = JGraphUtils.createEdge(cells, pos, pos + 1);
        }
        this.jgLayout.insert(edges);
        this.g = new JGraphModelAsGraph(this.jgModel);
        this.startNode = cells[0];
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Constructor for TestJGraphDirectedPath.
     * @param name
     */
    public TestJGraphDirectedPath(String name) throws Exception {
        super(name);
    }

    protected GraphCell getFirstNode() {
        return this.startNode;
    }

    protected Graph getGraph() {
        return this.g;
    }

    public final void testGraphWithStartNodeProperlyLoaded() {
        assertNotNull("start node must be identified by attribute", this.startNode);
    }

    public final void testTraverseDirectedPath() {
        super.testTraverseDirectedPath();
    }
}
