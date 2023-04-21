package org.parallelj.designer.edit.commands;

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
import org.parallelj.designer.edit.policies.ParallelJBaseItemSemanticEditPolicy;
import org.parallelj.model.Element;
import org.parallelj.model.Link;
import org.parallelj.model.ParallelJFactory;

/**
 * @generated
 */
public class LinkCreateCommand extends EditElementCommand {

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
    public LinkCreateCommand(CreateRelationshipRequest request, EObject source, EObject target) {
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
        if (source != null && false == source instanceof Element) {
            return false;
        }
        if (target != null && false == target instanceof Element) {
            return false;
        }
        if (getSource() == null) {
            return true;
        }
        return ParallelJBaseItemSemanticEditPolicy.getLinkConstraints().canCreateLink_4001(getSource(), getTarget());
    }

    /**
	 * @generated
	 */
    protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        if (!canExecute()) {
            throw new ExecutionException("Invalid arguments in create link command");
        }
        Link newElement = ParallelJFactory.eINSTANCE.createLink();
        getSource().getOutputLinks().add(newElement);
        newElement.setDestination(getTarget());
        doConfigure(newElement, monitor, info);
        ((CreateElementRequest) getRequest()).setNewElement(newElement);
        return CommandResult.newOKCommandResult(newElement);
    }

    /**
	 * @generated
	 */
    protected void doConfigure(Link newElement, IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
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
    protected Element getSource() {
        return (Element) source;
    }

    /**
	 * @generated
	 */
    protected Element getTarget() {
        return (Element) target;
    }
}
