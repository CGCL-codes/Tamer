package com.kwoksys.action.contacts;

import com.kwoksys.action.base.BaseAction;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dto.CompanyBookmark;
import com.kwoksys.framework.system.AppPaths;
import com.kwoksys.framework.system.RequestContext;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action class for adding company bookmark.
 */
public class CompanyBookmarkAdd2Action extends BaseAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RequestContext requestContext = new RequestContext(request);
        AccessUser user = requestContext.getUser();
        ContactService contactService = ServiceProvider.getContactService();
        CompanyBookmarkForm actionForm = (CompanyBookmarkForm) form;
        Integer companyId = actionForm.getCompanyId();
        contactService.getCompany(companyId);
        CompanyBookmark bookmark = new CompanyBookmark(companyId);
        bookmark.setName(actionForm.getBookmarkName().trim());
        bookmark.setPath(actionForm.getBookmarkPath().trim());
        bookmark.setDescription("");
        bookmark.setCreatorId(user.getId());
        ActionMessages errors = contactService.addCompanyBookmark(requestContext, bookmark);
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("error");
        } else {
            contactService.resetCompanyBookmarkCount(companyId);
            return redirect(AppPaths.CONTACTS_COMPANY_BOOKMARK + "?companyId=" + companyId + "&bookmarkId=" + bookmark.getId());
        }
    }
}
