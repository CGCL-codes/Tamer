package odm.diagram.part;

import odm.diagram.edit.parts.OWLEditPart;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.core.GMFEditingDomainFactory;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

/**
 * @generated
 */
public class OdmInitDiagramFileAction implements IObjectActionDelegate {

    /**
	 * @generated
	 */
    private IWorkbenchPart myPart;

    /**
	 * @generated
	 */
    private IFile mySelectedModelFile;

    /**
	 * @generated
	 */
    private IStructuredSelection mySelection;

    /**
	 * @generated
	 */
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        myPart = targetPart;
    }

    /**
	 * @generated
	 */
    public void selectionChanged(IAction action, ISelection selection) {
        mySelectedModelFile = null;
        mySelection = StructuredSelection.EMPTY;
        action.setEnabled(false);
        if (selection instanceof IStructuredSelection == false || selection.isEmpty()) {
            return;
        }
        mySelection = (IStructuredSelection) selection;
        mySelectedModelFile = (IFile) ((IStructuredSelection) selection).getFirstElement();
        action.setEnabled(true);
    }
}
