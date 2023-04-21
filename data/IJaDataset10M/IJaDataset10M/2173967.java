package se.mdh.mrtc.saveccm.diagram.edit.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import se.mdh.mrtc.saveccm.SaveccmPackage;
import se.mdh.mrtc.saveccm.diagram.edit.commands.Model5CreateCommand;
import se.mdh.mrtc.saveccm.diagram.providers.SaveccmElementTypes;

/**
 * @generated
 */
public class ComponentComponentModelsCompartmentItemSemanticEditPolicy extends SaveccmBaseItemSemanticEditPolicy {

    /**
	 * @generated
	 */
    protected Command getCreateCommand(CreateElementRequest req) {
        if (SaveccmElementTypes.Model_2043 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(SaveccmPackage.eINSTANCE.getElement_Behaviour());
            }
            return getGEFWrapper(new Model5CreateCommand(req));
        }
        return super.getCreateCommand(req);
    }
}
