package com.liferay.portlet.bookmarks.action;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.struts.ActionConstants;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.bookmarks.model.BookmarksEntry;
import com.liferay.portlet.bookmarks.service.BookmarksEntryServiceUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * <a href="OpenEntryAction.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class OpenEntryAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) throws Exception {
        try {
            long entryId = ParamUtil.getLong(req, "entryId");
            BookmarksEntry entry = BookmarksEntryServiceUtil.openEntry(entryId);
            req.setAttribute(WebKeys.FORWARD_URL, entry.getUrl());
            return mapping.findForward(ActionConstants.COMMON_FORWARD);
        } catch (Exception e) {
            req.setAttribute(PageContext.EXCEPTION, e);
            return mapping.findForward(ActionConstants.COMMON_ERROR);
        }
    }
}
