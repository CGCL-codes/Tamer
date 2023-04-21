package org.germinus.telcoblocks.servicios.diagram.edit.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.germinus.telcoblocks.Enlace;
import org.germinus.telcoblocks.Nodo;
import org.germinus.telcoblocks.TelcoblocksFactory;
import org.germinus.telcoblocks.TelcoblocksPackage;
import org.germinus.telcoblocks.servicios.diagram.edit.policies.TelcoblocksBaseItemSemanticEditPolicy;

/**
 * @generated
 */
public class EnlaceCreateCommand extends CreateElementCommand {

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
    private org.germinus.telcoblocks.SERVICIOS container;

    /**
	 * @generated
	 */
    public EnlaceCreateCommand(CreateRelationshipRequest request, EObject source, EObject target) {
        super(request);
        this.source = source;
        this.target = target;
        if (request.getContainmentFeature() == null) {
            setContainmentFeature(TelcoblocksPackage.eINSTANCE.getSERVICIOS_Enlaces());
        }
        for (EObject element = source; element != null; element = element.eContainer()) {
            if (element instanceof org.germinus.telcoblocks.SERVICIOS) {
                container = (org.germinus.telcoblocks.SERVICIOS) element;
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
        if (source != null && false == source instanceof Nodo) {
            return false;
        }
        if (target != null && false == target instanceof Nodo) {
            return false;
        }
        if (getSource() == null) {
            return true;
        }
        if (getContainer() == null) {
            return false;
        }
        return TelcoblocksBaseItemSemanticEditPolicy.LinkConstraints.canCreateEnlace_4001(getContainer(), getSource(), getTarget());
    }

    /**
	 * @generated
	 */
    protected EObject doDefaultElementCreation() {
        Enlace newElement = TelcoblocksFactory.eINSTANCE.createEnlace();
        getContainer().getEnlaces().add(newElement);
        newElement.setOrigen(getSource());
        newElement.setDestino(getTarget());
        return newElement;
    }

    /**
	 * @generated
	 */
    protected EClass getEClassToEdit() {
        return TelcoblocksPackage.eINSTANCE.getSERVICIOS();
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
    protected Nodo getSource() {
        return (Nodo) source;
    }

    /**
	 * @generated
	 */
    protected Nodo getTarget() {
        return (Nodo) target;
    }

    /**
	 * @generated
	 */
    public org.germinus.telcoblocks.SERVICIOS getContainer() {
        return container;
    }
}
