package org.eclipse.epsilon.fptc.system.diagram.edit.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.epsilon.fptc.system.Block;
import org.eclipse.epsilon.fptc.system.diagram.edit.policies.SystemBaseItemSemanticEditPolicy;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.commands.EditElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;

/**
 * @generated
 */
public class BlockSuccessorsCreateCommand extends EditElementCommand {

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
    public BlockSuccessorsCreateCommand(CreateRelationshipRequest request, EObject source, EObject target) {
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
        if (source != null && false == source instanceof Block) {
            return false;
        }
        if (target != null && false == target instanceof Block) {
            return false;
        }
        if (getSource() == null) {
            return true;
        }
        return SystemBaseItemSemanticEditPolicy.LinkConstraints.canCreateBlockSuccessors_3001(getSource(), getTarget());
    }

    /**
	 * @generated
	 */
    protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        if (!canExecute()) {
            throw new ExecutionException("Invalid arguments in create link command");
        }
        if (getSource() != null && getTarget() != null) {
            getSource().getSuccessors().add(getTarget());
        }
        return CommandResult.newOKCommandResult();
    }

    /**
	 * @generated
	 */
    protected Block getSource() {
        return (Block) source;
    }

    /**
	 * @generated
	 */
    protected Block getTarget() {
        return (Block) target;
    }
}
