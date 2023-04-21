package org.colimas.web.actions.protect;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.colimas.web.actions.AbstractAction;

/**
 * <h3>StatisticsAction.java</h3>
 *
 * <P>
 * Function:<BR />
 * 
 * </P>
 * @author zhao lei
 * @version 1.0
 * <br>
 * Modification History:
 * <PRE>
 * SEQ DATE       ORDER DEVELOPER      DESCRIPTION
 * --- ---------- ----- -------------- -----------------------------
 * 001 2006/02/14          tyrone        INIT
 * </PRE>
 */
public class StatisticsAction extends AbstractAction {

    /**
	 *<p> </p>
	 * @see org.colimas.web.actions.AbstractAction#doAction(org.apache.struts.action.ActionForm)
	 */
    public ActionForward doAction(ActionForm form) {
        return mapping.findForward(this.forward);
    }

    /**
	 *<p> </p>
	 * @see java.security.PrivilegedAction#run()
	 */
    public Object run() {
        return null;
    }
}
