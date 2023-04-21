package cz.vse.gebz.diagram.edit.commands;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.notation.View;
import cz.vse.gebz.GebzPackage;
import cz.vse.gebz.NominalniAtribut;
import cz.vse.gebz.diagram.providers.BzElementTypes;

/**
 * @generated
 */
public class NominalniAtribut3CreateCommand extends CreateElementCommand {

    /**
	 * @generated
	 */
    public NominalniAtribut3CreateCommand(CreateElementRequest req) {
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
        return GebzPackage.eINSTANCE.getBazeZnalosti();
    }

    /**
	 * @generated
	 */
    protected EObject doDefaultElementCreation() {
        NominalniAtribut newElement = (NominalniAtribut) super.doDefaultElementCreation();
        if (newElement != null) {
            BzElementTypes.Initializers.NominalniAtribut_3029.init(newElement);
        }
        return newElement;
    }
}
