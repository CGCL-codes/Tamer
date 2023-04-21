package hub.sam.mof.simulator.editor.diagram.providers;

import hub.sam.mof.simulator.editor.diagram.part.M3ActionsDiagramEditorPlugin;
import hub.sam.mof.simulator.editor.diagram.part.M3ActionsVisualIDRegistry;
import hub.sam.mof.simulator.editor.diagram.part.ValidateAction;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.AbstractContributionItemProvider;
import org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.action.IAction;

/**
 * @generated
 */
public class M3ActionsValidationProvider extends AbstractContributionItemProvider {

    /**
	 * @generated
	 */
    private static boolean constraintsActive = false;

    /**
	 * @generated
	 */
    public static boolean shouldConstraintsBePrivate() {
        return false;
    }

    /**
	 * @generated
	 */
    public static void runWithConstraints(TransactionalEditingDomain editingDomain, Runnable operation) {
        final Runnable op = operation;
        Runnable task = new Runnable() {

            public void run() {
                try {
                    constraintsActive = true;
                    op.run();
                } finally {
                    constraintsActive = false;
                }
            }
        };
        if (editingDomain != null) {
            try {
                editingDomain.runExclusive(task);
            } catch (Exception e) {
                M3ActionsDiagramEditorPlugin.getInstance().logError("Validation failed", e);
            }
        } else {
            task.run();
        }
    }

    /**
	 * @generated
	 */
    protected IAction createAction(String actionId, IWorkbenchPartDescriptor partDescriptor) {
        if (hub.sam.mof.simulator.editor.diagram.part.ValidateAction.VALIDATE_ACTION_KEY.equals(actionId)) {
            return new ValidateAction(partDescriptor);
        }
        return super.createAction(actionId, partDescriptor);
    }

    /**
	 * @generated
	 */
    static boolean isInDefaultEditorContext(Object object) {
        if (shouldConstraintsBePrivate() && !constraintsActive) {
            return false;
        }
        if (object instanceof View) {
            return constraintsActive && hub.sam.mof.simulator.editor.diagram.edit.parts.EPackageEditPart.MODEL_ID.equals(M3ActionsVisualIDRegistry.getModelID((View) object));
        }
        return true;
    }
}
