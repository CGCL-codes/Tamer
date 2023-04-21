package de.nordakademie.lejos.stateMachine.diagram.edit.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.commands.EditElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;

/**
 * @generated
 */
public class NamedEventTransitionToCreateCommand extends EditElementCommand {

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
    public NamedEventTransitionToCreateCommand(CreateRelationshipRequest request, EObject source, EObject target) {
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
        if (source != null && false == source instanceof de.nordakademie.lejos.stateMachine.NamedEvent) {
            return false;
        }
        if (target != null && false == target instanceof de.nordakademie.lejos.stateMachine.AbstractTargetState) {
            return false;
        }
        if (getSource() == null) {
            return true;
        }
        return de.nordakademie.lejos.stateMachine.diagram.edit.policies.StateMachineBaseItemSemanticEditPolicy.LinkConstraints.canCreateNamedEventTransitionTo_4001(getSource(), getTarget());
    }

    /**
	 * @generated
	 */
    protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        if (!canExecute()) {
            throw new ExecutionException("Invalid arguments in create link command");
        }
        if (getSource() != null && getTarget() != null) {
            getSource().setTransitionTo(getTarget());
        }
        return CommandResult.newOKCommandResult();
    }

    /**
	 * @generated
	 */
    protected de.nordakademie.lejos.stateMachine.NamedEvent getSource() {
        return (de.nordakademie.lejos.stateMachine.NamedEvent) source;
    }

    /**
	 * @generated
	 */
    protected de.nordakademie.lejos.stateMachine.AbstractTargetState getTarget() {
        return (de.nordakademie.lejos.stateMachine.AbstractTargetState) target;
    }
}
