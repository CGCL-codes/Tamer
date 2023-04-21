package org.argouml.uml.diagram.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerManager;
import org.tigris.gef.base.ModeCreate;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.GraphNodeHooks;
import org.tigris.gef.graph.GraphNodeRenderer;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigPoly;
import org.tigris.gef.presentation.Handle;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.data_types.MAggregationKind;

/** A Mode to interpret user input while creating an edge.  Basically
 *  mouse down starts creating an edge from a source port Fig, mouse
 *  motion paints a rubberband line, mouse up finds the destination port
 *  and finishes creating the edge and makes an FigEdge and sends
 *  it to the back of the Layer.
 *
 *  The argument "edgeClass" determines the type if edge to suggest
 *  that the Editor's GraphModel construct.  The GraphModel is
 *  responsible for acutally making an edge in the underlying model
 *  and connecting it to other model elements. */
public class ModeCreateEdgeAndNode extends ModeCreate {

    public static int Drags_To_Existing = 0;

    public static int Drags_To_New = 0;

    /** The NetPort where the arc is paintn from */
    private Object _startPort;

    /** The Fig that presents the starting NetPort */
    private Fig _startPortFig;

    /** The FigNode on the NetNode that owns the start port */
    private FigNode _sourceFigNode;

    /** The new NetEdge that is being created */
    private Object _newEdge;

    /** False if drawing from source and destination.  True if drawing
   *  from destination to source. */
    private boolean _destToSource = false;

    protected Handle _handle = new Handle(-1);

    protected FigNode _fn;

    protected FigEdge _fe;

    protected boolean _postProcessEdge = false;

    public ModeCreateEdgeAndNode() {
        super();
    }

    public ModeCreateEdgeAndNode(Editor ed, Class edgeClass, Class nodeClass, boolean post) {
        super(ed);
        setArg("edgeClass", edgeClass);
        setArg("nodeClass", nodeClass);
        _postProcessEdge = post;
    }

    public void setup(FigNode fn, Object port, int x, int y, boolean reverse) {
        start();
        _sourceFigNode = fn;
        _startPortFig = fn.getPortFig(port);
        _startPort = port;
        _newItem = createNewItem(null, x, y);
        _destToSource = reverse;
        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }

    public String instructions() {
        return "Drag to define an edge (and a new node)";
    }

    /** Create the new item that will be drawn. In this case I would
   *  rather create the FigEdge when I am done. Here I just
   *  create a rubberband FigLine to show during dragging. */
    public Fig createNewItem(MouseEvent me, int snapX, int snapY) {
        FigPoly p = new FigPoly(snapX, snapY);
        p.setLineColor(Globals.getPrefs().getRubberbandColor());
        p.setFillColor(null);
        p.addPoint(snapX, snapY);
        return p;
    }

    public void done() {
        super.done();
        if (_newItem != null) editor.damaged(_newItem);
        _newItem = null;
        _sourceFigNode = null;
        _startPort = null;
        _startPortFig = null;
    }

    /** On mousePressed determine what port the user is dragging from.
   *  The mousePressed event is sent via ModeSelect. */
    public void mousePressed(MouseEvent me) {
    }

    /** On mouseReleased, find the destination port, ask the GraphModel
   *  to connect the two ports.  If that connection is allowed, then
   *  construct a new FigEdge and add it to the Layer and send it to
   *  the back. */
    public void mouseReleased(MouseEvent me) {
        if (me.isConsumed()) return;
        if (_sourceFigNode == null) {
            done();
            return;
        }
        boolean nodeWasCreated = false;
        int x = me.getX(), y = me.getY();
        Class arcClass;
        Editor ce = Globals.curEditor();
        Fig f = ce.hit(x, y);
        if (f == null) {
            f = ce.hit(x - 16, y - 16, 32, 32);
        }
        GraphModel gm = ce.getGraphModel();
        if (!(gm instanceof MutableGraphModel)) f = null;
        MutableGraphModel mgm = (MutableGraphModel) gm;
        if (f == null) {
            Drags_To_New++;
            Object newNode = null;
            Class nodeClass = (Class) getArg("nodeClass");
            try {
                newNode = nodeClass.newInstance();
            } catch (java.lang.IllegalAccessException ignore) {
                return;
            } catch (java.lang.InstantiationException ignore) {
                return;
            }
            if (newNode instanceof GraphNodeHooks) ((GraphNodeHooks) newNode).initialize(_args);
            if (mgm.canAddNode(newNode)) {
                GraphNodeRenderer renderer = editor.getGraphNodeRenderer();
                Layer lay = editor.getLayerManager().getActiveLayer();
                _fn = renderer.getFigNodeFor(gm, lay, newNode);
                editor.add(_fn);
                mgm.addNode(newNode);
                Fig encloser = null;
                Rectangle bbox = _fn.getBounds();
                Vector otherFigs = lay.getContents();
                java.util.Enumeration others = otherFigs.elements();
                while (others.hasMoreElements()) {
                    Fig otherFig = (Fig) others.nextElement();
                    if (!(otherFig instanceof FigNode)) continue;
                    if (otherFig.equals(_fn)) continue;
                    Rectangle trap = otherFig.getTrapRect();
                    if (trap != null && (trap.contains(bbox.x, bbox.y) && trap.contains(bbox.x + bbox.width, bbox.y + bbox.height))) encloser = otherFig;
                }
                _fn.setEnclosingFig(encloser);
                if (newNode instanceof GraphNodeHooks) ((GraphNodeHooks) newNode).postPlacement(editor);
                editor.getSelectionManager().select(_fn);
                nodeWasCreated = true;
                f = _fn;
                f.setLocation(x - f.getWidth() / 2, y - f.getHeight() / 2);
            }
        } else {
            Drags_To_Existing++;
        }
        if (f instanceof FigNode) {
            FigNode destFigNode = (FigNode) f;
            Object foundPort = destFigNode.deepHitPort(x, y);
            if (foundPort == null) {
                Vector portFigs = destFigNode.getPortFigs();
                if (portFigs.size() > 0) foundPort = ((Fig) portFigs.elementAt(0)).getOwner();
            }
            FigPoly p = (FigPoly) _newItem;
            editor.damaged(p);
            p._isComplete = true;
            if (foundPort != null && foundPort != _startPort) {
                Fig destPortFig = destFigNode.getPortFig(foundPort);
                Class edgeClass = (Class) getArg("edgeClass");
                if (_destToSource) {
                    Object temp = _startPort;
                    _startPort = foundPort;
                    foundPort = temp;
                    FigNode tempFN = destFigNode;
                    destFigNode = _sourceFigNode;
                    _sourceFigNode = tempFN;
                    Fig tempFigPort = _startPortFig;
                    _startPortFig = destPortFig;
                    destPortFig = tempFigPort;
                }
                if (edgeClass != null) {
                    _newEdge = mgm.connect(_startPort, foundPort, edgeClass);
                } else _newEdge = mgm.connect(_startPort, foundPort);
                if (_newEdge != null) {
                    if (_postProcessEdge) postProcessEdge();
                    ce.damaged(_newItem);
                    _sourceFigNode.damage();
                    destFigNode.damage();
                    LayerManager lm = ce.getLayerManager();
                    _fe = (FigEdge) lm.getActiveLayer().presentationFor(_newEdge);
                    _newItem.setLineColor(Color.black);
                    _fe.setLineColor(Color.black);
                    _fe.setFig(_newItem);
                    _fe.setSourcePortFig(_startPortFig);
                    _fe.setSourceFigNode(_sourceFigNode);
                    _fe.setDestPortFig(destPortFig);
                    _fe.setDestFigNode(destFigNode);
                    _fe.setSourcePortFig(_startPortFig);
                    _fe.setSourceFigNode(_sourceFigNode);
                    _fe.setDestPortFig(destPortFig);
                    _fe.setDestFigNode(destFigNode);
                    if (_fe != null && !nodeWasCreated) ce.getSelectionManager().select(_fe);
                    done();
                    _newItem = null;
                    if (_fe instanceof MouseListener) ((MouseListener) _fe).mouseReleased(me);
                    if (_sourceFigNode != null) _sourceFigNode.updateEdges();
                    if (destFigNode != null) destFigNode.updateEdges();
                    return;
                } else System.out.println("connection return null");
            } else System.out.println("in dest node but no port");
        }
        _sourceFigNode.damage();
        ce.damaged(_newItem);
        _newItem = null;
        done();
    }

    public void mouseMoved(MouseEvent me) {
        mouseDragged(me);
    }

    public void mouseDragged(MouseEvent me) {
        if (me.isConsumed()) return;
        int x = me.getX(), y = me.getY();
        if (_newItem == null) {
            me.consume();
            return;
        }
        FigPoly p = (FigPoly) _newItem;
        editor.damaged(_newItem);
        Point snapPt = new Point(x, y);
        editor.snap(snapPt);
        _handle.index = p.getNumPoints() - 1;
        p.moveVertex(_handle, snapPt.x, snapPt.y, true);
        editor.damaged(_newItem);
        me.consume();
    }

    public void keyTyped(KeyEvent ke) {
        if (ke.getKeyChar() == '') {
            done();
            ke.consume();
        }
    }

    public void postProcessEdge() {
        if (_newEdge instanceof MAssociation) {
            java.util.List conn = ((MAssociation) _newEdge).getConnections();
            MAssociationEnd ae0 = (MAssociationEnd) conn.get(0);
            ae0.setAggregation(MAggregationKind.COMPOSITE);
        }
    }

    static final long serialVersionUID = -427957543380196265L;
}
