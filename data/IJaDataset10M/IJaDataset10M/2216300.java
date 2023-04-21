package org.plazmaforge.bsolution.payroll.client.swing.actions;

import org.plazmaforge.bsolution.payroll.client.swing.GUIPayrollEnvironment;
import org.plazmaforge.framework.client.swing.actions.ResourceAction;
import org.plazmaforge.framework.resources.Resources;

/**
 * @author Oleh Hapon
 * Date: 27.10.2004
 * Time: 8:47:29
 * $Id: GUIPayrollAction.java,v 1.2 2010/04/28 06:28:16 ohapon Exp $
 */
public abstract class GUIPayrollAction extends ResourceAction {

    public GUIPayrollAction() {
        super(GUIPayrollEnvironment.getResources());
    }

    public GUIPayrollAction(Resources resources) {
        super(resources == null ? GUIPayrollEnvironment.getResources() : resources);
    }
}
