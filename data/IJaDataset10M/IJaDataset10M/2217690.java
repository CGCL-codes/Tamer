package manetconfig;

import java.awt.*;
import java.net.URL;
import java.util.*;
import javax.swing.*;
import org.jgraph.JGraph;
import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelListener;
import org.jgraph.example.GraphEd;
import org.jgraph.graph.*;
import com.jgraph.example.GraphEdX;

/**
 * This is the main class of the application. It creates the GUI where users can create
 * the multi-hop scenarios and also apply the topology changes or the mobility models  
 * @author Francisco Antonio Bas Esparza
 *
 */
public class ManetConfigGUI extends Observable implements GraphModelListener, GraphSerializationListener {

    public static final int CONFIGURATION_MODE = 0;

    public static final int SIMULATION_MODE = 1;

    private GraphEdX graphEd;

    private JSplitPane mainPanel;

    JSplitPane rightPanel;

    NetworkMobilityPanel networkMobilityPanel;

    /**
	 * Constructor. Creates the main graphical user interface
	 *
	 */
    public ManetConfigGUI() {
        mainPanel = new JSplitPane();
        graphEd = new GraphEdX();
        graphEd.setPreferredSize(new Dimension(850, 650));
        graphEd.getGraph().setGridEnabled(true);
        graphEd.getGraph().setGridVisible(true);
        graphEd.getGraph().setDisconnectable(false);
        graphEd.repaint();
        graphEd.addGraphSerializationListener(this);
        graphEd.getGraph().getModel().addGraphModelListener(this);
        mainPanel.setOneTouchExpandable(true);
        mainPanel.setDividerLocation(-1);
        mainPanel.setLeftComponent(this.graphEd);
        NetworkEditorPanel networkEditorPanel = new NetworkEditorPanel(graphEd);
        this.addObserver((Observer) networkEditorPanel);
        networkMobilityPanel = new NetworkMobilityPanel(this.graphEd);
        networkMobilityPanel.addObserver(networkEditorPanel);
        this.addObserver((Observer) networkMobilityPanel);
        rightPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, networkMobilityPanel, networkEditorPanel);
        rightPanel.setOneTouchExpandable(true);
        mainPanel.setRightComponent(rightPanel);
    }

    /**
	 * returns the GUI
	 * @return
	 */
    public JComponent getGUI() {
        return this.mainPanel;
    }

    /**
	 * This funcion is called by the JGraph event mecanish each thime the graph changes.
	 * It notifes the rest of the observers of the graph so the structures such as the tables 
	 * containing the node properties are updated accordingly
	 */
    public void graphChanged(GraphModelEvent arg0) {
        this.setChanged();
        this.notifyObservers(new GraphChange(GraphChange.ONLY_CHECK_CONNECTIVITY, null, null));
        Object[] inserted = arg0.getChange().getInserted();
        if (inserted != null) {
            for (int i = 0; i < inserted.length; i++) {
                if ((inserted[i] instanceof DefaultGraphCell) && !(inserted[i] instanceof DefaultPort) && !(inserted[i] instanceof DefaultEdge)) {
                    DefaultGraphCell vertex = (DefaultGraphCell) inserted[i];
                    this.setChanged();
                    this.notifyObservers(new GraphChange(GraphChange.ADD_NODE, vertex, null));
                }
            }
        }
        Object[] removed = arg0.getChange().getRemoved();
        if (removed != null) {
            for (int i = 0; i < removed.length; i++) {
                if ((removed[i] instanceof DefaultGraphCell) && !(removed[i] instanceof DefaultPort) && !(removed[i] instanceof DefaultEdge)) {
                    String nodeName = removed[i].toString();
                    DefaultGraphCell vertex = (DefaultGraphCell) removed[i];
                    this.setChanged();
                    this.notifyObservers(new GraphChange(GraphChange.REMOVE_NODE, vertex, null));
                }
            }
        }
        Object[] changed = arg0.getChange().getChanged();
        if (changed != null) {
            for (int i = 0; i < changed.length; i++) {
                if ((changed[i] instanceof DefaultGraphCell) && !(changed[i] instanceof DefaultPort) && !(changed[i] instanceof DefaultEdge)) {
                    DefaultGraphCell vertex = (DefaultGraphCell) changed[i];
                    String newName = vertex.toString();
                    String oldName = (String) vertex.getAttributes().get("NAME");
                    if (!newName.equals(oldName)) {
                        vertex.getAttributes().applyValue("NAME", vertex.toString());
                        this.setChanged();
                        this.notifyObservers(new GraphChange(GraphChange.CHANGE_NODE, vertex, null));
                    }
                }
            }
        }
    }

    /**
	 * This funcion is called by JGraph each time a graph is deserialized, i.e. when the user
	 * loads it from disk.
	 * It notifes the rest of the observers of the graph so the structures such as the tables 
	 * containing the node properties are updated accordingly
	 * are updated
	 */
    public void graphDeserialized() {
        this.setChanged();
        this.notifyObservers(new GraphChange(GraphChange.REMOVE_ALL_NODES, null, null));
        JGraph graph = this.graphEd.getGraph();
        GraphModel model = graph.getModel();
        Object[] roots = graph.getRoots();
        for (int i = 0; i < roots.length; i++) {
            if (DefaultGraphModel.isVertex(model, roots[i])) {
                DefaultGraphCell vertex = (DefaultGraphCell) roots[i];
                this.setChanged();
                this.notifyObservers(new GraphChange(GraphChange.ADD_NODE, vertex, null));
            }
        }
        this.setChanged();
        this.notifyObservers(new GraphChange(GraphChange.CHANGE_GRAPH, null, graph));
        graphEd.getGraph().getModel().addGraphModelListener(this);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        JGraph graph = this.graphEd.getGraph();
        GraphModel model = graph.getModel();
        Object[] roots = graph.getRoots();
        for (int i = 0; i < roots.length; i++) {
            if (DefaultGraphModel.isVertex(model, roots[i])) {
                DefaultGraphCell cell = (DefaultGraphCell) roots[i];
                int numChildren = model.getChildCount(cell);
                for (int j = 0; j < numChildren; j++) {
                    Object port = model.getChild(cell, j);
                    if (model.isPort(port)) {
                        Iterator iter = model.edges(port);
                        while (iter.hasNext()) {
                            DefaultEdge edge = (DefaultEdge) iter.next();
                            DefaultGraphCell target = (DefaultGraphCell) ((DefaultGraphCell) edge.getTarget()).getParent();
                            if (!cell.equals(target)) {
                                sb.append(cell.toString() + " --> " + target.toString() + "\n");
                            }
                        }
                    }
                }
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        ManetConfigGUI manetconfigGUI = new ManetConfigGUI();
        JFrame frame = new JFrame("MANET CONFIGURATION TOOL (v1.0)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(manetconfigGUI.getGUI());
        URL jgraphUrl = GraphEd.class.getClassLoader().getResource("images/logo_telecomparis.gif");
        if (jgraphUrl != null) {
            ImageIcon jgraphIcon = new ImageIcon(jgraphUrl);
            frame.setIconImage(jgraphIcon.getImage());
        }
        frame.setSize(1370, 850);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
