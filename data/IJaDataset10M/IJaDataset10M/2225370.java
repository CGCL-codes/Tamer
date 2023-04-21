package org.spbu.pldoctoolkit.graph.diagram.productline.part;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.spbu.pldoctoolkit.graph.diagram.productline.edit.parts.InfProductEditPart;
import org.spbu.pldoctoolkit.graph.diagram.productline.edit.parts.ProductEditPart;
import org.spbu.pldoctoolkit.graph.diagram.productline.edit.parts.ProductLine2EditPart;
import org.spbu.pldoctoolkit.graph.diagram.productline.edit.parts.ProductLineEditPart;
import org.spbu.pldoctoolkit.graph.util.DrlModelHelper;

public class DrlOpenElementFileAction implements IObjectActionDelegate {

    private EObject mySelectedModelElement;

    private Shell myShell;

    private static final Set<Class<? extends GraphicalEditPart>> ALLOWED_EDIT_PARTS = new HashSet<Class<? extends GraphicalEditPart>>();

    static {
        ALLOWED_EDIT_PARTS.add(InfProductEditPart.class);
        ALLOWED_EDIT_PARTS.add(ProductEditPart.class);
        ALLOWED_EDIT_PARTS.add(ProductLine2EditPart.class);
        ALLOWED_EDIT_PARTS.add(ProductLineEditPart.class);
    }

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        myShell = targetPart.getSite().getShell();
    }

    public void run(IAction action) {
        if (mySelectedModelElement == null) {
            return;
        }
        DrlModelHelper.openDrlEditor(mySelectedModelElement);
    }

    public void selectionChanged(IAction action, ISelection selection) {
        mySelectedModelElement = null;
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection structuredSelection = (IStructuredSelection) selection;
            if (structuredSelection.size() == 1) {
                Object selectedElement = structuredSelection.getFirstElement();
                if (ALLOWED_EDIT_PARTS.contains(selectedElement.getClass())) {
                    mySelectedModelElement = ((View) ((GraphicalEditPart) selectedElement).getModel()).getElement();
                }
            }
        }
        action.setEnabled(isEnabled());
    }

    private boolean isEnabled() {
        return mySelectedModelElement != null;
    }
}
