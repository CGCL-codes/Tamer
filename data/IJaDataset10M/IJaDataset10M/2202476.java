package org.codescale.eDependency.diagram.edit.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.EditElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;

/**
 * @generated
 */
public class RequireFeatureCreateCommand extends EditElementCommand {

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
    private final org.codescale.eDependency.Feature container;

    /**
     * @generated
     */
    public RequireFeatureCreateCommand(CreateRelationshipRequest request, EObject source, EObject target) {
        super(request.getLabel(), null, request);
        this.source = source;
        this.target = target;
        container = deduceContainer(source, target);
    }

    /**
     * @generated
     */
    public boolean canExecute() {
        if (source == null && target == null) {
            return false;
        }
        if (source != null && false == source instanceof org.codescale.eDependency.Feature) {
            return false;
        }
        if (target != null && false == target instanceof org.codescale.eDependency.Feature) {
            return false;
        }
        if (getSource() == null) {
            return true;
        }
        if (getContainer() == null) {
            return false;
        }
        return org.codescale.eDependency.diagram.edit.policies.EDependencyBaseItemSemanticEditPolicy.LinkConstraints.canCreateRequireFeature_3002(getContainer(), getSource(), getTarget());
    }

    /**
     * @generated
     */
    protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        if (!canExecute()) {
            throw new ExecutionException("Invalid arguments in create link command");
        }
        org.codescale.eDependency.RequireFeature newElement = org.codescale.eDependency.EDependencyFactory.eINSTANCE.createRequireFeature();
        getContainer().getRequireFeatureList().add(newElement);
        newElement.setSource(getSource());
        newElement.setTarget(getTarget());
        doConfigure(newElement, monitor, info);
        ((CreateElementRequest) getRequest()).setNewElement(newElement);
        return CommandResult.newOKCommandResult(newElement);
    }

    /**
     * @generated
     */
    protected void doConfigure(org.codescale.eDependency.RequireFeature newElement, IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
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
    protected org.codescale.eDependency.Feature getSource() {
        return (org.codescale.eDependency.Feature) source;
    }

    /**
     * @generated
     */
    protected org.codescale.eDependency.Feature getTarget() {
        return (org.codescale.eDependency.Feature) target;
    }

    /**
     * @generated
     */
    public org.codescale.eDependency.Feature getContainer() {
        return container;
    }

    /**
     * Default approach is to traverse ancestors of the source to find instance of container.
     * Modify with appropriate logic.
     * @generated
     */
    private static org.codescale.eDependency.Feature deduceContainer(EObject source, EObject target) {
        for (EObject element = source; element != null; element = element.eContainer()) {
            if (element instanceof org.codescale.eDependency.Feature) {
                return (org.codescale.eDependency.Feature) element;
            }
        }
        return null;
    }
}
