package de.uka.aifb.owl.gui.actions;

import odm.OWLEntity;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.notation.impl.NodeImpl;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class CreateModuleFromDiagram extends CreateModuleAction implements IObjectActionDelegate {

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
    }

    @Override
    protected String getNamespace() {
        return getOWLEntity().getURIFromOWLEntity().substring(0, (getOWLEntity().getURIFromOWLEntity().lastIndexOf("#") + 1));
    }

    private OWLEntity getOWLEntity() {
        ShapeNodeEditPart editPart = ((ShapeNodeEditPart) ((StructuredSelection) actualSelection).getFirstElement());
        return (OWLEntity) ((NodeImpl) editPart.getModel()).getElement();
    }
}
