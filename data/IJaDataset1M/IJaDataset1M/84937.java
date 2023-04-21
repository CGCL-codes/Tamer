package de.uka.aifb.owl.odm.module.seperator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.notation.impl.NodeImpl;
import org.eclipse.jface.viewers.IStructuredSelection;
import com.ontoprise.ontostudio.datamodel.exception.ControlException;

public class ModuleSeparatorDiagramElement extends ModuleSeparatorEObject {

    protected ModuleSeparatorDiagramElement(IStructuredSelection selection, String moduleUri, String projectName) throws ControlException {
        super(selection, moduleUri, projectName);
    }

    protected EObject getOWLEntity(Object selectionItem) {
        assert selectionItem instanceof ShapeNodeEditPart;
        ShapeNodeEditPart editPart = (ShapeNodeEditPart) selectionItem;
        return (EObject) ((NodeImpl) editPart.getModel()).getElement();
    }
}
