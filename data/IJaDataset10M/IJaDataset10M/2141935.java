package hub.metrik.lang.petri.debugger.edit.commands;

import hub.metrik.lang.petri.Place;
import hub.metrik.lang.petri.Transition;
import hub.metrik.lang.petri.debugger.edit.policies.PetriBaseItemSemanticEditPolicy;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.commands.EditElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientReferenceRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;

/**
 * @generated
 */
public class TransitionSnkReorientCommand extends EditElementCommand {

    /**
	 * @generated
	 */
    private final int reorientDirection;

    /**
	 * @generated
	 */
    private final EObject referenceOwner;

    /**
	 * @generated
	 */
    private final EObject oldEnd;

    /**
	 * @generated
	 */
    private final EObject newEnd;

    /**
	 * @generated
	 */
    public TransitionSnkReorientCommand(ReorientReferenceRelationshipRequest request) {
        super(request.getLabel(), null, request);
        reorientDirection = request.getDirection();
        referenceOwner = request.getReferenceOwner();
        oldEnd = request.getOldRelationshipEnd();
        newEnd = request.getNewRelationshipEnd();
    }

    /**
	 * @generated
	 */
    public boolean canExecute() {
        if (false == referenceOwner instanceof Transition) {
            return false;
        }
        if (reorientDirection == ReorientRelationshipRequest.REORIENT_SOURCE) {
            return canReorientSource();
        }
        if (reorientDirection == ReorientRelationshipRequest.REORIENT_TARGET) {
            return canReorientTarget();
        }
        return false;
    }

    /**
	 * @generated
	 */
    protected boolean canReorientSource() {
        if (!(oldEnd instanceof Place && newEnd instanceof Transition)) {
            return false;
        }
        return PetriBaseItemSemanticEditPolicy.LinkConstraints.canExistTransitionSnk_3001(getNewSource(), getOldTarget());
    }

    /**
	 * @generated
	 */
    protected boolean canReorientTarget() {
        if (!(oldEnd instanceof Place && newEnd instanceof Place)) {
            return false;
        }
        return PetriBaseItemSemanticEditPolicy.LinkConstraints.canExistTransitionSnk_3001(getOldSource(), getNewTarget());
    }

    /**
	 * @generated
	 */
    protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        if (!canExecute()) {
            throw new ExecutionException("Invalid arguments in reorient link command");
        }
        if (reorientDirection == ReorientRelationshipRequest.REORIENT_SOURCE) {
            return reorientSource();
        }
        if (reorientDirection == ReorientRelationshipRequest.REORIENT_TARGET) {
            return reorientTarget();
        }
        throw new IllegalStateException();
    }

    /**
	 * @generated
	 */
    protected CommandResult reorientSource() throws ExecutionException {
        getOldSource().getSnk().remove(getOldTarget());
        getNewSource().getSnk().add(getOldTarget());
        return CommandResult.newOKCommandResult(referenceOwner);
    }

    /**
	 * @generated
	 */
    protected CommandResult reorientTarget() throws ExecutionException {
        getOldSource().getSnk().remove(getOldTarget());
        getOldSource().getSnk().add(getNewTarget());
        return CommandResult.newOKCommandResult(referenceOwner);
    }

    /**
	 * @generated
	 */
    protected Transition getOldSource() {
        return (Transition) referenceOwner;
    }

    /**
	 * @generated
	 */
    protected Transition getNewSource() {
        return (Transition) newEnd;
    }

    /**
	 * @generated
	 */
    protected Place getOldTarget() {
        return (Place) oldEnd;
    }

    /**
	 * @generated
	 */
    protected Place getNewTarget() {
        return (Place) newEnd;
    }
}
