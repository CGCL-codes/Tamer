package org.ist.contract.edit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.views.properties.IPropertySource;
import org.ist.contract.ContractPackage;
import org.ist.contract.ContractUIPlugin;
import org.ist.contract.commands.DeleteClauseCommand;
import org.ist.contract.figures.ClauseFigure;
import org.ist.contract.impl.ActionImpl;
import org.ist.contract.impl.ClauseImpl;
import org.ist.contract.impl.ContractImpl;
import org.ist.contract.impl.PredicateImpl;
import org.ist.contract.impl.RoleImpl;
import org.ist.contract.model.EObjectPropertySource;
import org.ist.contract.model.element.ClauseActionArrow;
import org.ist.contract.model.element.ClausePredicateArrow;
import org.ist.contract.model.element.ClauseRoleArrow;

/**
 * EditPart for a Agent. 
 */
public class ContractClauseEditPart extends AbstractGraphicalEditPart implements NodeEditPart, Adapter {

    private IPropertySource propertySource = null;

    private Notifier target;

    private Shell shell;

    /**
	 * Creates a new ContractEditPart instance.
	 * @param element
	 */
    public ContractClauseEditPart(ClauseImpl Action) {
        setModel(Action);
    }

    public ClauseImpl getAction() {
        return (ClauseImpl) getModel();
    }

    protected List getModelTargetConnections() {
        ArrayList lResult = new ArrayList();
        Iterator lIterator = getAction().getRoles().iterator();
        while (lIterator.hasNext()) {
            Object LActionObject = lIterator.next();
            if (LActionObject instanceof RoleImpl) {
                RoleImpl lAction = (RoleImpl) LActionObject;
                boolean found = false;
                ClauseRoleArrow lClauseActionArrow = new ClauseRoleArrow(lAction, getAction());
                ArrayList pVirtualModelElements = (ArrayList) ContractUIPlugin.getDefault().getVirtualModelElements(this.getViewer());
                for (int i = 0; i < pVirtualModelElements.size(); i++) {
                    Object LVirtualElement = pVirtualModelElements.get(i);
                    if (LVirtualElement instanceof ClauseRoleArrow) {
                        found = ((ClauseRoleArrow) LVirtualElement).equals(lClauseActionArrow);
                        if (found) {
                            lClauseActionArrow = (ClauseRoleArrow) LVirtualElement;
                        }
                    }
                }
                if (!found) {
                    ContractUIPlugin.getDefault().addVirtualModelElements(this.getViewer(), lClauseActionArrow);
                }
                lResult.add(lClauseActionArrow);
            }
        }
        return lResult;
    }

    protected List getModelSourceConnections() {
        ArrayList lResult = new ArrayList();
        Iterator lIterator = getAction().getActions().iterator();
        while (lIterator.hasNext()) {
            Object LActionObject = lIterator.next();
            if (LActionObject instanceof ActionImpl) {
                ActionImpl lAction = (ActionImpl) LActionObject;
                boolean found = false;
                ClauseActionArrow lClauseActionArrow = new ClauseActionArrow(lAction, getAction());
                ArrayList pVirtualModelElements = (ArrayList) ContractUIPlugin.getDefault().getVirtualModelElements(this.getViewer());
                for (int i = 0; i < pVirtualModelElements.size(); i++) {
                    Object LVirtualElement = pVirtualModelElements.get(i);
                    if (LVirtualElement instanceof ClauseActionArrow) {
                        found = ((ClauseActionArrow) LVirtualElement).equals(lClauseActionArrow);
                        if (found) {
                            lClauseActionArrow = (ClauseActionArrow) LVirtualElement;
                        }
                    }
                }
                if (!found) {
                    ContractUIPlugin.getDefault().addVirtualModelElements(this.getViewer(), lClauseActionArrow);
                }
                lResult.add(lClauseActionArrow);
            }
        }
        lIterator = getAction().getPredicates().iterator();
        while (lIterator.hasNext()) {
            Object LActionObject = lIterator.next();
            if (LActionObject instanceof PredicateImpl) {
                PredicateImpl lAction = (PredicateImpl) LActionObject;
                boolean found = false;
                ClausePredicateArrow lClauseActionArrow = new ClausePredicateArrow(lAction, getAction());
                ArrayList pVirtualModelElements = (ArrayList) ContractUIPlugin.getDefault().getVirtualModelElements(this.getViewer());
                for (int i = 0; i < pVirtualModelElements.size(); i++) {
                    Object LVirtualElement = pVirtualModelElements.get(i);
                    if (LVirtualElement instanceof ClausePredicateArrow) {
                        found = ((ClausePredicateArrow) LVirtualElement).equals(lClauseActionArrow);
                        if (found) {
                            lClauseActionArrow = (ClausePredicateArrow) LVirtualElement;
                        }
                    }
                }
                if (!found) {
                    ContractUIPlugin.getDefault().addVirtualModelElements(this.getViewer(), lClauseActionArrow);
                }
                lResult.add(lClauseActionArrow);
            }
        }
        return lResult;
    }

    /** 
	 * Returns the Figure of this, as a Agent type figure.
	 *
	 * @return  Figure as a AgentFigure.
	 */
    protected ClauseFigure getObjectFigure() {
        return (ClauseFigure) getFigure();
    }

    public void notifyChanged(Notification notification) {
        int featureId = notification.getFeatureID(ContractPackage.class);
        switch(featureId) {
            case ContractPackage.CLAUSE__PREDICATES:
                ((PredicateImpl) notification.getNewValue()).getClauses().add(notification.getNotifier());
                refreshSourceConnections();
                break;
            case ContractPackage.CLAUSE__ACTIONS:
                ((ActionImpl) notification.getNewValue()).getClauses().add(notification.getNotifier());
                refreshSourceConnections();
                break;
            case ContractPackage.CLAUSE__ROLES:
                ((RoleImpl) notification.getNewValue()).getClauses().add(notification.getNotifier());
                refreshSourceConnections();
                refreshTargetConnections();
                break;
            default:
                refreshVisuals();
                break;
        }
    }

    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ContractClauseEditPolicy());
    }

    public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
        return getObjectFigure().getSourceConnectionAnchor();
    }

    public ConnectionAnchor getSourceConnectionAnchor(Request request) {
        return getObjectFigure().getSourceConnectionAnchor();
    }

    public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
        return getObjectFigure().getTargetConnectionAnchor();
    }

    public ConnectionAnchor getTargetConnectionAnchor(Request request) {
        return getObjectFigure().getTargetConnectionAnchor();
    }

    protected void refreshVisuals() {
        getObjectFigure().setName(getAction().getName());
        Point loc = new Point(getAction().getX() == null ? 0 : getAction().getX().intValue(), getAction().getY() == null ? 0 : getAction().getY().intValue());
        Dimension size = new Dimension(-1, -1);
        Rectangle r = new Rectangle(loc, size);
        ((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), r);
    }

    public void activate() {
        if (isActive()) return;
        ((Notifier) getAction()).eAdapters().add(this);
        super.activate();
    }

    public void deactivate() {
        if (!isActive()) return;
        ((Notifier) getAction()).eAdapters().remove(this);
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
            propertySource = new EObjectPropertySource(getAction());
        }
        return propertySource;
    }

    protected IFigure createFigure() {
        ClauseFigure figure = new ClauseFigure();
        return figure;
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

    public Command getCommand(Request request) {
        if (request.getType().equals("delete")) {
            Iterator lIterator = getSourceConnections().iterator();
            while (lIterator.hasNext()) {
                ContractUIPlugin.getDefault().dropVirtualModelElements(lIterator.next());
            }
            lIterator = getTargetConnections().iterator();
            while (lIterator.hasNext()) {
                ContractUIPlugin.getDefault().dropVirtualModelElements(lIterator.next());
            }
            refreshSourceConnections();
            refreshTargetConnections();
            DeleteClauseCommand lCommand = new DeleteClauseCommand();
            lCommand.setAgent(getAction());
            lCommand.setParent(((ContractImpl) ((ClauseImpl) getAction()).eContainer()));
            return lCommand;
        }
        return super.getCommand(request);
    }
}
