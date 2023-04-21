package de.derbsen.jkangoo.action;

import de.derbsen.jkangoo.KangooException;
import de.derbsen.jkangoo.form.CopyDependencyForm;
import de.derbsen.jkangoo.view.ReleaseView;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Niels
 * @version Created ${Date}
 */
public class CopyDependencyAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            CopyDependencyForm depform = (CopyDependencyForm) form;
            ReleaseView rv = new ReleaseView();
            rv.setId(depform.getReleaseId());
            rv.loadFromDatabase();
            rv.copyReleaseDependencies(depform.getSourceReleaseId());
            return new ActionForward("/showEditDependency.do?x=" + depform.getReleaseId());
        } catch (KangooException e) {
            request.setAttribute("exception", e);
            return mapping.findForward("global.error");
        }
    }
}
