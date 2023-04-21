package name.dlazerka.gm.graphmaker;

import name.dlazerka.gm.Graph;
import name.dlazerka.gm.GraphMagicAPI;
import name.dlazerka.gm.Vertex;
import name.dlazerka.gm.Visual;
import name.dlazerka.gm.exception.EdgeCreateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Set;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class CompleteGraphMakerItem extends GraphMakerItem {

    private static final Logger logger = LoggerFactory.getLogger(BipartiteGraphMakerItem.class);

    protected JTextField nField;

    public CompleteGraphMakerItem(GraphMagicAPI graphMagicAPI) {
        super(graphMagicAPI);
    }

    @Override
    public String getLabel() {
        return "Complete(n)";
    }

    @Override
    protected void perform() {
        String nText = nField.getText();
        Integer n = Integer.valueOf(nText);
        createAndConnect(n);
    }

    protected void createAndConnect(int n) {
        Graph graph = getGraphMagicAPI().getFocusedGraph();
        graph.clear();
        CycleIterator cycleIterator = new CycleIterator(n);
        while (cycleIterator.hasNext()) {
            Set<Vertex> existentVertices = graph.getVertexSet();
            Point2D point2D = cycleIterator.next();
            Vertex vertex = graph.createVertex();
            Visual visual = vertex.getVisual();
            visual.setCenter(point2D.getX(), point2D.getY());
            try {
                for (Vertex existentVertex : existentVertices) {
                    graph.createEdge(vertex, existentVertex);
                }
            } catch (EdgeCreateException e) {
            }
        }
    }

    @Override
    public void fillParamsPanel(JPanel panel) {
        logger.debug("");
        nField = new JTextField(10);
        panel.add(nField, BorderLayout.WEST);
        nField.setText("5");
        nField.selectAll();
        nField.requestFocusInWindow();
    }
}
