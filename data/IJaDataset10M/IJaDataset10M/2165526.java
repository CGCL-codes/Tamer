package org.appspy.viewer.client.dashboards.action;

import org.appspy.viewer.client.ViewerModule;
import org.appspy.viewer.client.action.AbstractAction;
import org.appspy.viewer.client.dashboards.DashboardPanel;
import org.appspy.viewer.client.form.DashboardForm;
import org.appspy.viewer.client.paramchooser.ParamWindow;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.layout.FitLayout;

/**
 * @author Olivier HEDIN / olivier@appspy.org
 */
public class OpenDashboardAction extends AbstractAction {

    protected DashboardForm mDashboard = null;

    protected boolean mAskParams = true;

    /**
	 * @param viewerModule
	 */
    public OpenDashboardAction(ViewerModule viewerModule, DashboardForm dashboard, boolean askParams) {
        super(viewerModule);
        mDashboard = dashboard;
        mAskParams = askParams;
    }

    @Override
    public void execute() {
        final ParamWindow paramWindow = new ParamWindow(mViewerModule, mDashboard.getParams(), new AbstractAction(mViewerModule) {

            @Override
            public void execute() {
                TabPanel tabPanel = mViewerModule.getTabPanel();
                Panel p = new Panel();
                DashboardPanel c = new DashboardPanel(mViewerModule, mDashboard);
                p.setLayout(new FitLayout());
                p.setAutoScroll(true);
                p.setClosable(true);
                p.setTitle(mDashboard.getName());
                p.setIconCls("reports-menu-icon");
                p.add(c);
                try {
                    tabPanel.add(p);
                    tabPanel.activate(p.getId());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        paramWindow.setModal(true);
        if (mAskParams) {
            paramWindow.show();
        } else {
            paramWindow.executeAction();
            paramWindow.destroy();
        }
    }
}
