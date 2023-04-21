package se.mdh.mrtc.saveccm.diagram.edit.commands;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.notation.View;
import se.mdh.mrtc.saveccm.SaveccmPackage;

/**
 * @generated
 */
public class TriggerOut2CreateCommand extends CreateElementCommand {

    /**
	 * @generated
	 */
    public TriggerOut2CreateCommand(CreateElementRequest req) {
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
        return SaveccmPackage.eINSTANCE.getElement();
    }
}
