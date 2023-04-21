package hub.metrik.lang.petri.debugger.edit.commands;

import hub.metrik.lang.petri.PetriPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class TransitionCreateCommand extends CreateElementCommand {

    /**
	 * @generated
	 */
    public TransitionCreateCommand(CreateElementRequest req) {
        super(req);
    }

    /**
	 * @generated
	 */
    protected EObject getElementToEdit() {
        EObject container = ((CreateElementRequest) getRequest()).getContainer();
        if (container instanceof View) {
            container = ((View) container).getElement();
        }
        return container;
    }

    /**
	 * @generated
	 */
    protected EClass getEClassToEdit() {
        return PetriPackage.eINSTANCE.getNet();
    }
}
