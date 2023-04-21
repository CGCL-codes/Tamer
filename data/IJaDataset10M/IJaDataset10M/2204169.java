package org.spbu.pldoctoolkit.graph.diagram.infproduct.edit.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.spbu.pldoctoolkit.graph.command.diagram.DrlElementDestroyCommand;

/**
 * @generated
 */
public class InfElemRef2ItemSemanticEditPolicy extends DrlModelBaseItemSemanticEditPolicy {

    /**
	 * @generated
	 */
    protected Command getDestroyElementCommand(DestroyElementRequest req) {
        return getMSLWrapper(new DrlElementDestroyCommand(req));
    }
}
