package org.plazmaforge.studio.reportdesigner.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.plazmaforge.studio.reportdesigner.commands.ComponenetDeleteCommand;
import org.plazmaforge.studio.reportdesigner.model.IDesignContainer;
import org.plazmaforge.studio.reportdesigner.model.IDesignElement;
import org.plazmaforge.studio.reportdesigner.parts.ComponentEditPart;

/** 
 * @author Oleh Hapon
 * $Id: ReportComponentEditPolicy.java,v 1.3 2010/11/14 11:28:22 ohapon Exp $
 */
public class ReportComponentEditPolicy extends ComponentEditPolicy {

    protected Command createDeleteCommand(GroupRequest request) {
        ComponentEditPart tablePart = (ComponentEditPart) getHost();
        IDesignElement element = (IDesignElement) tablePart.getModel();
        IDesignContainer parent = (IDesignContainer) tablePart.getParent().getModel();
        return new ComponenetDeleteCommand(element, parent);
    }
}
