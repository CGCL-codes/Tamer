package com.kwoksys.action.issues;

import com.kwoksys.action.base.BaseAction;
import com.kwoksys.action.files.FileUploadForm;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.issues.IssueService;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.framework.system.AppPaths;
import com.kwoksys.framework.system.RequestContext;
import com.kwoksys.framework.util.HttpUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action class for issue-tracker/issue-file-add-2.jsp.
 */
public class IssueFileAdd2Action extends BaseAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RequestContext requestContext = new RequestContext(request);
        FileUploadForm actionForm = (FileUploadForm) form;
        Issue issue = new Issue();
        issue.setId(HttpUtils.getParameter(request, "issueId"));
        issue.setModifierId(requestContext.getUser().getId());
        IssueService issueService = ServiceProvider.getIssueService();
        ActionMessages errors = issueService.addIssueFile(requestContext, issue, actionForm);
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            request.setAttribute("issueId", issue.getId());
            return mapping.findForward("error");
        } else {
            return redirect(AppPaths.ISSUES_DETAIL + "?issueId=" + issue.getId());
        }
    }
}
