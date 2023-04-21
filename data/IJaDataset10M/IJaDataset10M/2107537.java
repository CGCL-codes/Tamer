package org.authorsite.bib.web.struts.action;

import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.authorsite.bib.web.struts.form.*;

/**
 *
 * @author  jejking
 * @version $Revision: 1.1 $
 */
public class SelectIMRAction extends BibAbstractAction {

    /** Creates a new instance of SelectIMRAction */
    public SelectIMRAction() {
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("execute method of SelectIMRAction called");
        String intraMediaRelationship = ((IMRSelectForm) form).getIntraMediaRelationship();
        request.setAttribute("IMRSelectForm", form);
        return mapping.findForward("IMRSelected");
    }
}
