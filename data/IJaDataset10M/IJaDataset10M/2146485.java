package se.mdh.mrtc.saveccm.assembly.diagram.edit.commands;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.notation.View;
import se.mdh.mrtc.saveccm.SaveccmPackage;

/**
 * @generated
 */
public class ComponentCreateCommand extends CreateElementCommand {

    /**
	 * @generated
	 */
    public ComponentCreateCommand(CreateElementRequest req) {
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
        return SaveccmPackage.eINSTANCE.getCollection();
    }
}
