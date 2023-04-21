package cz.vse.gebz.diagram.edit.commands;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.notation.View;
import cz.vse.gebz.GebzPackage;
import cz.vse.gebz.KonstantniAtribut;
import cz.vse.gebz.Vaha;
import cz.vse.gebz.diagram.providers.BzElementTypes;

/**
 * @generated
 */
public class VahaCreateCommand extends CreateElementCommand {

    /**
	 * @generated
	 */
    public VahaCreateCommand(CreateElementRequest req) {
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
    public boolean canExecute() {
        KonstantniAtribut container = (KonstantniAtribut) getElementToEdit();
        if (container.getKonstanta() != null) {
            return false;
        }
        return true;
    }

    /**
	 * @generated
	 */
    protected EClass getEClassToEdit() {
        return GebzPackage.eINSTANCE.getKonstantniAtribut();
    }

    /**
	 * @generated
	 */
    protected EObject doDefaultElementCreation() {
        Vaha newElement = (Vaha) super.doDefaultElementCreation();
        if (newElement != null) {
            BzElementTypes.Initializers.Vaha_3025.init(newElement);
        }
        return newElement;
    }
}
