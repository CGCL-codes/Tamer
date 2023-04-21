package se.mdh.mrtc.saveccm.assembly.diagram.edit.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import se.mdh.mrtc.saveccm.SaveccmPackage;
import se.mdh.mrtc.saveccm.assembly.diagram.edit.commands.Attribut5CreateCommand;
import se.mdh.mrtc.saveccm.assembly.diagram.providers.SaveccmElementTypes;

/**
 * @generated
 */
public class ComponentComponentAttributsCompartmentItemSemanticEditPolicy extends SaveccmBaseItemSemanticEditPolicy {

    /**
	 * @generated
	 */
    protected Command getCreateCommand(CreateElementRequest req) {
        if (SaveccmElementTypes.Attribut_2044 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(SaveccmPackage.eINSTANCE.getElement_Specifie());
            }
            return getGEFWrapper(new Attribut5CreateCommand(req));
        }
        return super.getCreateCommand(req);
    }
}
