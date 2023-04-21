package net.sf.copernicus.cclient.model.diagram.edit.policies;

import net.sf.copernicus.cclient.model.ModelPackage;
import net.sf.copernicus.cclient.model.diagram.edit.commands.PropertyCreateCommand;
import net.sf.copernicus.cclient.model.diagram.providers.ModelElementTypes;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;

/**
 * @generated
 */
public class InstanceInstancePropertyCompartmentItemSemanticEditPolicy extends ModelBaseItemSemanticEditPolicy {

    /**
	 * @generated
	 */
    protected Command getCreateCommand(CreateElementRequest req) {
        if (ModelElementTypes.Property_2001 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(ModelPackage.eINSTANCE.getInstance_Properties());
            }
            return getGEFWrapper(new PropertyCreateCommand(req));
        }
        return super.getCreateCommand(req);
    }
}
