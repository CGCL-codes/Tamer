package de.iritgo.aktera.dashboard;

import de.iritgo.aktera.ui.AbstractUIController;
import de.iritgo.aktera.ui.UIControllerException;
import de.iritgo.aktera.ui.UIRequest;
import de.iritgo.aktera.ui.UIResponse;

public class PromptDashboard extends AbstractUIController {

    /** The dashboard manager */
    private DashboardManager dashboardManager;

    private UIResponseVisitor uiResponseVisitor;

    /**
	 * The dashboard manager
	 *
	 * @param dashboardManager The manager
	 */
    public void setDashboardManager(DashboardManager dashboardManager) {
        this.dashboardManager = dashboardManager;
    }

    public void setUiResponseVisitor(UIResponseVisitor uiResponseVisitor) {
        this.uiResponseVisitor = uiResponseVisitor;
    }

    /**
	 * Execute the model.
	 *
	 **/
    public void execute(UIRequest request, UIResponse response) throws UIControllerException {
        uiResponseVisitor.init(response);
        for (DashboardGroup dashboardGroup : dashboardManager.getDashboardGroups()) {
            DashboardGroup freshDashboardGroup = dashboardGroup.newInstance();
            freshDashboardGroup.setLocale(request.getLocale());
            freshDashboardGroup.generate(uiResponseVisitor);
        }
    }
}
