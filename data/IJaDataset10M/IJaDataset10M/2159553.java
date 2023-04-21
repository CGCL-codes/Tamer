package fr.fous.ecore.edit.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import fr.fous.ecore.edit.policies.EcoreBaseItemSemanticEditPolicy;
import fr.fous.ecore.providers.EcoreElementTypes;

/**
 * @generated
 */
public class EReference2CreateCommand extends CreateElementCommand {

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
    public EReference2CreateCommand(CreateRelationshipRequest request, EObject source, EObject target) {
        super(request);
        this.source = source;
        this.target = target;
        if (request.getContainmentFeature() == null) {
            setContainmentFeature(EcorePackage.eINSTANCE.getEClass_EStructuralFeatures());
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
        if (source != null && !(source instanceof EClass)) {
            return false;
        }
        if (target != null && !(target instanceof EClassifier)) {
            return false;
        }
        if (getSource() == null) {
            return true;
        }
        return EcoreBaseItemSemanticEditPolicy.LinkConstraints.canCreateEReference_3002(getSource(), getTarget());
    }

    /**
	 * @generated
	 */
    protected EObject doDefaultElementCreation() {
        EReference newElement = EcoreFactory.eINSTANCE.createEReference();
        getSource().getEStructuralFeatures().add(newElement);
        newElement.setEType(getTarget());
        EcoreElementTypes.Initializers.EReference_3002.init(newElement);
        return newElement;
    }

    /**
	 * @generated
	 */
    protected EClass getEClassToEdit() {
        return EcorePackage.eINSTANCE.getEClass();
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
    protected EClass getSource() {
        return (EClass) source;
    }

    /**
	 * @generated
	 */
    protected EClassifier getTarget() {
        return (EClassifier) target;
    }
}
