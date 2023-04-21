package bmm.diagram.edit.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.commands.EditElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import bmm.OrganizationUnit;
import bmm.Strategy;
import bmm.diagram.edit.policies.BmmBaseItemSemanticEditPolicy;

/**
 * @generated
 */
public class OrganizationUnitDeterminedByCreateCommand extends EditElementCommand {

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
    public OrganizationUnitDeterminedByCreateCommand(CreateRelationshipRequest request, EObject source, EObject target) {
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
        if (source != null && false == source instanceof OrganizationUnit) {
            return false;
        }
        if (target != null && false == target instanceof Strategy) {
            return false;
        }
        if (getSource() == null) {
            return true;
        }
        return BmmBaseItemSemanticEditPolicy.LinkConstraints.canCreateOrganizationUnitDeterminedBy_4011(getSource(), getTarget());
    }

    /**
	 * @generated
	 */
    protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        if (!canExecute()) {
            throw new ExecutionException("Invalid arguments in create link command");
        }
        if (getSource() != null && getTarget() != null) {
            getSource().getDeterminedBy().add(getTarget());
        }
        return CommandResult.newOKCommandResult();
    }

    /**
	 * @generated
	 */
    protected OrganizationUnit getSource() {
        return (OrganizationUnit) source;
    }

    /**
	 * @generated
	 */
    protected Strategy getTarget() {
        return (Strategy) target;
    }
}
