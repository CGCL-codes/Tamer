package org.scopemvc.controller.basic;

import org.scopemvc.core.Control;
import org.scopemvc.core.ControlException;
import org.scopemvc.core.Controller;

/**
 * Interface for actions that handle response to Controls.
 *
 * @author <A HREF="mailto:patrik_nordwall@yahoo.se">Patrik Nordwall</A>
 * @version $Revision: 1.2 $ $Date: 2002/09/23 13:51:15 $
 * @created 23 September 2002
 * @see org.scopemvc.controller.basic.BasicController
 */
public interface ControlAction {

    /**
     * Executes custom code for handling the control
     *
     * @param inControl The control that triggered the command
     * @param inController The controller where the command is executed
     * @throws ControlException when the control could not be handled
     */
    void execute(Control inControl, Controller inController) throws ControlException;
}
