package edu.psu.citeseerx.myciteseer.web.admin;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import edu.psu.citeseerx.myciteseer.domain.Account;
import edu.psu.citeseerx.myciteseer.domain.MCSConfiguration;
import edu.psu.citeseerx.myciteseer.domain.logic.MyCiteSeerFacade;
import edu.psu.citeseerx.myciteseer.web.utils.MCSUtils;

/**
 * Controller used to manipulate System configuration
 * @see org.springframework.web.servlet.mvc.Controller
 * @author Juan Pablo Fernandez Ramirez
 * @version $$Rev: 810 $$ $$Date: 2008-12-02 14:05:57 -0500 (Tue, 02 Dec 2008) $$
 */
public class EditConfigurationController implements Controller {

    private MyCiteSeerFacade myciteseer;

    /**
     * @param myciteseer
     */
    public void setMyCiteSeer(MyCiteSeerFacade myciteseer) {
        this.myciteseer = myciteseer;
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Account adminAccount = MCSUtils.getLoginAccount();
        if (!adminAccount.isAdmin()) {
            return new ModelAndView("admin/adminRequired", null);
        }
        MCSConfiguration editConfig = myciteseer.getConfiguration();
        String type = request.getParameter("type");
        if (type != null && type.equals("update")) {
            boolean newAccountsEnabled = ServletRequestUtils.getBooleanParameter(request, "setaccounts", false);
            boolean urlSubmissionEnabled = ServletRequestUtils.getBooleanParameter(request, "seturlsubmission", false);
            boolean correctionsEnabled = ServletRequestUtils.getBooleanParameter(request, "setcorrections", false);
            boolean groupsEnabled = ServletRequestUtils.getBooleanParameter(request, "setgroups", false);
            boolean peopleSearchEnabled = ServletRequestUtils.getBooleanParameter(request, "setpeoplesearch", false);
            boolean personalPortalEnabled = ServletRequestUtils.getBooleanParameter(request, "setpersonalportal", false);
            editConfig.setNewAccountsEnabled(newAccountsEnabled);
            editConfig.setUrlSubmissionsEnabled(urlSubmissionEnabled);
            editConfig.setCorrectionsEnabled(correctionsEnabled);
            editConfig.setGroupsEnabled(groupsEnabled);
            editConfig.setPeopleSearchEnabled(peopleSearchEnabled);
            editConfig.setPersonalPortalEnabled(personalPortalEnabled);
            myciteseer.saveConfiguration(editConfig);
        }
        HashMap<String, Boolean> model = new HashMap<String, Boolean>();
        model.put("setaccounts", editConfig.getNewAccountsEnabled());
        model.put("seturlsubmission", editConfig.getUrlSubmissionsEnabled());
        model.put("setcorrections", editConfig.getCorrectionsEnabled());
        model.put("setgroups", editConfig.getGroupsEnabled());
        model.put("setpeoplesearch", editConfig.getPeopleSearchEnabled());
        model.put("setpersonalportal", editConfig.getPersonalPortalEnabled());
        return new ModelAndView("admin/editConfiguration", model);
    }
}
