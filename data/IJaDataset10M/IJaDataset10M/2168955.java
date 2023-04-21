package com.avatal.struts.action.rights;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForward;
import org.apache.struts.validator.DynaValidatorForm;
import com.avatal.Constants;
import com.avatal.struts.action.AbstractAction;
import com.avatal.struts.base.AvatalActionMapping;
import com.avatal.view.rightprofile.RightProfileListView;
import com.avatal.view.rightprofile.RightProfileView;

/**
 * 
 * @author c. ferdinand
 * @date 18.09.2003
 *
 */
public class UpdateRightProfileAction extends AbstractAction {

    public ActionForward executeAction(AvatalActionMapping mapping, DynaValidatorForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer id = new Integer(Integer.parseInt(request.getParameter("rightProfileId")));
        HttpSession session = getSession(request);
        RightProfileListView rightProfileListView = (RightProfileListView) session.getAttribute(RightProfileListView.VIEW_NAME);
        RightProfileView newsView = (RightProfileView) rightProfileListView.getViewBeanById(RightProfileListView.RIGHT_PROFILE_LIST, RightProfileView.ID, id.toString());
        rightProfileListView.getViewBeanById(RightProfileListView.RIGHT_PROFILE_LIST, RightProfileView.ID, id.toString());
        addForNextPage(request, RightProfileView.VIEW_NAME, newsView);
        return mapping.findForward(Constants.SUCCESS_KEY);
    }
}
