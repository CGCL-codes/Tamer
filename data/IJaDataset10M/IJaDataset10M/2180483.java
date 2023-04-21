package org.bionote.webapp.action.page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.bionote.om.IComment;
import org.bionote.om.IPage;
import org.bionote.om.service.PageService;
import org.bionote.webapp.action.BaseStrutsAction;

/**
 * @author mbreese
 *  
 */
public class AddComment extends BaseStrutsAction {

    public ActionForward bionoteExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String source = getParameterString(request, "source");
        String subject = getParameterString(request, "subject");
        String username = getParameterString(request, "username");
        String sessionId = getParameterString(request, "sessionId");
        Long pageId = getParameterLong(request, "pageId");
        Integer replyId = getParameterInteger(request, "replyId");
        PageService pageService = (PageService) springContext.getBean("pageService");
        IPage page = pageService.findPage(pageId);
        IComment replyTo = null;
        if (replyId != null) {
            replyTo = pageService.findComment(page, replyId.intValue());
        }
        if (page != null && container.getSessionId().equals(sessionId)) {
            IComment comment = pageService.addComment(page, subject, source, container.getUser(), username, replyTo, request.getRemoteAddr());
            request.setAttribute("pageId", new Long(page.getId()));
            request.setAttribute("showcomments", Boolean.TRUE);
            request.setAttribute("showcomment", "comment_" + comment.getCommentId());
        }
        return mapping.findForward("Success");
    }
}
