package fd2.diagram.edit.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.commands.EditElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import fd2.BaseFeatureNode;
import fd2.Fd2Factory;
import fd2.RequireRelation;
import fd2.diagram.edit.policies.Fd2BaseItemSemanticEditPolicy;

/**
 * @generated
 */
public class RequireRelationCreateCommand extends EditElementCommand {

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
    public RequireRelationCreateCommand(CreateRelationshipRequest request, EObject source, EObject target) {
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
        if (source != null && false == source instanceof BaseFeatureNode) {
            return false;
        }
        if (target != null && false == target instanceof BaseFeatureNode) {
            return false;
        }
        if (getSource() == null) {
            return true;
        }
        return Fd2BaseItemSemanticEditPolicy.LinkConstraints.canCreateRequireRelation_4006(getSource(), getTarget());
    }

    /**
	 * @generated
	 */
    protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        if (!canExecute()) {
            throw new ExecutionException("Invalid arguments in create link command");
        }
        RequireRelation newElement = Fd2Factory.eINSTANCE.createRequireRelation();
        getSource().getChildRelations().add(newElement);
        newElement.setTargetFeatureNode(getTarget());
        doConfigure(newElement, monitor, info);
        ((CreateElementRequest) getRequest()).setNewElement(newElement);
        return CommandResult.newOKCommandResult(newElement);
    }

    /**
	 * @generated
	 */
    protected void doConfigure(RequireRelation newElement, IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        IElementType elementType = ((CreateElementRequest) getRequest()).getElementType();
        ConfigureRequest configureRequest = new ConfigureRequest(getEditingDomain(), newElement, elementType);
        configureRequest.setClientContext(((CreateElementRequest) getRequest()).getClientContext());
        configureRequest.addParameters(getRequest().getParameters());
        configureRequest.setParameter(CreateRelationshipRequest.SOURCE, getSource());
        configureRequest.setParameter(CreateRelationshipRequest.TARGET, getTarget());
        ICommand configureCommand = elementType.getEditCommand(configureRequest);
        if (configureCommand != null && configureCommand.canExecute()) {
            configureCommand.execute(monitor, info);
        }
    }

    /**
	 * @generated
	 */
    protected void setElementToEdit(EObject element) {
        throw new UnsupportedOperationException();
    }

    /**
	 * @generated
	 */
    protected BaseFeatureNode getSource() {
        return (BaseFeatureNode) source;
    }

    /**
	 * @generated
	 */
    protected BaseFeatureNode getTarget() {
        return (BaseFeatureNode) target;
    }
}
