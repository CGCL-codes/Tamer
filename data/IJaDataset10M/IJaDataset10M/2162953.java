package net.sf.xsnapshot.example.presentation.web.struts.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import net.sf.xsnapshot.example.business.LocationBean;
import net.sf.xsnapshot.example.data.FullLocationView;

/**
 * Action to handle viewing a user-selected location
 * 
 * @version $Revision: 17 $
 */
public final class LocationDetailsAction extends Action {

    private static final Logger s_log = Logger.getLogger(LocationDetailsAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        FullLocationView loc = LocationBean.getInstance().getLocation(new Long(id));
        request.setAttribute("location", loc);
        return mapping.findForward("success");
    }
}
