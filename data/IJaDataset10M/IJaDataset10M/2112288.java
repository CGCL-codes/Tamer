package jfb.examples.gmf.filesystem.diagram.edit.commands;

import jfb.examples.gmf.filesystem.Folder;
import jfb.examples.gmf.filesystem.diagram.edit.policies.FilesystemBaseItemSemanticEditPolicy;
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
public class FolderFoldersReorientCommand extends EditElementCommand {

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
    public FolderFoldersReorientCommand(ReorientReferenceRelationshipRequest request) {
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
        if (false == referenceOwner instanceof Folder) {
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
        if (!(oldEnd instanceof Folder && newEnd instanceof Folder)) {
            return false;
        }
        return FilesystemBaseItemSemanticEditPolicy.LinkConstraints.canExistFolderFolders_4002(getNewSource(), getOldTarget());
    }

    /**
	 * @generated
	 */
    protected boolean canReorientTarget() {
        if (!(oldEnd instanceof Folder && newEnd instanceof Folder)) {
            return false;
        }
        return FilesystemBaseItemSemanticEditPolicy.LinkConstraints.canExistFolderFolders_4002(getOldSource(), getNewTarget());
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
        getOldSource().getFolders().remove(getOldTarget());
        getNewSource().getFolders().add(getOldTarget());
        return CommandResult.newOKCommandResult(referenceOwner);
    }

    /**
	 * @generated
	 */
    protected CommandResult reorientTarget() throws ExecutionException {
        getOldSource().getFolders().remove(getOldTarget());
        getOldSource().getFolders().add(getNewTarget());
        return CommandResult.newOKCommandResult(referenceOwner);
    }

    /**
	 * @generated
	 */
    protected Folder getOldSource() {
        return (Folder) referenceOwner;
    }

    /**
	 * @generated
	 */
    protected Folder getNewSource() {
        return (Folder) newEnd;
    }

    /**
	 * @generated
	 */
    protected Folder getOldTarget() {
        return (Folder) oldEnd;
    }

    /**
	 * @generated
	 */
    protected Folder getNewTarget() {
        return (Folder) newEnd;
    }
}
