package org.njo.webapp.root.action.forum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.njo.webapp.root.model.activity.ForumActivity;

/**
 * TODO:comment
 *
 * @author yu.peng
 * @version 0.01
 */
public class TakeTopSubjectAction extends Action {

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
     *
     * @param mapping  The ActionMapping used to select this instance
     * @param form     The optional ActionForm bean for this request (if any)
     * @param request  The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @throws Exception if the application business logic throws
     *                   an exception
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ForumActivity forum = new ForumActivity();
        String id = request.getParameter("id");
        String board = request.getParameter("board");
        forum.takeTop(id);
        return (new ActionForward("/processor.listsubject.tiles?board=" + board, true));
    }
}
