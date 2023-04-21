package hub.sam.mof.simulator.editor.diagram.edit.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.commands.EditElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;

/**
 * @generated
 */
public class EClassESuperTypesCreateCommand extends EditElementCommand {

    /**
	 * @generated
	 */
    private final EObject source;

    /**
	 * @generated
	 */
    private final EObject target;

    /**
	 * @generated
	 */
    public EClassESuperTypesCreateCommand(CreateRelationshipRequest request, EObject source, EObject target) {
        super(request.getLabel(), null, request);
        this.source = source;
        this.target = target;
    }

    /**
	 * @generated
	 */
    public boolean canExecute() {
        if (source == null && target == null) {
            return false;
        }
        if (source != null && false == source instanceof EClass) {
            return false;
        }
        if (target != null && false == target instanceof EClass) {
            return false;
        }
        if (getSource() == null) {
            return true;
        }
        return hub.sam.mof.simulator.editor.diagram.edit.policies.M3ActionsBaseItemSemanticEditPolicy.LinkConstraints.canCreateEClassESuperTypes_3003(getSource(), getTarget());
    }

    /**
	 * @generated
	 */
    protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        if (!canExecute()) {
            throw new ExecutionException("Invalid arguments in create link command");
        }
        if (getSource() != null && getTarget() != null) {
            getSource().getESuperTypes().add(getTarget());
        }
        return CommandResult.newOKCommandResult();
    }

    /**
	 * @generated
	 */
    protected EClass getSource() {
        return (EClass) source;
    }

    /**
	 * @generated
	 */
    protected EClass getTarget() {
        return (EClass) target;
    }
}
