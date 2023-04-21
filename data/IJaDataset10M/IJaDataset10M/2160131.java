package com.liferay.portal.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.liferay.portal.ejb.LayoutManagerUtil;
import com.liferay.portal.model.Layout;
import com.liferay.portal.util.Constants;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.CachePortlet;
import com.liferay.util.BrowserSniffer;
import com.liferay.util.ParamUtil;

/**
 * <a href="RemovePortletAction.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.9 $
 *
 */
public class RemovePortletAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) throws Exception {
        try {
            HttpSession ses = req.getSession();
            Layout layout = (Layout) req.getAttribute(WebKeys.LAYOUT);
            boolean refreshPage = (BrowserSniffer.is_ns_4(req) || layout.hasStateMax());
            if (!layout.isGroup()) {
                String portletId = ParamUtil.getString(req, "p_p_id");
                if (req.getRemoteUser() != null) {
                    layout.removePortletId(portletId);
                    LayoutManagerUtil.updateLayout(layout.getPrimaryKey(), layout.getName(), layout.getColumnOrder(), layout.getNarrow1(), layout.getNarrow2(), layout.getWide(), layout.getStateMax(), layout.getStateMin(), layout.getModeEdit(), layout.getModeHelp());
                    CachePortlet.clearResponse(ses, layout.getPrimaryKey(), portletId);
                }
            }
            if (!refreshPage) {
                return mapping.findForward(Constants.COMMON_NULL);
            } else {
                return mapping.findForward(Constants.COMMON_REFERER);
            }
        } catch (Exception e) {
            req.setAttribute(PageContext.EXCEPTION, e);
            return mapping.findForward(Constants.COMMON_ERROR);
        }
    }
}
