package ch.hsr.orm.model.diagram.edit.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import ch.hsr.orm.model.BiManyToMany;
import ch.hsr.orm.model.Diagram;
import ch.hsr.orm.model.Entity;
import ch.hsr.orm.model.ModelFactory;
import ch.hsr.orm.model.ModelPackage;
import ch.hsr.orm.model.Persistable;
import ch.hsr.orm.model.diagram.edit.policies.OrmmetaBaseItemSemanticEditPolicy;
import ch.hsr.orm.model.diagram.providers.OrmmetaElementTypes;

/**
 * @generated
 */
public class BiManyToManyCreateCommand extends CreateElementCommand {

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
    public BiManyToManyCreateCommand(CreateRelationshipRequest request, EObject source, EObject target) {
        super(request);
        this.source = source;
        this.target = target;
        if (request.getContainmentFeature() == null) {
            setContainmentFeature(ModelPackage.eINSTANCE.getDiagram_Relations());
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
        if (source != null && !(source instanceof Persistable)) {
            return false;
        }
        if (target != null && !(target instanceof Entity)) {
            return false;
        }
        if (getSource() == null) {
            return true;
        }
        if (getContainer() == null) {
            return false;
        }
        return OrmmetaBaseItemSemanticEditPolicy.LinkConstraints.canCreateBiManyToMany_3005(getContainer(), getSource(), getTarget());
    }

    /**
	 * @generated
	 */
    protected EObject doDefaultElementCreation() {
        BiManyToMany newElement = ModelFactory.eINSTANCE.createBiManyToMany();
        getContainer().getRelations().add(newElement);
        newElement.setOwner(getSource());
        newElement.setOwned(getTarget());
        OrmmetaElementTypes.Initializers.BiManyToMany_3005.init(newElement);
        return newElement;
    }

    /**
	 * @generated
	 */
    protected EClass getEClassToEdit() {
        return ModelPackage.eINSTANCE.getDiagram();
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
    protected Persistable getSource() {
        return (Persistable) source;
    }

    /**
	 * @generated
	 */
    protected Entity getTarget() {
        return (Entity) target;
    }

    /**
	 * @generated
	 */
    public Diagram getContainer() {
        return container;
    }
}
