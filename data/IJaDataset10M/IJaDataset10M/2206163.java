package net.confex.schema.part;

import java.beans.PropertyChangeEvent;
import java.util.List;
import net.confex.schema.directedit.ActiveElementDirectEditPolicy;
import net.confex.schema.directedit.IElementPropertyDialog;
import net.confex.schema.directedit.ValidationMessageHandler;
import net.confex.schema.editor.ValidationEnabledGraphicalViewer;
import net.confex.schema.figures.EditableLabel;
import net.confex.schema.figures.IModelElementFigure;
import net.confex.schema.figures.SimpleContainerFigure;
import net.confex.schema.model.ActiveElement;
import net.confex.schema.model.NodeElement;
import net.confex.schema.model.SimpleContainer;
import net.confex.schema.model.StateContainer;
import net.confex.schema.policy.SchemaXYLayoutPolicy;
import net.confex.schema.policy.SimpleContainerContainerEditPolicy;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.swt.graphics.Color;

public class SimpleContainerPart extends NodeElementPart implements IModelElementContainerPart {

    protected DirectEditManager manager;

    /**
	 * @see org.eclipse.gef.EditPart#activate()
	 */
    public void activate() {
        super.activate();
    }

    /**
	 * @see org.eclipse.gef.EditPart#deactivate()
	 */
    public void deactivate() {
        super.deactivate();
    }

    /**
	 * Returns the SimpleContainer model object represented by this EditPart
	 */
    public SimpleContainer getSimpleContainer() {
        return (SimpleContainer) getModel();
    }

    /**
	 * @return the children Model objects as a new ArrayList
	 */
    protected List getModelChildren() {
        return getSimpleContainer().getChildren();
    }

    /**
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelSourceConnections()
	 */
    protected List getModelSourceConnections() {
        return getSimpleContainer().getOutConnections();
    }

    /**
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelTargetConnections()
	 */
    protected List getModelTargetConnections() {
        return getSimpleContainer().getInConnections();
    }

    /**
	 * ������� ��� figure � ��� �������� Visible
	 * @param vis_flag - true ��� ������
	 */
    public void setAllVisible(boolean visible) {
        if (children != null) {
            for (int i = 0; i < children.size(); i++) {
                ((ModelElementPart) children.get(i)).setAllVisible(visible);
            }
        }
        this.getFigure().setVisible(visible);
    }

    public void changeState(String state) {
        if (getModel() instanceof StateContainer) {
            ((StateContainer) getModel()).modifyState(state);
        }
    }

    /**
	 * If modified, sets states and fires off event notification
   	 * ������� ����� ������ ����� ��� ���� ������� ��������� !!!
	 */
    public void modifyState(String state) {
        if (getModel() instanceof StateContainer) {
            SimpleContainerFigure figure = (SimpleContainerFigure) this.getFigure();
            if (state.equals(StateContainer.COMPACT_STATE)) {
                setAllVisible(false);
                figure.modifyState(state);
            } else {
                setAllVisible(true);
                figure.modifyState(state);
            }
            updateBoundsFromModel();
        }
    }

    /**
	 * Creates edit policies and associates these with roles
	 */
    protected void createEditPolicies() {
        super.createEditPolicies();
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new SchemaXYLayoutPolicy());
        installEditPolicy(EditPolicy.CONTAINER_ROLE, new SimpleContainerContainerEditPolicy());
    }

    /**
	 * @see org.eclipse.gef.EditPart#performRequest(org.eclipse.gef.Request)
	public void performRequest(Request request)	{
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT)	{
			if (request instanceof DirectEditRequest
					&& !directEditHitTest(((DirectEditRequest) request).getLocation().getCopy()))
				return;
			performDirectEdit();
		}
	}
	 */
    private boolean directEditHitTest(Point requestLoc) {
        SimpleContainerFigure figure = (SimpleContainerFigure) getFigure();
        EditableLabel nameLabel = figure.getNameLabel();
        nameLabel.translateToRelative(requestLoc);
        if (nameLabel.containsPoint(requestLoc)) return true;
        return false;
    }

    /**
	 * �������������� ������� ����� ������ �������
	 *
	 */
    protected void performDirectEdit() {
        NodeElement element = getNodeElement();
        IElementPropertyDialog property_dialog = element.getPropertyDialog();
        property_dialog.setEditPart(this);
        property_dialog.show();
    }

    /**
	 * Sets layout constraint only if XYLayout is active
	 */
    public void setLayoutConstraint(EditPart child, IFigure childFigure, Object constraint) {
        super.setLayoutConstraint(child, childFigure, constraint);
    }

    /**
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
    public void propertyChange(PropertyChangeEvent evt) {
        String property = evt.getPropertyName();
        if (StateContainer.CONTAINER_STATE.equals(property)) {
            this.handleStateChange(evt);
        } else {
            super.propertyChange(evt);
        }
    }

    /**
	 * handles change in bounds, to be overridden by subclass
	 */
    protected void handleBoundsChange(PropertyChangeEvent evt) {
        super.handleBoundsChange(evt);
    }

    /**
	 * ������������ ��������� ��������� ���������� 
	 * @param evt
	 */
    protected void handleStateChange(PropertyChangeEvent evt) {
        String state = (String) evt.getNewValue();
        this.modifyState(state);
    }

    /**
	 * Handles change in name when committing a direct edit
	 */
    protected void handleNameChange(PropertyChangeEvent evt) {
        SimpleContainerFigure tableFigure = (SimpleContainerFigure) getFigure();
        EditableLabel label = tableFigure.getNameLabel();
        label.setText(getSimpleContainer().getText());
        label.setVisible(true);
        refreshVisuals();
    }

    protected void handleForeColorChange(PropertyChangeEvent evt) {
        super.handleForeColorChange(evt);
    }

    protected void handleBackColorChange(PropertyChangeEvent evt) {
        super.handleBackColorChange(evt);
    }

    /**
	 * Creates a figure only
	 */
    private IFigure create_figure() {
        SimpleContainer container = getSimpleContainer();
        EditableLabel label = new EditableLabel(container.getText());
        SimpleContainerFigure containerFigure = new SimpleContainerFigure(label);
        containerFigure.setBounds(container.getBounds());
        containerFigure.setForegroundColor(new Color(null, container.getForeColor()));
        containerFigure.setBackgroundColor(new Color(null, container.getBackColor()));
        this.setFigure(containerFigure);
        return containerFigure;
    }

    /**
	 * Creates a figure and modify by state
	 */
    protected IFigure createFigure() {
        SimpleContainerFigure containerFigure = (SimpleContainerFigure) create_figure();
        if (getModel() instanceof StateContainer) {
            modifyState(((StateContainer) getModel()).getCurrentState());
        }
        return containerFigure;
    }

    public IFigure getFigure() {
        if (figure == null) setFigure(createFigure());
        return figure;
    }

    /**
	 * �������������� �������� ��� ������� ����� �����
	 *  
	 * @return the Content pane for adding or removing child figures
	 */
    public IFigure getContentPane() {
        SimpleContainerFigure figure = (SimpleContainerFigure) getFigure();
        return figure.getContentsPane();
    }

    /**
	 * Reset the layout constraint, and revalidate the content pane
	*/
    protected void refreshVisuals() {
    }

    /**
	 * ������������� ��� ����������
	 */
    public void immediateRepaint() {
        getFigure().getUpdateManager().performUpdate();
    }

    /**
	 * Sets the width of the line when selected
	 */
    public void setSelected(int value) {
        super.setSelected(value);
        SimpleContainerFigure figure = (SimpleContainerFigure) getFigure();
        if (value != EditPart.SELECTED_NONE) figure.setSelected(true); else figure.setSelected(false);
        figure.repaint();
    }

    /**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#toString()
	 */
    public String toString() {
        return getModel().toString();
    }

    protected void addSourceConnection(ConnectionEditPart connection, int index) {
        connection.setParent(this);
        super.addSourceConnection(connection, index);
        connection.setParent(this);
    }
}
