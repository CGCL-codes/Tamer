package net.sourceforge.c4jplugin.internal.ui.actions;

import net.sourceforge.c4jplugin.C4JActivator;
import net.sourceforge.c4jplugin.internal.core.ContractReferenceModel;
import net.sourceforge.c4jplugin.internal.util.SelectionConverter;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

public class OpenDirectContractAction implements IObjectActionDelegate {

    public void run(IAction action) {
        IEditorInput editorInput = null;
        try {
            editorInput = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorInput();
        } catch (NullPointerException e) {
            return;
        }
        IResource resource = JavaUI.getEditorInputJavaElement(editorInput).getResource();
        IResource contract = ContractReferenceModel.getDirectContract(resource);
        if (contract == null) return;
        IJavaElement element = JavaCore.create(contract);
        try {
            JavaUI.openInEditor(element);
        } catch (PartInitException e) {
            C4JActivator.log(e);
        } catch (JavaModelException e) {
            C4JActivator.log(e);
        }
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        if (targetPart == null) return;
        IStructuredSelection selection;
        try {
            selection = SelectionConverter.getStructuredSelection(targetPart);
        } catch (JavaModelException e) {
            e.printStackTrace();
            action.setEnabled(false);
            return;
        }
        if (selection.isEmpty()) {
            action.setEnabled(false);
            return;
        }
        Object sel = selection.getFirstElement();
        if ((sel instanceof IMethod && ContractReferenceModel.isContracted(((IMethod) sel).getCompilationUnit().getResource())) || (sel instanceof ICompilationUnit && ContractReferenceModel.isContracted(((ICompilationUnit) sel).getResource()))) {
            action.setEnabled(true);
        } else {
            action.setEnabled(false);
        }
    }
}
