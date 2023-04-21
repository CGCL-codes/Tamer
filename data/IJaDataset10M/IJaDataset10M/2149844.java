package hub.sam.mof.simulator.editor.diagram.edit.commands;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class M3ActionsCreateShortcutDecorationsCommand extends AbstractTransactionalCommand {

    /**
	 * @generated
	 */
    private List myDescriptors;

    /**
	 * @generated
	 */
    public M3ActionsCreateShortcutDecorationsCommand(TransactionalEditingDomain editingDomain, View parentView, List viewDescriptors) {
        super(editingDomain, "Create Shortcuts", getWorkspaceFiles(parentView));
        myDescriptors = viewDescriptors;
    }

    /**
	 * @generated
	 */
    public M3ActionsCreateShortcutDecorationsCommand(TransactionalEditingDomain editingDomain, View parentView, org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest.ViewDescriptor viewDescriptor) {
        this(editingDomain, parentView, Collections.singletonList(viewDescriptor));
    }

    /**
	 * @generated
	 */
    protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        for (Iterator it = myDescriptors.iterator(); it.hasNext(); ) {
            org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest.ViewDescriptor nextDescriptor = (org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest.ViewDescriptor) it.next();
            View view = (View) nextDescriptor.getAdapter(View.class);
            if (view != null && view.getEAnnotation("Shortcut") == null) {
                EAnnotation shortcutAnnotation = org.eclipse.emf.ecore.EcoreFactory.eINSTANCE.createEAnnotation();
                shortcutAnnotation.setSource("Shortcut");
                shortcutAnnotation.getDetails().put("modelID", hub.sam.mof.simulator.editor.diagram.edit.parts.EPackageEditPart.MODEL_ID);
                view.getEAnnotations().add(shortcutAnnotation);
            }
        }
        return CommandResult.newOKCommandResult();
    }
}
