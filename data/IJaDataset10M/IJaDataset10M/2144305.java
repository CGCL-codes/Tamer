package de.uni_trier.st.nevada.analysis.networkmetric;

import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import de.uni_trier.st.nevada.analysis.AnalysisTools;
import de.uni_trier.st.nevada.graphs.PresentGraph;
import de.uni_trier.st.nevada.graphs.Int.Edge;
import de.uni_trier.st.nevada.graphs.Int.Graph;
import de.uni_trier.st.nevada.graphs.Int.InvalidGraphTransformationException;
import de.uni_trier.st.nevada.graphs.Int.Node;
import de.uni_trier.st.nevada.view.WindowStore;
import de.uni_trier.st.nevada.view.central.GraphAnimationController;

public class NetworkCliques implements NetworkPatternMetric {

    private JSpinner numberK = new JSpinner(new SpinnerNumberModel(4, 2, 1000, 1));

    private WindowStore store;

    private AnalysisTools tools = new AnalysisTools();

    private HashMap<Graph, HashMap<Boolean, HashMap<Number, HashSet<Graph>>>> cliques = new HashMap<Graph, HashMap<Boolean, HashMap<Number, HashSet<Graph>>>>();

    public NetworkCliques(WindowStore store) {
        this.store = store;
    }

    public Set<Graph> findPatterns(Graph g, boolean directed) {
        if (!cliques.containsKey(g)) cliques.put(g, new HashMap<Boolean, HashMap<Number, HashSet<Graph>>>());
        if (!cliques.get(g).containsKey(directed)) cliques.get(g).put(directed, new HashMap<Number, HashSet<Graph>>());
        HashMap<Number, HashSet<Graph>> actCliques = cliques.get(g).get(directed);
        int k = ((SpinnerNumberModel) numberK.getModel()).getNumber().intValue();
        if (!actCliques.containsKey(2)) {
            HashSet<Graph> newGs = new HashSet<Graph>();
            actCliques.put(2, newGs);
            for (Edge e : g.getEdges()) {
                if (!directed || this.tools.getNeighbours(g, e.getTarget(), directed).contains(e.getSource())) {
                    Graph newG = new PresentGraph();
                    newG.addNode(e.getSource());
                    newG.addNode(e.getTarget());
                    try {
                        newG.addEdge(e);
                    } catch (InvalidGraphTransformationException e1) {
                        e1.printStackTrace();
                    }
                    newGs.add(newG);
                    cliques.get(g).get(directed).put(2, newGs);
                }
            }
        }
        for (int i = 3; i <= k; i++) {
            if (!actCliques.containsKey(i)) {
                HashSet<Graph> newGs = new HashSet<Graph>();
                actCliques.put(i, newGs);
                for (Graph g2 : actCliques.get(i - 1)) {
                    for (Node n : this.tools.getNeighboursForAll(g, g2.getNodes(), directed)) {
                        Graph newG = tools.cloneGraph(g2);
                        if (!directed && this.tools.getNeighbours(g, n, directed).containsAll(newG.getNodes()) || (directed && this.tools.getNeighbours(g, n, directed).containsAll(newG.getNodes()) && this.tools.getPredecessors(g, n).containsAll(newG.getNodes()))) {
                            for (Node newNode : newG.getNodes()) {
                                newG.addEdges(tools.getAllEdgesBetween(g, n, newNode, false));
                            }
                            newG.addNode(n);
                            if (!tools.isGraphWithSameNodesInGraphs(newG, newGs)) newGs.add(newG);
                        }
                    }
                }
                cliques.get(g).get(directed).put(i, newGs);
            }
        }
        return cliques.get(g).get(directed).get(k);
    }

    public JComponent getOptionsComponent() {
        JPanel result = new JPanel(new FlowLayout());
        result.add(new JLabel("Clique Members: "));
        this.numberK.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                for (GraphAnimationController c : NetworkCliques.this.store.getSelectedController()) {
                    c.updateView();
                    c.updateMetric();
                }
            }
        });
        result.add(this.numberK);
        return result;
    }

    public void update() {
        tools.update();
        cliques.clear();
    }
}
