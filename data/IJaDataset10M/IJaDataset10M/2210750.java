package se.mdh.mrtc.saveccm.diagram.edit.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;

/**
 * @generated
 */
public class Model4ItemSemanticEditPolicy extends SaveccmBaseItemSemanticEditPolicy {

    /**
	 * @generated
	 */
    protected Command getDestroyElementCommand(DestroyElementRequest req) {
        CompoundCommand cc = getDestroyEdgesCommand();
        addDestroyShortcutsCommand(cc);
        cc.add(getGEFWrapper(new DestroyElementCommand(req)));
        return cc.unwrap();
    }
}
