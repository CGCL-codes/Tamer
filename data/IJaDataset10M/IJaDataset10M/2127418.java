package higraph.swing;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import higraph.model.interfaces.Edge;
import higraph.model.interfaces.Higraph;
import higraph.model.interfaces.Node;
import higraph.model.interfaces.Payload;
import higraph.model.interfaces.Subgraph;
import higraph.model.interfaces.WholeGraph;
import higraph.view.ComponentView;
import higraph.view.NodeView;
import higraph.view.HigraphView;
import higraph.view.interfaces.SubgraphEventObserver;
import tm.utilities.Assert;

/** <p>A Mouse Adapter for the
 * {@link higraph.view.HigraphView HigraphView}
 * class which handles both {@link java.awt.event.MouseListener} and
 * {@link java.awt.event.MouseMotionListener} events on {@link
 * higraph.view.ComponentView ComponentView} objects
 * within the {@link higraph.view.HigraphView HigraphView}.</p>
 * 
 * 
 * @author mpbl
 *
 * @param <NP> Node Payload
 * @param <EP> Edge Payload
 * @param <WG> Whole Graph
 * @param <SG> Subgraph
 * @param <N>  Node
 * @param <E>  Edge
 */
public class SubgraphMouseAdapter<NP extends Payload<NP>, EP extends Payload<EP>, HG extends Higraph<NP, EP, HG, WG, SG, N, E>, WG extends WholeGraph<NP, EP, HG, WG, SG, N, E>, SG extends Subgraph<NP, EP, HG, WG, SG, N, E>, N extends Node<NP, EP, HG, WG, SG, N, E>, E extends Edge<NP, EP, HG, WG, SG, N, E>> implements MouseListener, MouseMotionListener {

    protected ComponentView<NP, EP, HG, WG, SG, N, E> lastOverComponent;

    protected boolean lastOnBoundary = false;

    protected HigraphView<NP, EP, HG, WG, SG, N, E> theView;

    private SubgraphEventObserver<NP, EP, HG, WG, SG, N, E> observer;

    protected SubgraphTransferHandler<NP, EP, HG, WG, SG, N, E> myTransferHandler;

    public SubgraphMouseAdapter(HigraphView<NP, EP, HG, WG, SG, N, E> view, SubgraphEventObserver<NP, EP, HG, WG, SG, N, E> observer) {
        theView = view;
        this.observer = observer;
        myTransferHandler = new SubgraphTransferHandler<NP, EP, HG, WG, SG, N, E>(this);
    }

    public void installIn(JComponent jComponent) {
        jComponent.addMouseListener(this);
        jComponent.addMouseMotionListener(this);
        jComponent.setTransferHandler(myTransferHandler);
    }

    public SubgraphEventObserver<NP, EP, HG, WG, SG, N, E> getObserver() {
        return observer;
    }

    public void mouseMoved(MouseEvent e) {
        Stack<ComponentView<NP, EP, HG, WG, SG, N, E>> componentStack = findComponentsUnder(e.getPoint());
        observer.movedOver(componentStack, e);
    }

    public void mouseClicked(MouseEvent e) {
        Stack<ComponentView<NP, EP, HG, WG, SG, N, E>> componentStack = findComponentsUnder(e.getPoint());
        observer.clickedOn(componentStack, e);
    }

    public void mouseDragged(MouseEvent e) {
        Stack<ComponentView<NP, EP, HG, WG, SG, N, E>> componentStack = findComponentsUnder(e.getPoint());
        observer.dragged(componentStack, e);
    }

    public void mousePressed(MouseEvent e) {
        Stack<ComponentView<NP, EP, HG, WG, SG, N, E>> stack = findComponentsUnder(e.getPoint());
        observer.pressedOn(stack, e);
        JComponent jComponent = (JComponent) e.getSource();
        myTransferHandler.exportAsDrag(jComponent, e, TransferHandler.MOVE);
    }

    public void mouseReleased(MouseEvent e) {
        Stack<ComponentView<NP, EP, HG, WG, SG, N, E>> componentStack = findComponentsUnder(e.getPoint());
        observer.releasedOn(componentStack, e);
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    /**
	 * A simple minded search for a component under a mouse. This one
	 * uses a brute force approach that takes no advantage of layout
	 * information.
	 * @param p the point under which components are to be found
	 * @return a stack of Component Views representing all components found
	 *          under the point, the top of stack representing the component
	 *          closest to the viewer.
	 */
    protected Stack<ComponentView<NP, EP, HG, WG, SG, N, E>> findComponentsUnder(Point p) {
        Iterator<NodeView<NP, EP, HG, WG, SG, N, E>> roots = theView.getTops();
        Stack<ComponentView<NP, EP, HG, WG, SG, N, E>> theStack = new Stack<ComponentView<NP, EP, HG, WG, SG, N, E>>();
        NodeView<NP, EP, HG, WG, SG, N, E> nodeView;
        while (roots.hasNext()) {
            nodeView = roots.next();
            Assert.check(nodeView != null, "nodeView is null in findComponentsUnder");
            Assert.check(nodeView.getExtent() != null, "nodeView.getExtent is null in findComponentsUnder");
            findComponentsUnder(theStack, nodeView, p);
        }
        return theStack;
    }

    protected void findComponentsUnder(Stack<ComponentView<NP, EP, HG, WG, SG, N, E>> stack, NodeView<NP, EP, HG, WG, SG, N, E> nodeView, Point p) {
        if (nodeView.getExtent().contains(p)) {
            nodeView.getComponentsUnder(stack, p);
            for (int i = 0; i < nodeView.getNumChildren(); i++) findComponentsUnder(stack, nodeView.getChild(i), p);
        }
    }
}
