package com.liferay.portal.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.liferay.portal.ejb.LayoutManagerUtil;
import com.liferay.portal.model.Layout;
import com.liferay.portal.util.Constants;
import com.liferay.portal.util.WebKeys;
import com.liferay.util.ParamUtil;

/**
 * <a href="AddPortletAction.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.5 $
 *
 */
public class AddPortletAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) throws Exception {
        try {
            Layout layout = (Layout) req.getAttribute(WebKeys.LAYOUT);
            if (!layout.isGroup()) {
                String portletId = ParamUtil.getString(req, "p_p_id");
                String curColumnOrder = req.getParameter("p_l_cur_co");
                if (req.getRemoteUser() != null) {
                    layout.addPortletId(portletId, curColumnOrder);
                    LayoutManagerUtil.updateLayout(layout.getPrimaryKey(), layout.getName(), layout.getColumnOrder(), layout.getNarrow1(), layout.getNarrow2(), layout.getWide(), layout.getStateMax(), layout.getStateMin(), layout.getModeEdit(), layout.getModeHelp());
                }
            }
            return mapping.findForward(Constants.COMMON_REFERER_JSP);
        } catch (Exception e) {
            req.setAttribute(PageContext.EXCEPTION, e);
            return mapping.findForward(Constants.COMMON_ERROR);
        }
    }
}
