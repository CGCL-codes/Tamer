package com.ctb.diagram.edit.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.commands.EditElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;
import com.ctb.Diagram;
import com.ctb.PhysicalConnection;
import com.ctb.PhysicalConnector;
import com.ctb.diagram.edit.policies.CtbBaseItemSemanticEditPolicy;

/**
 * @generated
 */
public class PhysicalConnectionReorientCommand extends EditElementCommand {

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
    public PhysicalConnectionReorientCommand(ReorientRelationshipRequest request) {
        super(request.getLabel(), request.getRelationship(), request);
        reorientDirection = request.getDirection();
        oldEnd = request.getOldRelationshipEnd();
        newEnd = request.getNewRelationshipEnd();
    }

    /**
	 * @generated
	 */
    public boolean canExecute() {
        if (!(getElementToEdit() instanceof PhysicalConnection)) {
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
        if (!(oldEnd instanceof PhysicalConnector && newEnd instanceof PhysicalConnector)) {
            return false;
        }
        PhysicalConnector target = getLink().getTarget();
        if (!(getLink().eContainer() instanceof Diagram)) {
            return false;
        }
        Diagram container = (Diagram) getLink().eContainer();
        return CtbBaseItemSemanticEditPolicy.LinkConstraints.canExistPhysicalConnection_3002(container, getNewSource(), target);
    }

    /**
	 * @generated
	 */
    protected boolean canReorientTarget() {
        if (!(oldEnd instanceof PhysicalConnector && newEnd instanceof PhysicalConnector)) {
            return false;
        }
        PhysicalConnector source = getLink().getTarget();
        if (!(getLink().eContainer() instanceof Diagram)) {
            return false;
        }
        Diagram container = (Diagram) getLink().eContainer();
        return CtbBaseItemSemanticEditPolicy.LinkConstraints.canExistPhysicalConnection_3002(container, source, getNewTarget());
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
        getLink().setTarget(getNewSource());
        return CommandResult.newOKCommandResult(getLink());
    }

    /**
	 * @generated
	 */
    protected CommandResult reorientTarget() throws ExecutionException {
        getLink().setTarget(getNewTarget());
        return CommandResult.newOKCommandResult(getLink());
    }

    /**
	 * @generated
	 */
    protected PhysicalConnection getLink() {
        return (PhysicalConnection) getElementToEdit();
    }

    /**
	 * @generated
	 */
    protected PhysicalConnector getOldSource() {
        return (PhysicalConnector) oldEnd;
    }

    /**
	 * @generated
	 */
    protected PhysicalConnector getNewSource() {
        return (PhysicalConnector) newEnd;
    }

    /**
	 * @generated
	 */
    protected PhysicalConnector getOldTarget() {
        return (PhysicalConnector) oldEnd;
    }

    /**
	 * @generated
	 */
    protected PhysicalConnector getNewTarget() {
        return (PhysicalConnector) newEnd;
    }
}
