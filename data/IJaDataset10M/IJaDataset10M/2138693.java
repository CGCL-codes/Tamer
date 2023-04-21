package it.unisannio.rcost.callgraphanalyzer.diagram.edit.commands;

import it.unisannio.rcost.callgraphanalyzer.CallGraphFactory;
import it.unisannio.rcost.callgraphanalyzer.CallGraphPackage;
import it.unisannio.rcost.callgraphanalyzer.Dependence;
import it.unisannio.rcost.callgraphanalyzer.Node;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.policies.CallGraphBaseItemSemanticEditPolicy;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;

/**
 * @generated
 */
public class DependenceCreateCommand extends CreateElementCommand {

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
    private Node container;

    /**
	 * @generated
	 */
    public DependenceCreateCommand(CreateRelationshipRequest request, EObject source, EObject target) {
        super(request);
        this.source = source;
        this.target = target;
        if (request.getContainmentFeature() == null) {
            setContainmentFeature(CallGraphPackage.eINSTANCE.getNode_OutEdges());
        }
        for (EObject element = source; element != null; element = element.eContainer()) {
            if (element instanceof Node) {
                container = (Node) element;
                super.setElementToEdit(container);
                break;
            }
        }
    }

    /**
	 */
    public boolean canExecute() {
        return false;
    }

    /**
	 * @generated
	 */
    protected EObject doDefaultElementCreation() {
        Dependence newElement = CallGraphFactory.eINSTANCE.createDependence();
        getContainer().getOutEdgesList().add(newElement);
        newElement.setSource(getSource());
        newElement.setTarget(getTarget());
        return newElement;
    }

    /**
	 * @generated
	 */
    protected EClass getEClassToEdit() {
        return CallGraphPackage.eINSTANCE.getNode();
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
    protected Node getSource() {
        return (Node) source;
    }

    /**
	 * @generated
	 */
    protected Node getTarget() {
        return (Node) target;
    }

    /**
	 * @generated
	 */
    public Node getContainer() {
        return container;
    }
}
