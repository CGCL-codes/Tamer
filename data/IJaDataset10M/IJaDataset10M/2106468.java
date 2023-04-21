package se.mdh.mrtc.save.taEditor.diagram.edit.commands;

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
public class StandardEdgeCreateCommand extends CreateElementCommand {

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
    public StandardEdgeCreateCommand(CreateRelationshipRequest request, EObject source, EObject target) {
        super(request);
        this.source = source;
        this.target = target;
        if (request.getContainmentFeature() == null) {
            setContainmentFeature(se.mdh.mrtc.save.taEditor.TaEditorPackage.eINSTANCE.getSource_From());
        }
        super.setElementToEdit(source);
    }

    /**
	 * @generated
	 */
    public boolean canExecute() {
        if (source == null && target == null) {
            return false;
        }
        if (source != null && !(source instanceof se.mdh.mrtc.save.taEditor.Source)) {
            return false;
        }
        if (target != null && !(target instanceof se.mdh.mrtc.save.taEditor.AbstractNode)) {
            return false;
        }
        if (getSource() == null) {
            return true;
        }
        return se.mdh.mrtc.save.taEditor.diagram.edit.policies.TaEditorBaseItemSemanticEditPolicy.LinkConstraints.canCreateStandardEdge_3001(getSource(), getTarget());
    }

    /**
	 * @generated
	 */
    protected EObject doDefaultElementCreation() {
        se.mdh.mrtc.save.taEditor.StandardEdge newElement = se.mdh.mrtc.save.taEditor.TaEditorFactory.eINSTANCE.createStandardEdge();
        getSource().getFrom().add(newElement);
        newElement.setTo(getTarget());
        return newElement;
    }

    /**
	 * @generated
	 */
    protected EClass getEClassToEdit() {
        return se.mdh.mrtc.save.taEditor.TaEditorPackage.eINSTANCE.getSource();
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
    protected se.mdh.mrtc.save.taEditor.Source getSource() {
        return (se.mdh.mrtc.save.taEditor.Source) source;
    }

    /**
	 * @generated
	 */
    protected se.mdh.mrtc.save.taEditor.AbstractNode getTarget() {
        return (se.mdh.mrtc.save.taEditor.AbstractNode) target;
    }
}
