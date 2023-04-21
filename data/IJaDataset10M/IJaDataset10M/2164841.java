package org.ist.contract.edit;

import java.util.List;
import java.util.Vector;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.views.properties.IPropertySource;
import org.ist.contract.impl.ContractImpl;
import org.ist.contract.model.EObjectPropertySource;

/**
 * EditPart for the main component "Contract".
 */
public class ContractEditPart extends AbstractGraphicalEditPart implements Adapter {

    private IPropertySource propertySource = null;

    private Notifier target;

    private Shell shell;

    /**
	 * @param element
	 */
    public ContractEditPart(ContractImpl Contract) {
        setModel(Contract);
    }

    protected IFigure createFigure() {
        FreeformLayer layer = new FreeformLayer();
        layer.setLayoutManager(new FreeformLayout());
        layer.setBorder(new LineBorder(1));
        return layer;
    }

    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.CONTAINER_ROLE, new ContractEditPolicy());
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new ContractXYLayoutEditPolicy());
        installEditPolicy(EditPolicy.NODE_ROLE, null);
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, null);
        installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, null);
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
    }

    /**
	 * Returns the Contract.
	 * @return the Contract
	 */
    public ContractImpl getContract() {
        return (ContractImpl) getModel();
    }

    protected List getModelChildren() {
        List children = new Vector();
        children.addAll(getContract().getAgents());
        children.addAll(getContract().getRoles());
        children.addAll(getContract().getActions());
        children.addAll(getContract().getClauses());
        children.addAll(getContract().getObjects());
        children.addAll(getContract().getPredicates());
        return children;
    }

    public void activate() {
        if (isActive()) return;
        ((Notifier) getContract()).eAdapters().add(this);
        super.activate();
    }

    public void deactivate() {
        if (!isActive()) return;
        ((Notifier) getContract()).eAdapters().remove(this);
        super.deactivate();
    }

    public Notifier getTarget() {
        return target;
    }

    public boolean isAdapterForType(Object type) {
        return type.equals(getModel().getClass());
    }

    public void setTarget(Notifier newTarget) {
        target = newTarget;
    }

    public Object getAdapter(Class key) {
        if (IPropertySource.class == key) {
            return getPropertySource();
        }
        return super.getAdapter(key);
    }

    protected IPropertySource getPropertySource() {
        if (propertySource == null) {
            propertySource = new EObjectPropertySource(getContract());
        }
        return propertySource;
    }

    /**
	 * Notify changes from the model to the view.
	 */
    public void notifyChanged(Notification notification) {
        int type = notification.getEventType();
        switch(type) {
            case Notification.ADD:
            case Notification.ADD_MANY:
            case Notification.REMOVE:
            case Notification.REMOVE_MANY:
                refreshChildren();
                break;
            case Notification.SET:
                refreshVisuals();
                break;
        }
    }

    /**
	 * @return Returns the shell.
	 */
    public Shell getShell() {
        return shell;
    }

    /**
	 * @param shell The shell to set.
	 */
    public void setShell(Shell shell) {
        this.shell = shell;
    }
}
