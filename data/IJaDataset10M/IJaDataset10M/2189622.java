package org.suse.ui.part.graph;

import java.util.Observable;
import java.util.Observer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.jface.window.SameShellProvider;
import org.eclipse.ui.dialogs.PropertyDialogAction;
import org.suse.ui.model.Element;
import org.suse.ui.model.NotificationMessages;
import org.suse.ui.policy.EditPolicyInstaller;
import org.suse.ui.part.GraphElementEditPart;
import org.suse.ui.util.EditPartSelectionProviderAdapter;

public abstract class ElementGraphicalEditPart extends AbstractGraphicalEditPart implements Observer, NotificationMessages, GraphElementEditPart {

    public ElementGraphicalEditPart(Observable observable) {
        setModel(observable);
    }

    protected IFigure createFigure() {
        return getElement().getElementType().getContributor().createFigure();
    }

    protected void createEditPolicies() {
        EditPolicyInstaller.installEditPolicies(this);
    }

    private Element getElement() {
        return (Element) getModel();
    }

    public void activate() {
        if (!isActive()) {
            getElement().addObserver(this);
            super.activate();
        }
    }

    public void deactivate() {
        if (isActive()) {
            getElement().deleteObserver(this);
            super.deactivate();
        }
    }

    public void performRequest(Request request) {
        if (request.getType() == RequestConstants.REQ_OPEN) {
            openPropertyDialog();
        } else {
            super.performRequest(request);
        }
    }

    private void openPropertyDialog() {
        IShellProvider shellProvider = new SameShellProvider(getViewer().getControl().getShell());
        PropertyDialogAction propertyDialogAction = new PropertyDialogAction(shellProvider, new EditPartSelectionProviderAdapter(getViewer()));
        propertyDialogAction.run();
    }

    public boolean testAttribute(Object target, String name, String value) {
        Element element = (Element) ((ElementGraphicalEditPart) target).getModel();
        return element.testAttribute(element, name, value);
    }
}
