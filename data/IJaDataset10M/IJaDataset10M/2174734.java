package org.itracker.web.actions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.itracker.core.resources.ITrackerResources;
import org.itracker.services.ConfigurationService;
import org.itracker.web.actions.base.ItrackerBaseAction;
import org.itracker.web.util.Constants;

public class ShowHelpAction extends ItrackerBaseAction {

    private static final Logger log = Logger.getLogger(ShowHelpAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String helpPage = "";
        String helpParam = request.getParameter("page");
        HttpSession session = request.getSession(true);
        Locale locale = (Locale) session.getAttribute(Constants.LOCALE_KEY);
        log.debug("Requesting Help Page: " + helpParam);
        if ("ct".equals(helpParam)) {
            helpPage = ITrackerResources.getString("itracker.web.helppage.commontasks", locale);
        } else if ("ab".equals(helpParam)) {
            setupHelpAboutPageAttributes(request);
            helpPage = ITrackerResources.getString("itracker.web.helppage.about", locale);
        } else {
            helpPage = ITrackerResources.getString("itracker.web.helppage.index", locale);
        }
        log.debug("Redirecting to Help Page: " + helpPage);
        request.setAttribute("helpPage", helpPage);
        String pageTitleKey = "itracker.web.showhelp.title";
        String pageTitleArg = "";
        request.setAttribute("pageTitleKey", pageTitleKey);
        request.setAttribute("pageTitleArg", pageTitleArg);
        return mapping.findForward("show_help");
    }

    public ShowHelpAction() {
        super();
    }

    /**
     * This method will prepare the request attribute for the help about jsp .
     */
    private void setupHelpAboutPageAttributes(HttpServletRequest request) {
        ConfigurationService configurationService = getITrackerServices().getConfigurationService();
        long startTimeMillis = Long.parseLong(configurationService.getProperty("start_time_millis", ""));
        SimpleDateFormat dateFormat = new SimpleDateFormat(ITrackerResources.getString("itracker.dateformat.full"));
        String startTime = dateFormat.format(new Date(startTimeMillis));
        String versionNumber = configurationService.getProperty("version", "Unknown");
        request.setAttribute("starttime", startTime);
        request.setAttribute("version", versionNumber);
        request.setAttribute("javaVersion", System.getProperty("java.version"));
        request.setAttribute("javaVendor", System.getProperty("java.vendor"));
    }
}
