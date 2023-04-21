package edu.gsbme.gyoza2d.Input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.jgraph.graph.CellHandle;
import org.jgraph.graph.GraphContext;
import org.jgraph.plaf.basic.BasicGraphUI;
import edu.gsbme.gyoza2d.GraphGenerator.GraphGenerator;

/**
 * Overrides BasicGraphUI to provide zooming capability
 * @author David
 *
 */
public class myBasicGraphUI extends BasicGraphUI {

    Composite parent;

    GraphGenerator graphEngine;

    public myBasicGraphUI(GraphGenerator graphEngine, Composite parent) {
        super();
        this.graphEngine = graphEngine;
        this.parent = parent;
    }

    public myBasicGraphUI() {
        super();
    }

    public CellHandle createHandle(GraphContext context) {
        return new MyRootHandle(this, context, graphEngine, parent);
    }

    public MouseListener createMouseListener() {
        return new myMouseHandler();
    }

    class myMouseHandler extends BasicGraphUI.MouseHandler implements MouseWheelListener {

        public void mousePressed(MouseEvent e) {
            if (e.getButton() == e.BUTTON1 && e.isShiftDown()) {
                Display.getDefault().asyncExec(new Runnable() {

                    @Override
                    public void run() {
                        graphEngine.returnCurrentViewState().setZoomLevel(graphEngine.returnJGraph().getScale() * 0.75);
                        graphEngine.returnCurrentViewState().NotifyChange();
                    }
                });
                graph.setScale(graph.getScale() * 0.75, new Point2D.Double(e.getX(), e.getY()));
            } else if (e.getButton() == e.BUTTON3 && e.isShiftDown()) {
                Display.getDefault().asyncExec(new Runnable() {

                    @Override
                    public void run() {
                        graphEngine.returnCurrentViewState().setZoomLevel(graphEngine.returnJGraph().getScale() / 0.75);
                        graphEngine.returnCurrentViewState().NotifyChange();
                    }
                });
                graph.setScale(graph.getScale() / 0.75, new Point2D.Double(e.getX(), e.getY()));
            }
            super.mousePressed(e);
        }
    }
}
