package de.mpiwg.vspace.vspacemaps.diagram.application;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.emf.workspace.AbstractEMFOperation;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.LayoutService;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.LayoutType;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import de.mpiwg.vspace.vspacemaps.diagram.part.VspacemapsDiagramEditor;

public class ArrangeRadialEditorAction implements IEditorActionDelegate {

    private IEditorPart activeEditor;

    public void run(IAction action) {
        if (activeEditor instanceof VspacemapsDiagramEditor) {
            DiagramEditPart diagramEditPart = ((VspacemapsDiagramEditor) activeEditor).getDiagramEditPart();
            if (diagramEditPart.getDiagramView() != null) {
                final Diagram diag = diagramEditPart.getDiagramView();
                TransactionalEditingDomain ted = TransactionUtil.getEditingDomain(diag);
                AbstractEMFOperation operation = new AbstractEMFOperation(ted, LayoutType.RADIAL, null) {

                    protected IStatus doExecute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
                        LayoutService.getInstance().layout(diag, LayoutType.RADIAL);
                        return Status.OK_STATUS;
                    }
                };
                try {
                    operation.execute(new NullProgressMonitor(), null);
                } catch (Exception e) {
                    throw new RuntimeException(e.getCause());
                }
            }
        }
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }

    public void setActiveEditor(IAction action, IEditorPart targetEditor) {
        this.activeEditor = targetEditor;
    }
}
