package com.centraview.administration.modulesettings.templates;

import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import com.centraview.common.CVUtility;
import com.centraview.settings.Settings;

/**
 * The execute method of this action class simply sets the list of PrintTemplate
 * types on the form bean and forwards to a blank template details page.
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class NewTemplateHandler extends Action {

    private static Logger logger = Logger.getLogger(NewTemplateHandler.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
        Collection templateCategories = null;
        try {
            templateCategories = (Collection) CVUtility.invokeEJBMethod("Printtemplate", "com.centraview.printtemplate.PrintTemplateHome", "getCategories", null, dataSource);
        } catch (Exception e) {
            logger.error("[execute] Exception thrown.", e);
            throw new ServletException(e);
        }
        ((DynaActionForm) form).set("typeList", templateCategories);
        TemplateUtil.setNavigation(request, "detail");
        return mapping.findForward(".view.administration.new_template");
    }
}
