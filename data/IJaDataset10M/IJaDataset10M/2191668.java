package com.safi.workshop.providers;

import org.eclipse.gmf.runtime.emf.clipboard.core.CopyObjects;
import org.eclipse.gmf.runtime.emf.clipboard.core.CopyOperation;
import org.eclipse.gmf.runtime.emf.clipboard.core.OverrideCopyOperation;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.impl.DiagramImpl;
import org.eclipse.ui.IEditorPart;
import com.safi.workshop.edit.parts.HandlerEditPart;
import com.safi.workshop.part.AsteriskDiagramEditor;
import com.safi.workshop.part.AsteriskDiagramEditorPlugin;

public class ActionstepCopyAction extends OverrideCopyOperation {

    public ActionstepCopyAction(CopyOperation overriddenCopyOperation) {
        super(overriddenCopyOperation);
    }

    @Override
    protected CopyObjects getAuxiliaryObjects() {
        CopyObjects copyObjects = super.getAuxiliaryObjects();
        return copyObjects;
    }

    @Override
    public String copy() throws Exception {
        return doCopy();
    }

    @Override
    protected String doCopy() throws Exception {
        String result = super.doCopy();
        for (Object o : getEObjects()) {
            if (o instanceof Node && ((Node) o).eContainer() instanceof DiagramImpl) {
                DiagramImpl diag = (DiagramImpl) ((Node) o).eContainer();
                IEditorPart editor = AsteriskDiagramEditorPlugin.getInstance().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
                if (editor instanceof AsteriskDiagramEditor) {
                    AsteriskDiagramEditor asteriskDiagramEditor = (AsteriskDiagramEditor) editor;
                    HandlerEditPart part = (HandlerEditPart) asteriskDiagramEditor.getDiagramGraphicalViewer().getContents();
                    asteriskDiagramEditor.getDiagramGraphicalViewer().select(part);
                    break;
                }
            }
        }
        return result;
    }
}
