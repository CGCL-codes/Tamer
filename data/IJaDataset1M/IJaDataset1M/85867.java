package com.hofstetter.diplthesis.ctb.ctb.diagram.edit.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import com.hofstetter.diplthesis.ctb.ctb.CtbFactory;
import com.hofstetter.diplthesis.ctb.ctb.CtbPackage;
import com.hofstetter.diplthesis.ctb.ctb.LocalRuntimeConnection;
import com.hofstetter.diplthesis.ctb.ctb.RuntimeComponent;
import com.hofstetter.diplthesis.ctb.ctb.diagram.edit.policies.CtbBaseItemSemanticEditPolicy;

/**
 * @generated
 */
public class LocalRuntimeConnectionCreateCommand extends CreateElementCommand {

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
    private RuntimeComponent container;

    /**
	 * @generated
	 */
    public LocalRuntimeConnectionCreateCommand(CreateRelationshipRequest request, EObject source, EObject target) {
        super(request);
        this.source = source;
        this.target = target;
        if (request.getContainmentFeature() == null) {
            setContainmentFeature(CtbPackage.eINSTANCE.getRuntimeComponent_RuntimeConnections());
        }
        for (EObject element = source; element != null; element = element.eContainer()) {
            if (element instanceof RuntimeComponent) {
                container = (RuntimeComponent) element;
                super.setElementToEdit(container);
                break;
            }
        }
    }

    /**
	 * @generated
	 */
    public boolean canExecute() {
        if (source == null && target == null) {
            return false;
        }
        if (source != null && !(source instanceof RuntimeComponent)) {
            return false;
        }
        if (target != null && !(target instanceof RuntimeComponent)) {
            return false;
        }
        if (getSource() == null) {
            return true;
        }
        if (getContainer() == null) {
            return false;
        }
        return CtbBaseItemSemanticEditPolicy.LinkConstraints.canCreateLocalRuntimeConnection_3002(getContainer(), getSource(), getTarget());
    }

    /**
	 * @generated
	 */
    protected EObject doDefaultElementCreation() {
        LocalRuntimeConnection newElement = CtbFactory.eINSTANCE.createLocalRuntimeConnection();
        getContainer().getRuntimeConnections().add(newElement);
        newElement.setSourceRuntimeComponent(getSource());
        newElement.setTargetRuntimeComponent(getTarget());
        return newElement;
    }

    /**
	 * @generated
	 */
    protected EClass getEClassToEdit() {
        return CtbPackage.eINSTANCE.getRuntimeComponent();
    }

    /**
	 * @generated
	 */
    protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        if (!canExecute()) {
            throw new ExecutionException("Invalid arguments in create link command");
        }
        return super.doExecuteWithResult(monitor, info);
    }

    /**
	 * @generated
	 */
    protected ConfigureRequest createConfigureRequest() {
        ConfigureRequest request = super.createConfigureRequest();
        request.setParameter(CreateRelationshipRequest.SOURCE, getSource());
        request.setParameter(CreateRelationshipRequest.TARGET, getTarget());
        return request;
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
    protected RuntimeComponent getSource() {
        return (RuntimeComponent) source;
    }

    /**
	 * @generated
	 */
    protected RuntimeComponent getTarget() {
        return (RuntimeComponent) target;
    }

    /**
	 * @generated
	 */
    public RuntimeComponent getContainer() {
        return container;
    }
}
