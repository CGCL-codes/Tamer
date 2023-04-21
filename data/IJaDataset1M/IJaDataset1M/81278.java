package edu.georgiasouthern.math.flexagon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphConstants;
import com.jgraph.layout.JGraphFacade;
import edu.georgiasouthern.math.flexagon.graph.FlexGraphModel;
import edu.georgiasouthern.math.flexagon.graph.FlexagonCellViewFactory;
import edu.georgiasouthern.math.flexagon.graph.JGraphCircleView;
import edu.georgiasouthern.math.flexagon.graph.NodeData;

/**
 * Defines a wrapper for a graph panel.
 * 
 * 
 * @author Emil Iacob
 *
 */
public class GraphPanel extends JPanel {

    /**
	 * Holds the JGraph instance.
	 */
    private JGraph jgraph;

    private DefaultGraphCell lastVisitedVertex = null;

    /**
	 * Creates the graph panel.
	 *
	 */
    public GraphPanel() {
        initGraph();
        populateContentPane();
    }

    /**
	 * Adds a new vertex if not already in the graph.
	 * @param descriptor
	 */
    public void addVertex(String descriptor) {
        descriptor = descriptor.substring(descriptor.indexOf(" ")).trim();
        DefaultGraphCell newvertex = findGraphCell(descriptor);
        if (lastVisitedVertex != null && newvertex == lastVisitedVertex) {
            return;
        }
        if (newvertex == null) {
            newvertex = createNode(descriptor);
        }
        if (lastVisitedVertex != null) {
            createEdge(lastVisitedVertex, newvertex);
        }
        lastVisitedVertex = newvertex;
    }

    /**
	 * Creates a new graph node.
	 * @param id
	 * @param descriptor
	 */
    private DefaultGraphCell createNode(String descriptor) {
        String id = "" + getVerticesCount(jgraph);
        NodeData nd = new NodeData(id, descriptor);
        DefaultGraphCell cell = new DefaultGraphCell(nd);
        GraphConstants.setGradientColor(cell.getAttributes(), Color.green);
        GraphConstants.setOpaque(cell.getAttributes(), true);
        if (lastVisitedVertex != null) {
            Rectangle2D r = GraphConstants.getBounds(lastVisitedVertex.getAttributes());
            GraphConstants.setBounds(cell.getAttributes(), new Rectangle2D.Double(r.getMaxX() + 40, r.getMaxY() + 40, 2 * JGraphCircleView.RADIUS, 2 * JGraphCircleView.RADIUS));
        } else {
            GraphConstants.setBounds(cell.getAttributes(), new Rectangle2D.Double(40, 40, 2 * JGraphCircleView.RADIUS, 2 * JGraphCircleView.RADIUS));
        }
        GraphConstants.setBorderColor(cell.getAttributes(), Color.black);
        cell.addPort();
        jgraph.getGraphLayoutCache().insert(cell);
        return cell;
    }

    /**
	 * Creates an edge between the given nodes.
	 * @param source
	 * @param target
	 */
    private void createEdge(DefaultGraphCell source, DefaultGraphCell target) {
        DefaultEdge edge = new DefaultEdge();
        edge.setSource(source.getChildAt(0));
        edge.setTarget(target.getChildAt(0));
        GraphConstants.setLineEnd(edge.getAttributes(), GraphConstants.ARROW_CLASSIC);
        GraphConstants.setEndFill(edge.getAttributes(), true);
        jgraph.getGraphLayoutCache().insert(edge);
    }

    /**
	 * Initializes the graph.
	 *
	 */
    private void initGraph() {
        jgraph = new JGraph(new FlexGraphModel());
        jgraph.getGraphLayoutCache().setFactory(new FlexagonCellViewFactory());
        jgraph.setCloneable(true);
        jgraph.setInvokesStopCellEditing(true);
        jgraph.setJumpToDefaultPort(true);
        jgraph.setSizeable(false);
    }

    /**
	 * Adds components to the panel.
	 *
	 */
    private void populateContentPane() {
        setLayout(new BorderLayout());
        add(new JScrollPane(jgraph), BorderLayout.CENTER);
    }

    /**
	 * Searches for a graph vertex with a given description.
	 * @param desc
	 * @return
	 */
    public DefaultGraphCell findGraphCell(String desc) {
        Object[] list = getVertices(jgraph);
        for (int i = 0; i < list.length; i++) {
            Object v = list[i];
            if (v instanceof DefaultGraphCell) {
                DefaultGraphCell cell = (DefaultGraphCell) v;
                NodeData nd = (NodeData) cell.getUserObject();
                if (equalDescriptors(nd.getDescriptor(), desc)) {
                    return cell;
                }
            }
        }
        return null;
    }

    /**
	 * Compare for equality two node descriptors.
	 * @param desc1
	 * @param desc2
	 * @return
	 */
    private boolean equalDescriptors(String desc1, String desc2) {
        boolean equal = false;
        String[] t1 = desc1.split("[ -]+");
        String[] t2 = desc2.split("[ -]+");
        for (int i = 0; i < t2.length; i++) {
            if (t1[0].equals(t2[i])) {
                equal = true;
                int k = 1;
                for (int j = i + 1; j < t2.length; j++, k++) {
                    if (!t1[k].equals(t2[j])) {
                        equal = false;
                        break;
                    }
                }
                if (equal) {
                    for (int j = 0; j < i; j++, k++) {
                        if (!t1[k].equals(t2[j])) {
                            equal = false;
                            break;
                        }
                    }
                }
                break;
            }
        }
        return equal;
    }

    /**
	 * Returns an array with all graph vertices.
	 * @param jgraph
	 * @return
	 */
    public static Object[] getVertices(JGraph jgraph) {
        List list = DefaultGraphModel.getDescendants(jgraph.getModel(), DefaultGraphModel.getRoots(jgraph.getModel()));
        Vector v = new Vector();
        for (int i = 0; i < list.size(); i++) {
            if (DefaultGraphModel.isVertex(jgraph.getModel(), list.get(i))) {
                v.add(list.get(i));
            }
        }
        return v.toArray();
    }

    /**
	 * Returns the number of nodes in the graph.
	 * @param jgraph
	 * @return
	 */
    public static int getVerticesCount(JGraph jgraph) {
        return getVertices(jgraph).length;
    }
}
