package net.sourceforge.smarthomephone.action;

import java.util.List;
import java.util.Map;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import net.sourceforge.smarthomephone.dao.AutomationDao;
import net.sourceforge.smarthomephone.entities.Automation;

public class AutomationListAction extends ActionSupport {

    private static final long serialVersionUID = 781458971062809294L;

    private List<Automation> automationList;

    @SuppressWarnings("unchecked")
    public String showAutomationList() {
        Map session = ActionContext.getContext().getSession();
        if (ActionUtil.validateSession(session)) {
            AutomationDao dao = new AutomationDao();
            automationList = dao.getAllAutomation();
            return "success";
        } else {
            addActionError(getText("InvalidSession"));
            return "invalidSession";
        }
    }

    /**
     * Getter for automationList member
     *
     * @return the automationList
     */
    public List<Automation> getAutomationList() {
        return automationList;
    }

    /**
     * @param automationList the automationList to set
     */
    public void setAutomationList(List<Automation> automationList) {
        this.automationList = automationList;
    }
}
