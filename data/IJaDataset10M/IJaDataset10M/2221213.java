package fr.gfi.gfinet.web.struts.business.cvManagement.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class CvDisplayTabsAction extends Action {

    public static final String FORWARD_view = "view";

    public CvDisplayTabsAction() {
        super();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward(FORWARD_view);
    }
}
