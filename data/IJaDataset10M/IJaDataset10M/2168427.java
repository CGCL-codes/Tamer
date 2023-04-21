package de.uni_trier.st.nevada.analysis.property;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import de.uni_trier.st.nevada.analysis.AnalysisTools;
import de.uni_trier.st.nevada.graphs.Int.Edge;
import de.uni_trier.st.nevada.graphs.Int.Graph;
import de.uni_trier.st.nevada.graphs.Int.Node;

public class PropertyTree implements PropertyMetric {

    private AnalysisTools tools = new AnalysisTools();

    public boolean hasProperty(Graph g, boolean directed) {
        int count = 0;
        if (directed) {
            for (Node n : g.getNodes()) {
                if (n.getIncommingEdges(g).size() > 1) return false;
                if (n.getIncommingEdges(g).size() == 0) count++;
            }
            if (count < 1) return false;
        } else if (g.getNodes().iterator().hasNext()) {
            Node n = g.getNodes().iterator().next();
            HashSet<Node> visited = new HashSet<Node>();
            Node[] nodes = { null, n };
            visited.add(n);
            Stack<Node[]> S = new Stack<Node[]>();
            S.push(nodes);
            while (!S.empty()) {
                Node[] nodes2 = S.pop();
                Node pre = nodes2[0];
                Node ns = nodes2[1];
                for (Node neighbour : tools.getNeighbours(g, ns, false)) {
                    if (neighbour != pre) {
                        if (visited.contains(neighbour)) return false;
                        Node[] newnodes = { ns, neighbour };
                        S.push(newnodes);
                        visited.add(neighbour);
                    }
                }
            }
        }
        return true;
    }

    public JComponent getOptionsComponent() {
        JPanel test = new JPanel();
        test.add(new JLabel("No options available"));
        return test;
    }

    public void update() {
        this.tools.update();
    }
}
