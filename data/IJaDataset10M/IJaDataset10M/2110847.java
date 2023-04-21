package org.wsmostudio.bpmo.ui.editor.editpart;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.BendpointEditPolicy;
import org.eclipse.gef.requests.BendpointRequest;
import org.eclipse.swt.widgets.Display;
import org.omwg.ontology.*;
import org.wsmo.common.Identifier;
import org.wsmostudio.bpmo.ImagePool;
import org.wsmostudio.bpmo.model.connectors.DataflowConnector;
import org.wsmostudio.bpmo.model.connectors.GraphConnector;
import org.wsmostudio.bpmo.ui.editor.Utils;
import org.wsmostudio.bpmo.ui.editor.command.*;
import org.wsmostudio.bpmo.ui.preferences.DiagramColorsPage;
import org.wsmostudio.runtime.WSMORuntime;
import org.wsmostudio.ui.editors.common.DataValueEditor;
import org.wsmostudio.ui.editors.common.WSMOChooser;

/**
 * Edit part for Connection model elements.
 * <p>This edit part must implement the PropertyChangeListener interface, 
 * so it can be notified of property changes in the corresponding model element.
 * </p>
 */
public class DataflowConnectorEditPart extends GraphConnectorEditPart {

    public DataflowConnectorEditPart(EditPart iContext) {
        super(iContext);
    }

    protected void createEditPolicies() {
        super.createEditPolicies();
        installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, new BendpointEditPolicy() {

            protected Command getCreateBendpointCommand(BendpointRequest request) {
                CreateBendpointCommand com = new CreateBendpointCommand();
                Point p = request.getLocation();
                com.setLocation(p);
                Point ref1 = getConnection().getSourceAnchor().getReferencePoint();
                Point ref2 = getConnection().getTargetAnchor().getReferencePoint();
                com.setRelativeDimensions(p.getDifference(ref1), p.getDifference(ref2));
                com.setConnectionModel((GraphConnector) request.getSource().getModel());
                com.setIndex(request.getIndex());
                return com;
            }

            protected Command getDeleteBendpointCommand(BendpointRequest request) {
                BaseBendpointCommand com = new DeleteBendpointCommand();
                Point p = request.getLocation();
                com.setLocation(p);
                com.setConnectionModel((GraphConnector) request.getSource().getModel());
                com.setIndex(request.getIndex());
                return com;
            }

            protected Command getMoveBendpointCommand(BendpointRequest request) {
                MoveBendpointCommand com = new MoveBendpointCommand();
                Point p = request.getLocation();
                com.setLocation(p);
                Point ref1 = getConnection().getSourceAnchor().getReferencePoint();
                Point ref2 = getConnection().getTargetAnchor().getReferencePoint();
                com.setRelativeDimensions(p.getDifference(ref1), p.getDifference(ref2));
                com.setConnectionModel((GraphConnector) request.getSource().getModel());
                com.setIndex(request.getIndex());
                return com;
            }
        });
    }

    protected IFigure createFigure() {
        DataflowConnector model = (DataflowConnector) getModel();
        PolylineConnection connection = new PolylineConnection();
        connection.setTargetDecoration(new PolygonDecoration());
        connection.setLineStyle(model.getLineStyle());
        Utils.updateConnection(connection, model);
        connection.setLineWidth(2);
        lab = new Label();
        if (model.getDataType() != null) {
            Identifier typeID = (model.getDataType() instanceof WsmlDataType) ? ((WsmlDataType) model.getDataType()).getIRI() : ((Concept) model.getDataType()).getIdentifier();
            lab.setText(" " + org.wsmostudio.ui.Utils.getEntityLocalName(typeID.toString()) + " ");
        }
        lab.setOpaque(true);
        lab.setBackgroundColor(DiagramColorsPage.getConnectionLabelBackground());
        lab.setIcon(ImagePool.getImage(ImagePool.DATA_OBJECT_ICON).createImage());
        lab.setIconAlignment(PositionConstants.TOP);
        connection.setConnectionRouter(new BendpointConnectionRouter());
        AbstractLocator locator = new MidpointLocator(connection, 0);
        locator.setRelativePosition(PositionConstants.NORTH);
        locator.setGap(5);
        connection.add(lab, locator);
        return connection;
    }

    protected void performDirectEdit() {
        Type dataType = null;
        DataflowConnector modelConn = (DataflowConnector) getModel();
        if (modelConn.getDataType() != null && modelConn.getDataType() instanceof WsmlDataType) {
            dataType = DataValueEditor.createTypeSelector(Display.getCurrent().getActiveShell());
        } else {
            WSMOChooser ch = WSMOChooser.createConceptChooser(Display.getCurrent().getActiveShell(), WSMORuntime.getRuntime());
            dataType = (Type) ch.open();
        }
        if (dataType == null) {
            return;
        }
        ((DataflowConnector) getModel()).setDataType(dataType);
        refreshVisuals();
    }

    protected void refreshVisuals() {
        DataflowConnector model = (DataflowConnector) getModel();
        Utils.updateConnection((Shape) getFigure(), model);
        refreshBendpoints();
        if (model.getDataType() != null) {
            Identifier typeID = (model.getDataType() instanceof WsmlDataType) ? ((WsmlDataType) model.getDataType()).getIRI() : ((Concept) model.getDataType()).getIdentifier();
            lab.setText(" " + org.wsmostudio.ui.Utils.getEntityLocalName(typeID.toString()) + " ");
        } else {
            lab.setText("");
        }
        lab.setBackgroundColor(DiagramColorsPage.getConnectionLabelBackground());
    }
}
