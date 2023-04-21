package fd2.diagram.edit.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.commands.EditElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;
import fd2.BaseFeatureNode;
import fd2.RequireRelation;
import fd2.diagram.edit.policies.Fd2BaseItemSemanticEditPolicy;

/**
 * @generated
 */
public class RequireRelationReorientCommand extends EditElementCommand {

    /**
	 * @generated
	 */
    private final int reorientDirection;

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
    public RequireRelationReorientCommand(ReorientRelationshipRequest request) {
        super(request.getLabel(), request.getRelationship(), request);
        reorientDirection = request.getDirection();
        oldEnd = request.getOldRelationshipEnd();
        newEnd = request.getNewRelationshipEnd();
    }

    /**
	 * @generated
	 */
    public boolean canExecute() {
        if (false == getElementToEdit() instanceof RequireRelation) {
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
        if (!(oldEnd instanceof BaseFeatureNode && newEnd instanceof BaseFeatureNode)) {
            return false;
        }
        BaseFeatureNode target = getLink().getTargetFeatureNode();
        return Fd2BaseItemSemanticEditPolicy.LinkConstraints.canExistRequireRelation_4006(getNewSource(), target);
    }

    /**
	 * @generated
	 */
    protected boolean canReorientTarget() {
        if (!(oldEnd instanceof BaseFeatureNode && newEnd instanceof BaseFeatureNode)) {
            return false;
        }
        if (!(getLink().eContainer() instanceof BaseFeatureNode)) {
            return false;
        }
        BaseFeatureNode source = (BaseFeatureNode) getLink().eContainer();
        return Fd2BaseItemSemanticEditPolicy.LinkConstraints.canExistRequireRelation_4006(source, getNewTarget());
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
        getOldSource().getChildRelations().remove(getLink());
        getNewSource().getChildRelations().add(getLink());
        return CommandResult.newOKCommandResult(getLink());
    }

    /**
	 * @generated
	 */
    protected CommandResult reorientTarget() throws ExecutionException {
        getLink().setTargetFeatureNode(getNewTarget());
        return CommandResult.newOKCommandResult(getLink());
    }

    /**
	 * @generated
	 */
    protected RequireRelation getLink() {
        return (RequireRelation) getElementToEdit();
    }

    /**
	 * @generated
	 */
    protected BaseFeatureNode getOldSource() {
        return (BaseFeatureNode) oldEnd;
    }

    /**
	 * @generated
	 */
    protected BaseFeatureNode getNewSource() {
        return (BaseFeatureNode) newEnd;
    }

    /**
	 * @generated
	 */
    protected BaseFeatureNode getOldTarget() {
        return (BaseFeatureNode) oldEnd;
    }

    /**
	 * @generated
	 */
    protected BaseFeatureNode getNewTarget() {
        return (BaseFeatureNode) newEnd;
    }
}
