package net.sf.yari.hypergraph.swt;

import hypergraph.graphApi.Graph;
import hypergraph.graphApi.Node;
import hypergraph.visualnet.GraphPanel;
import java.awt.event.MouseEvent;
import net.sf.yari.YariFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Remo Loetscher
 * 
 */
public class SWTGraphPanel extends GraphPanel {

    private static final long serialVersionUID = 3349215072323679482L;

    private static Control oldControl;

    private static Color oldControlBackgroundColor;

    /**
	 * @param arg0
	 */
    public SWTGraphPanel(Graph arg0) {
        super(arg0);
    }

    @Override
    public void createGraphLayout() {
        getPropertyManager().setProperty("hypergraph.visualnet.TreeLayout.defaultSize", new Double(0.3));
        getPropertyManager().setProperty("hypergraph.visualnet.TreeLayout.mindistance", new Double(0.3));
        super.createGraphLayout();
    }

    /**
	 * 
	 */
    public void dispose() {
        if (oldControl != null && oldControlBackgroundColor != null) {
            if (oldControl.isDisposed()) {
                oldControl = null;
                oldControlBackgroundColor = null;
                return;
            }
            oldControl.setBackground(oldControlBackgroundColor);
            oldControl.update();
        }
    }

    @Override
    public void nodeClicked(int clicks, Node node) {
        super.nodeClicked(clicks, node);
        setHoverElement(node, true);
        if (node instanceof SWTNodeImpl) {
            final Control c = ((SWTNodeImpl) node).getSwtControl();
            if (clicks >= 2) {
                Display.getDefault().asyncExec(new Runnable() {

                    public void run() {
                        inspectData(c, c.getShell());
                    }
                });
            } else {
                Display.getDefault().asyncExec(new Runnable() {

                    public void run() {
                        if (oldControl != null && oldControlBackgroundColor != null) {
                            oldControl.setBackground(oldControlBackgroundColor);
                            oldControl.update();
                        }
                        oldControl = c;
                        oldControlBackgroundColor = c.getBackground();
                        c.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_MAGENTA));
                        c.update();
                    }
                });
            }
        }
    }

    /**
	 * @param widget
	 * @param rootShell
	 */
    void inspectData(Control widget, Shell rootShell) {
        YariFactory.openPropertiesPart(widget, rootShell);
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
    }
}
