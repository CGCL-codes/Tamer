package edu.psu.bd.math.vertexreplacer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.EdgeShape;
import edu.uci.ics.jung.graph.decorators.EllipseVertexShapeFunction;
import edu.uci.ics.jung.graph.decorators.PickableVertexPaintFunction;
import edu.uci.ics.jung.graph.decorators.VertexIconAndShapeFunction;
import edu.uci.ics.jung.graph.decorators.VertexSizeFunction;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.PluggableRenderer;
import edu.uci.ics.jung.visualization.ShapePickSupport;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;

/**
 *
 * @author Joseph F. Pleso
 */
public class ShowLayout {

    private int iteration;

    private JFrame frame;

    private VertexReplacement vr;

    private GraphZoomScrollPane panel;

    private JPanel controls;

    public ShowLayout(VertexReplacement VR, JFrame jf) {
        vr = VR;
        frame = jf;
        iteration = 0;
        ShowIteration();
    }

    private class MyVertexSizeFunction implements VertexSizeFunction {

        private int size;

        public MyVertexSizeFunction(int i) {
            size = i;
        }

        public int getSize(Vertex v) {
            return size;
        }
    }

    public void ShowIteration() {
        VisualizationViewer vv;
        PluggableRenderer pr = new PluggableRenderer();
        pr.setVertexPaintFunction(new PickableVertexPaintFunction(pr, Color.black, Color.black, Color.yellow));
        EllipseVertexShapeFunction evsf = new EllipseVertexShapeFunction();
        evsf.setSizeFunction(new MyVertexSizeFunction(6 - iteration));
        VertexIconAndShapeFunction dvisf = new VertexIconAndShapeFunction(evsf);
        pr.setVertexShapeFunction(dvisf);
        pr.setVertexIconFunction(dvisf);
        vr.calcIteration(iteration, pr);
        vv = vr.getVV();
        vv.setPickSupport(new ShapePickSupport());
        pr.setEdgeShapeFunction(new EdgeShape.Line());
        vv.setBackground(Color.white);
        Container content = frame.getContentPane();
        panel = new GraphZoomScrollPane(vv);
        content.add(panel);
        final ModalGraphMouse gm = new DefaultModalGraphMouse();
        vv.setGraphMouse(gm);
        JButton plus = new JButton("Next Iteration");
        plus.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                plusFunction();
            }
        });
        JButton minus = new JButton("Previous Iteration");
        minus.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                minusFunction();
            }
        });
        controls = new JPanel();
        controls.add(plus);
        controls.add(minus);
        controls.add(((DefaultModalGraphMouse) gm).getModeComboBox());
        content.add(controls, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }

    private void plusFunction() {
        vr.returnLayout();
        iteration++;
        cleanup();
        ShowIteration();
    }

    private void minusFunction() {
        vr.returnLayout();
        iteration--;
        cleanup();
        if (iteration < 0) iteration = 0;
        ShowIteration();
    }

    public void cleanup() {
        Container c = frame.getContentPane();
        c.remove(panel);
        c.remove(controls);
    }
}
