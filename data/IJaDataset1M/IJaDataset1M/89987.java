package webml.diagram.edit.policies;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.commands.core.commands.DuplicateEObjectsCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest;
import webml.diagram.edit.commands.AreaCreateCommand;
import webml.diagram.edit.commands.ContentUnitCreateCommand;
import webml.diagram.edit.commands.OperationUnitCreateCommand;
import webml.diagram.edit.commands.PageCreateCommand;
import webml.diagram.providers.WebmlElementTypes;

/**
 * @generated
 */
public class SiteviewItemSemanticEditPolicy extends WebmlBaseItemSemanticEditPolicy {

    /**
	 * @generated
	 */
    public SiteviewItemSemanticEditPolicy() {
        super(WebmlElementTypes.Siteview_1000);
    }

    /**
	 * @generated
	 */
    protected Command getCreateCommand(CreateElementRequest req) {
        if (WebmlElementTypes.Area_2001 == req.getElementType()) {
            return getGEFWrapper(new AreaCreateCommand(req));
        }
        if (WebmlElementTypes.Page_2002 == req.getElementType()) {
            return getGEFWrapper(new PageCreateCommand(req));
        }
        if (WebmlElementTypes.ContentUnit_2003 == req.getElementType()) {
            return getGEFWrapper(new ContentUnitCreateCommand(req));
        }
        if (WebmlElementTypes.OperationUnit_2004 == req.getElementType()) {
            return getGEFWrapper(new OperationUnitCreateCommand(req));
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
