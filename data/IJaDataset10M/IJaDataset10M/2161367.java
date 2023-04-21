package com.struts.action.friendlink;

import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import com.spring.service.friendlink.FriendLinkService;
import com.struts.form.ArticlesForm;
import com.struts.form.FriendLinkForm;

public class FriendLinkAction extends DispatchAction {

    private FriendLinkService friendLinkService = null;

    /**
	 * request friend link 
	 * @return
	 */
    public ActionForward friendLinkRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=GBK");
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FriendLinkForm friendlinkform = (FriendLinkForm) form;
        boolean bool = friendLinkService.saveFriendLink(friendlinkform);
        String path = mapping.findForward("success").getPath();
        try {
            if (bool) {
                response.getWriter().write("<script>parent.location.href='friendlink/redirectUrl_friendlink.html';alert('申请成功');</script>");
            } else {
                response.getWriter().write("<script>parent.location.href='friendlink/redirectUrl_friendlink.html';alert('申请失败');</script>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * query friend link list
	 * @return
	 */
    public ActionForward findFriendLink(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        int page = 0;
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        Map map = friendLinkService.findFriendLink(page);
        request.setAttribute("page_content", map.get("page_content"));
        request.setAttribute("friendlink_list", map.get("friendlink_list"));
        return mapping.findForward("friendlink_list");
    }

    /**
	 * delete friend link
	 * @return
	 */
    public ActionForward deleteFriendLink(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        int id = 0;
        if (request.getParameter("id") != null) {
            id = Integer.parseInt(request.getParameter("id"));
        }
        boolean bool = friendLinkService.deleteFriendLink(id);
        if (bool) {
            String path = mapping.findForward("success").getPath();
            ActionForward forward = new ActionForward(path + "&page=" + request.getParameter("page"));
            forward.setRedirect(true);
            return forward;
        } else {
            return mapping.findForward("fail");
        }
    }

    /**
	 * audit friend link
	 * @return
	 */
    public ActionForward updateFriendLinkAudit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        Byte audit = Byte.parseByte(request.getParameter("audit"));
        if (audit == 0) audit = 1; else audit = 0;
        FriendLinkForm friendlinkform = new FriendLinkForm();
        friendlinkform.setId(id);
        friendlinkform.setAudit(audit);
        if (friendLinkService.updateFriendLinkAudit(friendlinkform)) {
            String path = mapping.findForward("success").getPath();
            ActionForward forward = new ActionForward(path + "&page=" + request.getParameter("page"));
            forward.setRedirect(true);
            return forward;
        } else return mapping.findForward("fail");
    }

    /**
	 * delete Batch Message
	 * @return
	 */
    public ActionForward deleteBatchMessage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String ids = request.getParameter("ids");
        if (friendLinkService.deleteBatchMessage(ids)) {
            String path = mapping.findForward("success").getPath();
            ActionForward forward = new ActionForward(path + "&page=" + request.getParameter("page"));
            forward.setRedirect(true);
            return forward;
        } else {
            return mapping.findForward("fail");
        }
    }

    public FriendLinkService getFriendLinkService() {
        return friendLinkService;
    }

    public void setFriendLinkService(FriendLinkService friendLinkService) {
        this.friendLinkService = friendLinkService;
    }
}
