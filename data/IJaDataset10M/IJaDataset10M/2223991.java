package org.adempiere.webui.desktop;

import java.io.Serializable;
import org.adempiere.webui.EnvWeb;
import org.adempiere.webui.component.Accordion;
import org.adempiere.webui.component.Tabpanel;
import org.adempiere.webui.component.ToolBarButton;
import org.adempiere.webui.dashboard.DPActivities;
import org.adempiere.webui.dashboard.DashboardPanel;
import org.adempiere.webui.dashboard.DashboardRunnable;
import org.adempiere.webui.event.MenuListener;
import org.adempiere.webui.panel.HeaderPanel;
import org.adempiere.webui.panel.SidePanel;
import org.adempiere.webui.util.IServerPushCallback;
import org.adempiere.webui.util.ServerPushTemplate;
import org.compiere.util.Msg;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zkex.zul.Center;
import org.zkoss.zkex.zul.North;
import org.zkoss.zkex.zul.West;
import org.zkoss.zkmax.zul.Portallayout;
import org.zkoss.zul.Div;

/**
 * @author hengsin 
 */
public class NavBar2Desktop extends TabbedDesktop implements MenuListener, Serializable, EventListener, IServerPushCallback {

    private static final String FAVOURITES_PATH = "/zul/favourites.zul";

    private static final String ACTIVITIES_PATH = "/zul/activities.zul";

    private static final long serialVersionUID = 9056511175189603883L;

    private Center windowArea;

    private Borderlayout layout;

    private Thread dashboardThread;

    private DashboardRunnable dashboardRunnable;

    private Accordion shortcutPanel;

    private int noOfNotice;

    private int noOfRequest;

    private int noOfWorkflow;

    public NavBar2Desktop() {
        super();
    }

    protected Component doCreatePart(Component parent) {
        SidePanel pnlSide = new SidePanel();
        HeaderPanel pnlHead = new HeaderPanel();
        pnlSide.getMenuPanel().addMenuListener(this);
        layout = new Borderlayout();
        if (parent != null) {
            layout.setParent(parent);
            layout.setWidth("100%");
            layout.setHeight("100%");
            layout.setStyle("position: absolute");
        } else layout.setPage(page);
        dashboardRunnable = new DashboardRunnable(layout.getDesktop(), this);
        North n = new North();
        layout.appendChild(n);
        n.setCollapsible(false);
        pnlHead.setParent(n);
        West w = new West();
        layout.appendChild(w);
        w.setWidth("300px");
        w.setCollapsible(true);
        w.setSplittable(true);
        w.setTitle("Menu");
        w.setFlex(true);
        pnlSide.setParent(w);
        w.setOpen(false);
        Center center = new Center();
        center.setParent(layout);
        center.setFlex(true);
        Borderlayout innerLayout = new Borderlayout();
        innerLayout.setHeight("100%");
        innerLayout.setWidth("100%");
        innerLayout.setParent(center);
        West innerW = new West();
        innerW.setWidth("200px");
        innerW.setCollapsible(true);
        innerW.setTitle("Navigation");
        innerW.setSplittable(true);
        innerW.setCollapsible(true);
        innerW.setParent(innerLayout);
        shortcutPanel = new Accordion();
        shortcutPanel.setWidth("100%");
        shortcutPanel.setHeight("100%");
        innerW.appendChild(shortcutPanel);
        Div div = new Div();
        Executions.createComponents(FAVOURITES_PATH, div, null);
        shortcutPanel.add(div, "Favourites");
        div = new Div();
        Component component = Executions.createComponents(ACTIVITIES_PATH, div, null);
        if (component instanceof DashboardPanel) {
            DashboardPanel dashboardPanel = (DashboardPanel) component;
            dashboardRunnable.add(dashboardPanel);
        }
        shortcutPanel.add(div, "Activities");
        shortcutPanel.setSelectedIndex(0);
        windowArea = new Center();
        windowArea.setParent(innerLayout);
        windowArea.setFlex(true);
        windowContainer.createPart(windowArea);
        createHomeTab();
        return layout;
    }

    private void createHomeTab() {
        Tabpanel homeTab = new Tabpanel();
        windowContainer.addWindow(homeTab, Msg.getMsg(EnvWeb.getCtx(), "Home").replaceAll("&", ""), false);
        Portallayout portalLayout = new Portallayout();
        portalLayout.setWidth("100%");
        portalLayout.setHeight("100%");
        portalLayout.setStyle("position: absolute; overflow: auto");
        homeTab.appendChild(portalLayout);
        registerWindow(homeTab);
        if (!portalLayout.getDesktop().isServerPushEnabled()) portalLayout.getDesktop().enableServerPush(true);
        dashboardRunnable.refreshDashboard();
        dashboardThread = new Thread(dashboardRunnable, "UpdateInfo");
        dashboardThread.setDaemon(true);
        dashboardThread.start();
    }

    public void onEvent(Event event) {
        Component comp = event.getTarget();
        String eventName = event.getName();
        if (eventName.equals(Events.ON_CLICK)) {
            if (comp instanceof ToolBarButton) {
                ToolBarButton btn = (ToolBarButton) comp;
                int menuId = 0;
                try {
                    menuId = Integer.valueOf(btn.getName());
                } catch (Exception e) {
                }
                if (menuId > 0) onMenuSelected(menuId);
            }
        }
    }

    public void onServerPush(ServerPushTemplate template) {
        noOfNotice = DPActivities.getNoticeCount();
        noOfRequest = DPActivities.getRequestCount();
        noOfWorkflow = DPActivities.getWorkflowCount();
        template.execute(this);
    }

    /**
	 * 
	 * @param page
	 */
    public void setPage(Page page) {
        if (this.page != page) {
            layout.setPage(page);
            this.page = page;
        }
    }

    /**
	 * Get the root component
	 * @return Component
	 */
    public Component getComponent() {
        return layout;
    }

    public void logout() {
        if (dashboardThread != null && dashboardThread.isAlive()) {
            dashboardRunnable.stop();
            dashboardThread.interrupt();
        }
    }

    public void updateUI() {
        int total = noOfNotice + noOfRequest + noOfWorkflow;
        shortcutPanel.setLabel(1, "Activities (" + total + ")");
        shortcutPanel.setTooltiptext(1, "Notice : " + noOfNotice + ", Request : " + noOfRequest + ", Workflow Activities : " + noOfWorkflow);
    }
}
