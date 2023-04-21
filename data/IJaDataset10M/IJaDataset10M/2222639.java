package com.ctb.diagram.edit.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import com.ctb.CtbFactory;
import com.ctb.CtbPackage;
import com.ctb.Diagram;
import com.ctb.PatternConnection;
import com.ctb.PatternHub;
import com.ctb.PatternNode;
import com.ctb.diagram.edit.policies.CtbBaseItemSemanticEditPolicy;

/**
 * @generated
 */
public class PatternConnectionCreateCommand extends CreateElementCommand {

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
    private Diagram container;

    /**
	 * @generated
	 */
    public PatternConnectionCreateCommand(CreateRelationshipRequest request, EObject source, EObject target) {
        super(request);
        this.source = source;
        this.target = target;
        if (request.getContainmentFeature() == null) {
            setContainmentFeature(CtbPackage.eINSTANCE.getDiagram_PatternConnections());
        }
        for (EObject element = source; element != null; element = element.eContainer()) {
            if (element instanceof Diagram) {
                container = (Diagram) element;
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
        if (source != null && !(source instanceof PatternNode)) {
            return false;
        }
        if (target != null && !(target instanceof PatternHub)) {
            return false;
        }
        if (getSource() == null) {
            return true;
        }
        if (getContainer() == null) {
            return false;
        }
        return CtbBaseItemSemanticEditPolicy.LinkConstraints.canCreatePatternConnection_3002(getContainer(), getSource(), getTarget());
    }

    /**
	 * @generated
	 */
    protected EObject doDefaultElementCreation() {
        PatternConnection newElement = CtbFactory.eINSTANCE.createPatternConnection();
        getContainer().getPatternConnections().add(newElement);
        newElement.setSourcePatternNode(getSource());
        newElement.setTargetPatternHub(getTarget());
        return newElement;
    }

    /**
	 * @generated
	 */
    protected EClass getEClassToEdit() {
        return CtbPackage.eINSTANCE.getDiagram();
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
    protected PatternNode getSource() {
        return (PatternNode) source;
    }

    /**
	 * @generated
	 */
    protected PatternHub getTarget() {
        return (PatternHub) target;
    }

    /**
	 * @generated
	 */
    public Diagram getContainer() {
        return container;
    }
}
