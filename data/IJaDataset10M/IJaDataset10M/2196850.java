package Examples;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphModel;

public class HelloWorld {

    public static void main(String[] args) {
        GraphModel model = new DefaultGraphModel();
        JGraph graph = new JGraph(model);
        graph.setCloneable(true);
        graph.setInvokesStopCellEditing(true);
        graph.setJumpToDefaultPort(true);
        DefaultGraphCell[] cells = new DefaultGraphCell[3];
        cells[0] = createVertex("Hello", 20, 20, 40, 20, null, false);
        cells[1] = createVertex("World", 140, 140, 40, 20, Color.ORANGE, true);
        DefaultEdge edge = new DefaultEdge();
        edge.setSource(cells[0].getChildAt(0));
        edge.setTarget(cells[1].getChildAt(0));
        cells[2] = edge;
        int arrow = GraphConstants.ARROW_CLASSIC;
        GraphConstants.setLineEnd(edge.getAttributes(), arrow);
        GraphConstants.setEndFill(edge.getAttributes(), true);
        graph.getGraphLayoutCache().insert(cells);
        JFrame frame = new JFrame();
        frame.getContentPane().add(new JScrollPane(graph));
        frame.pack();
        frame.setVisible(true);
    }

    public static DefaultGraphCell createVertex(String name, double x, double y, double w, double h, Color bg, boolean raised) {
        DefaultGraphCell cell = new DefaultGraphCell(name);
        GraphConstants.setBounds(cell.getAttributes(), new Rectangle2D.Double(x, y, w, h));
        if (bg != null) {
            GraphConstants.setGradientColor(cell.getAttributes(), Color.orange);
            GraphConstants.setOpaque(cell.getAttributes(), true);
        }
        if (raised) GraphConstants.setBorder(cell.getAttributes(), BorderFactory.createRaisedBevelBorder()); else GraphConstants.setBorderColor(cell.getAttributes(), Color.black);
        DefaultPort port = new DefaultPort();
        cell.add(port);
        return cell;
    }
}
