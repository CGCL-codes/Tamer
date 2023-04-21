package de.uni_trier.st.nevada.sight;

import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JPanel;
import de.uni_trier.st.nevada.graphs.Int.Edge;
import de.uni_trier.st.nevada.graphs.Int.Graph;
import de.uni_trier.st.nevada.graphs.Int.Node;
import de.uni_trier.st.nevada.layout.FLTAlgorithm;
import de.uni_trier.st.nevada.view.central.NodeTree;
import de.uni_trier.st.nevada.view.central.WindowPanel;

/**
 * This interface defines a Sight. A Sight is a kind of filter defining a new graph based on an original version and rules to compute nodes and
 * edges. <code>SightAdapter</code> offers a partial implementation and should be used as adapter for this interface.
 * @author reitz
 *
 */
public interface Sight {

    /**
	 * Clones the whole graph sequence. This copy is base for further computations
	 * @param graph
	 * @return
	 */
    public List<Graph> cloneGraphList(List<Graph> graph);

    public JPanel getOptionDialog();

    /**
	 * makes the Sight use the ruleset to transform it's copy of the graph
	 *
	 */
    public void createView();

    public void setWindowPanel(WindowPanel p);

    /**
	 * Adds a new node and forces recomputation of view if neccessary
	 * @param n
	 * @param g
	 * @return
	 */
    public Node addNode(Node n, Graph g);

    /**
	 * Applies the ruleset on one single Node
	 * @param n
	 * @param g
	 */
    public void processNode(Node n, Graph g);

    /**
	 * Adds a new edge and forces recomputation of view if neccessary
	 * @param e
	 * @param g
	 */
    public void addEdge(Edge e, Graph g);

    /**
	 * Applies the ruleset on one single Edge
	 * @param e
	 * @param g
	 */
    public void processEdge(Edge e, Graph g);

    /**
	 * Deletes Node and forces recomputation if nesseccary
	 * @param n
	 * @param g
	 */
    public void deleteNode(Node n, Graph g);

    /**
	 * Deletes Edge and forces recomputation if nesseccary
	 * @param e
	 * @param g
	 */
    public void deleteEdge(Edge e, Graph g);

    public Graph getGraph(Graph g);

    public Node getNode(Node n);

    /**
	 * Returns the name of this sight object, that is the type of the sight + a number
	 * @return
	 */
    public String getName();

    /**
	 * Returns just the name of the sight
	 * @return
	 */
    public String getCanonicName();

    public void performGraphLayout(final FLTAlgorithm f, Map<Graph, NodeTree> nodeStorage);

    public JPanel getOptions();

    public JComponent getComponent();

    public void update();
}
