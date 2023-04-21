package view.graphview;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.PickInfo;
import view.graphview.GraphNode;
import com.sun.j3d.utils.pickfast.PickCanvas;

/**
 * This class handles mouse clicking events on the canvas. It is responsible for recognising a click
 * as a click on a GraphNode, if one was clicked upon. It then informs graphView if the node was 
 * either selected (single click) or is to be zoomed to (double click)
 * 
 * @author Tao Zhou, Chris Ooi
 */
public class MouseClickHandler extends MouseAdapter {

    /** A standard PickCanvas utility object. */
    private PickCanvas pickCanvas;

    /** The GraphView object used to reference the objects clicked upon to nodes */
    private GraphView view;

    /**
	 * Constructor of MouseClickHandler. Takes the canvas this
	 * handler will be listening to and the root BranchGroup.
	 * 
	 * @param canvas canvas that this handler listens on.
	 * @param sceneRoot the root BranchGroup of a SceneGraph.
	 * @param view The graphView object the handler acts upon.
	 */
    protected MouseClickHandler(Canvas3D canvas, BranchGroup sceneRoot, GraphView view) {
        this.view = view;
        pickCanvas = new PickCanvas(canvas, sceneRoot);
        pickCanvas.setMode(PickInfo.PICK_GEOMETRY);
        pickCanvas.setTolerance(3.0f);
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
    }

    /**
	 * Overrides the parent method in MouseAdapter.
	 * Handle events that arise when mouse is clicked on the canvas.
	 * This method should not be called externally
	 * @param e The MouseEvent that triggered the click.
	 */
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 1) {
            handleNodeSelect(e);
        } else if (e.getClickCount() == 2) {
            handleZoomNode(e);
        }
    }

    private GraphNode determineClickedNode(MouseEvent e) {
        GraphNode clickedNode = null;
        pickCanvas.setShapeLocation(e);
        PickInfo result = pickCanvas.pickClosest();
        if (result == null) {
        } else {
            javax.media.j3d.Node j3dNode = result.getNode();
            while (j3dNode != null && (!(j3dNode instanceof BranchGroup)) || j3dNode.getName() == null) {
                j3dNode = j3dNode.getParent();
            }
            if (j3dNode != null) {
                clickedNode = view.lookupNode(j3dNode.getName());
            }
        }
        return clickedNode;
    }

    private void handleNodeSelect(MouseEvent e) {
        GraphNode n = determineClickedNode(e);
        view.handleClickedNode(n, e);
    }

    private void handleZoomNode(MouseEvent e) {
        GraphNode n = determineClickedNode(e);
        if (n != null) {
            view.zoomToNode(n);
        }
    }
}
