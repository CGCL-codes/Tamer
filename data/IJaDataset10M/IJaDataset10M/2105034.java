package cz.vse.gebz.diagram.edit.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.commands.EditElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;
import cz.vse.gebz.AtribalniVyrok;
import cz.vse.gebz.BazeZnalosti;
import cz.vse.gebz.LogickePravidlo;
import cz.vse.gebz.Vyrok;
import cz.vse.gebz.diagram.edit.policies.BzBaseItemSemanticEditPolicy;

/**
 * @generated
 */
public class LogickePravidloReorientCommand extends EditElementCommand {

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
    public LogickePravidloReorientCommand(ReorientRelationshipRequest request) {
        super(request.getLabel(), request.getRelationship(), request);
        reorientDirection = request.getDirection();
        oldEnd = request.getOldRelationshipEnd();
        newEnd = request.getNewRelationshipEnd();
    }

    /**
	 * @generated
	 */
    public boolean canExecute() {
        if (!(getElementToEdit() instanceof LogickePravidlo)) {
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
        if (!(oldEnd instanceof Vyrok && newEnd instanceof Vyrok)) {
            return false;
        }
        AtribalniVyrok target = getLink().getDusledek();
        if (!(getLink().eContainer() instanceof BazeZnalosti)) {
            return false;
        }
        BazeZnalosti container = (BazeZnalosti) getLink().eContainer();
        return BzBaseItemSemanticEditPolicy.LinkConstraints.canExistLogickePravidlo_4001(container, getNewSource(), target);
    }

    /**
	 * @generated
	 */
    protected boolean canReorientTarget() {
        if (!(oldEnd instanceof AtribalniVyrok && newEnd instanceof AtribalniVyrok)) {
            return false;
        }
        Vyrok source = getLink().getZdroj();
        if (!(getLink().eContainer() instanceof BazeZnalosti)) {
            return false;
        }
        BazeZnalosti container = (BazeZnalosti) getLink().eContainer();
        return BzBaseItemSemanticEditPolicy.LinkConstraints.canExistLogickePravidlo_4001(container, source, getNewTarget());
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
        getLink().setZdroj(getNewSource());
        return CommandResult.newOKCommandResult(getLink());
    }

    /**
	 * @generated
	 */
    protected CommandResult reorientTarget() throws ExecutionException {
        getLink().setDusledek(getNewTarget());
        return CommandResult.newOKCommandResult(getLink());
    }

    /**
	 * @generated
	 */
    protected LogickePravidlo getLink() {
        return (LogickePravidlo) getElementToEdit();
    }

    /**
	 * @generated
	 */
    protected Vyrok getOldSource() {
        return (Vyrok) oldEnd;
    }

    /**
	 * @generated
	 */
    protected Vyrok getNewSource() {
        return (Vyrok) newEnd;
    }

    /**
	 * @generated
	 */
    protected AtribalniVyrok getOldTarget() {
        return (AtribalniVyrok) oldEnd;
    }

    /**
	 * @generated
	 */
    protected AtribalniVyrok getNewTarget() {
        return (AtribalniVyrok) newEnd;
    }
}
