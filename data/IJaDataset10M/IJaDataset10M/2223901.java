package de.uni_trier.st.nevada.analysis.test;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import de.uni_trier.st.nevada.analysis.nodemetric.NodeMetric;
import de.uni_trier.st.nevada.graphs.Int.Graph;
import de.uni_trier.st.nevada.graphs.Int.Node;

public class NodeMetricTest implements NodeMetric {

    private Map<Graph, Map<Node, Double>> store;

    private Map<Graph, Map<Node, Double>> normalizedStore;

    public NodeMetricTest() {
        this.store = new HashMap<Graph, Map<Node, Double>>();
        this.normalizedStore = new HashMap<Graph, Map<Node, Double>>();
    }

    public double normalizedValue(Graph g, Node n, boolean undirected) {
        if (normalizedStore.get(g) == null) this.computeNormalizedValue(g, undirected);
        return this.normalizedStore.get(g).get(n);
    }

    public double value(Graph g, Node n, boolean directed) {
        if (store.get(g) == null) this.computeValue(g, directed);
        return this.store.get(g).get(n);
    }

    private void computeValue(Graph g, boolean directed) {
        store.put(g, new HashMap<Node, Double>());
        for (Node n : g.getNodes()) {
            store.get(g).put(n, Math.random() * 100);
        }
    }

    private void computeNormalizedValue(Graph g, boolean undirected) {
        this.normalizedStore.put(g, new HashMap<Node, Double>());
        for (Node n : g.getNodes()) {
            this.normalizedStore.get(g).put(n, Math.random());
        }
    }

    public JComponent getOptionsComponent() {
        JPanel test = new JPanel();
        test.add(new JLabel("test"));
        return test;
    }

    public void update() {
        this.store.clear();
        this.normalizedStore.clear();
    }
}
