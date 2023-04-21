package org.eclipse.epsilon.fptc.system.diagram.edit.policies;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.epsilon.fptc.system.SystemPackage;
import org.eclipse.epsilon.fptc.system.diagram.edit.commands.BlockCreateCommand;
import org.eclipse.epsilon.fptc.system.diagram.providers.SystemElementTypes;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.commands.core.commands.DuplicateEObjectsCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest;

/**
 * @generated
 */
public class SystemItemSemanticEditPolicy extends SystemBaseItemSemanticEditPolicy {

    /**
	 * @generated
	 */
    protected Command getCreateCommand(CreateElementRequest req) {
        if (SystemElementTypes.Block_2002 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(SystemPackage.eINSTANCE.getSystem_Blocks());
            }
            return getGEFWrapper(new BlockCreateCommand(req));
        }
        return super.getCreateCommand(req);
    }

    /**
	 * @generated
	 */
    protected Command getDuplicateCommand(DuplicateElementsRequest req) {
        TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost()).getEditingDomain();
        return getGEFWrapper(new DuplicateAnythingCommand(editingDomain, req));
    }

    /**
	 * @generated
	 */
    private static class DuplicateAnythingCommand extends DuplicateEObjectsCommand {

        /**
		 * @generated
		 */
        public DuplicateAnythingCommand(TransactionalEditingDomain editingDomain, DuplicateElementsRequest req) {
            super(editingDomain, req.getLabel(), req.getElementsToBeDuplicated(), req.getAllDuplicatedElementsMap());
        }
    }
}
