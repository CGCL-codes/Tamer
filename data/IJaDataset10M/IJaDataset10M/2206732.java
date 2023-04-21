package cz.vse.gebz.diagram.edit.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import cz.vse.gebz.GebzPackage;
import cz.vse.gebz.diagram.edit.commands.NominalniAtribalniVyrokCreateCommand;
import cz.vse.gebz.diagram.providers.BzElementTypes;

/**
 * @generated
 */
public class NominalniAtributNominalniAtributCompartmentItemSemanticEditPolicy extends BzBaseItemSemanticEditPolicy {

    /**
	 * @generated
	 */
    protected Command getCreateCommand(CreateElementRequest req) {
        if (BzElementTypes.NominalniAtribalniVyrok_3001 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(GebzPackage.eINSTANCE.getNominalniAtribut_NominalniVyroky());
            }
            return getGEFWrapper(new NominalniAtribalniVyrokCreateCommand(req));
        }
        return super.getCreateCommand(req);
    }
}
