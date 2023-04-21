package hub.sam.mof.simulator.behaviour.diagram.part;

import java.util.Iterator;
import java.util.List;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.ui.actions.AbstractDeleteFromAction;
import org.eclipse.gmf.runtime.diagram.ui.commands.CommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

/**
 * @generated
 */
public class DeleteElementAction extends AbstractDeleteFromAction {

    /**
	 * @generated
	 */
    public DeleteElementAction(IWorkbenchPart part) {
        super(part);
    }

    /**
	 * @generated
	 */
    public DeleteElementAction(IWorkbenchPage workbenchPage) {
        super(workbenchPage);
    }

    /**
	 * @generated
	 */
    public void init() {
        super.init();
        setId(org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds.ACTION_DELETE_FROM_MODEL);
        setText(org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages.DiagramEditor_Delete_from_Model);
        setToolTipText(org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages.DiagramEditor_Delete_from_ModelToolTip);
        ISharedImages workbenchImages = PlatformUI.getWorkbench().getSharedImages();
        setHoverImageDescriptor(workbenchImages.getImageDescriptor(org.eclipse.ui.ISharedImages.IMG_TOOL_DELETE));
        setImageDescriptor(workbenchImages.getImageDescriptor(org.eclipse.ui.ISharedImages.IMG_TOOL_DELETE));
        setDisabledImageDescriptor(workbenchImages.getImageDescriptor(org.eclipse.ui.ISharedImages.IMG_TOOL_DELETE_DISABLED));
    }

    /**
	 * @generated
	 */
    protected String getCommandLabel() {
        return org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages.DiagramEditor_Delete_from_Model;
    }

    /**
	 * @generated
	 */
    protected Command getCommand(Request request) {
        List operationSet = getOperationSet();
        if (operationSet.isEmpty()) {
            return org.eclipse.gef.commands.UnexecutableCommand.INSTANCE;
        }
        Iterator editParts = operationSet.iterator();
        CompositeTransactionalCommand command = new CompositeTransactionalCommand(getEditingDomain(), getCommandLabel());
        while (editParts.hasNext()) {
            EditPart editPart = (EditPart) editParts.next();
            Command curCommand = editPart.getCommand(request);
            if (curCommand != null) {
                command.compose(new CommandProxy(curCommand));
            }
        }
        if (command.isEmpty() || command.size() != operationSet.size()) {
            return org.eclipse.gef.commands.UnexecutableCommand.INSTANCE;
        }
        return new ICommandProxy(command);
    }
}
