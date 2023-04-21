package de.derbsen.jkangoo.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import de.derbsen.jkangoo.view.ProjectFileView;
import de.derbsen.jkangoo.KangooException;

/**
 * Created by IntelliJ IDEA.
 * User: niels
 * Date: Aug 17, 2004
 * Time: 11:37:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class DeleteProjectFileAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            int x = Integer.parseInt(request.getParameter("x"));
            ProjectFileView file = new ProjectFileView();
            file.setId(x);
            file.loadFromDatabase();
            int projectId = file.getProjectId();
            file.deleteFromDatabase();
            file.deleteFiles();
            return new ActionForward("/showUploadProject.do?x=" + projectId);
        } catch (NumberFormatException e) {
            request.setAttribute("exception", e);
            return mapping.findForward("global.error");
        } catch (KangooException e) {
            request.setAttribute("exception", e);
            return mapping.findForward("global.error");
        }
    }
}
