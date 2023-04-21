package net.sf.jeda.schematics.visual;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.JComponent;
import net.sf.jeda.schematics.visual.api.SchematicEdge;
import net.sf.jeda.schematics.visual.api.SchematicNode;
import net.sf.jeda.schematics.visual.api.SchematicPin;
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.anchor.PointShape;
import org.netbeans.api.visual.graph.GraphPinScene;
import org.netbeans.api.visual.router.RouterFactory;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.spi.palette.PaletteController;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author eduardo-costa
 */
public class GraphSceneImpl extends GraphPinScene<SchematicNode, SchematicEdge, SchematicPin> {

    private WidgetAction moveAction = ActionFactory.createMoveAction(null, new MultiMoveProvider(this));

    private ConnectProviderImpl connectProvider;

    private ReconnectProviderImpl reconnectProviderImpl;

    private LayerWidget backLayer;

    private LayerWidget mainLayer;

    private LayerWidget edgeLayer;

    private LayerWidget tempLayer;

    public GraphSceneImpl() {
        addChild(backLayer = new LayerWidget(this));
        addChild(mainLayer = new LayerWidget(this));
        addChild(edgeLayer = new LayerWidget(this));
        addChild(tempLayer = new LayerWidget(this));
        connectProvider = new ConnectProviderImpl(this, edgeLayer);
        reconnectProviderImpl = new ReconnectProviderImpl(this);
        getActions().addAction(ActionFactory.createPanAction());
        getActions().addAction(ActionFactory.createZoomAction());
        getActions().addAction(ActionFactory.createRectangularSelectAction(this, backLayer));
        getActions().addAction(ActionFactory.createAcceptAction(new AcceptProvider() {

            public ConnectorState isAcceptable(Widget widget, Point point, Transferable transferable) {
                try {
                    if (!transferable.isDataFlavorSupported(PaletteController.ITEM_DATA_FLAVOR)) {
                        return ConnectorState.REJECT;
                    }
                    Lookup l = (Lookup) transferable.getTransferData(PaletteController.ITEM_DATA_FLAVOR);
                    SchematicNode node = l.lookup(SchematicNode.class);
                    if (node == null) {
                        return ConnectorState.REJECT;
                    }
                    JComponent view = getView();
                    Graphics2D g2 = (Graphics2D) view.getGraphics();
                    Rectangle visRect = view.getVisibleRect();
                    view.paintImmediately(visRect.x, visRect.y, visRect.width, visRect.height);
                    g2.translate(point.getLocation().getX(), point.getLocation().getY());
                    node.paint(g2);
                    return ConnectorState.ACCEPT;
                } catch (UnsupportedFlavorException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
                return ConnectorState.REJECT;
            }

            public void accept(Widget widget, Point point, Transferable transferable) {
                try {
                    Lookup l = (Lookup) transferable.getTransferData(PaletteController.ITEM_DATA_FLAVOR);
                    SchematicNode original = l.lookup(SchematicNode.class);
                    SchematicNode sym = new SchematicNodeInstance(original);
                    Widget w = addNode(sym);
                    w.setPreferredLocation(widget.convertLocalToScene(point));
                    mainLayer.addChild(w);
                    for (SchematicPin p : sym.getPinNodes()) {
                        Widget c = addPin(sym, p);
                        w.addChild(c);
                    }
                } catch (UnsupportedFlavorException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }));
    }

    @Override
    protected Widget attachNodeWidget(SchematicNode node) {
        Widget w = new SchematicNodeWidget(this, node);
        w.getActions().addAction(createSelectAction());
        w.getActions().addAction(moveAction);
        w.getActions().addAction(createWidgetHoverAction());
        return w;
    }

    @Override
    protected Widget attachEdgeWidget(SchematicEdge edge) {
        EdgeWidget w = new EdgeWidget(this, edge);
        w.setPaintControlPoints(true);
        w.setControlPointShape(PointShape.SQUARE_FILLED_BIG);
        w.setRouter(RouterFactory.createOrthogonalSearchRouter(mainLayer));
        w.getActions().addAction(createSelectAction());
        w.getActions().addAction(ActionFactory.createReconnectAction(reconnectProviderImpl, reconnectProviderImpl));
        w.getActions().addAction(ActionFactory.createOrthogonalMoveControlPointAction());
        w.getActions().addAction(ActionFactory.createAddRemoveControlPointAction());
        return w;
    }

    @Override
    protected Widget attachPinWidget(SchematicNode node, SchematicPin pin) {
        Widget w = new PinNodeWidget(this, pin);
        w.getActions().addAction(ActionFactory.createConnectAction(connectProvider, tempLayer, connectProvider));
        w.getActions().addAction(createWidgetHoverAction());
        return w;
    }

    @Override
    protected void attachEdgeSourceAnchor(SchematicEdge edge, SchematicPin oldPin, SchematicPin newPin) {
        Widget srcWidget = findWidget(newPin);
        Anchor anchor = connectProvider.createSourceAnchor(srcWidget);
        EdgeWidget edgeWidget = (EdgeWidget) findWidget(edge);
        edgeWidget.setSourceAnchor(anchor);
    }

    @Override
    protected void attachEdgeTargetAnchor(SchematicEdge edge, SchematicPin oldPin, SchematicPin newPin) {
        Widget targetWidget = findWidget(newPin);
        Anchor anchor = connectProvider.createSourceAnchor(targetWidget);
        EdgeWidget edgeWidget = (EdgeWidget) findWidget(edge);
        edgeWidget.setTargetAnchor(anchor);
    }
}
