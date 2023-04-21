package com.fundsmart.workplan.client.page;

import java.util.HashMap;
import java.util.Map;
import com.fundsmart.workplan.client.page.plan.AddOrEditPlanPage;
import com.fundsmart.workplan.client.page.plan.PlanListPage;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;

public class MenuBarPage extends BaseIncludedPage {

    private MenuBar rootBar;

    public void onLoad(Map<String, Object> parameters) {
        rootBar = new MenuBar();
        RootPanel.get().add(rootBar);
        MenuBar planBar = new MenuBar(true);
        rootBar.addItem(messages.labelPlan(), planBar);
        rootBar.addItem("About", new Command() {

            public void execute() {
            }
        });
        planBar.setAutoOpen(true);
        planBar.setAnimationEnabled(true);
        planBar.addItem(messages.labelAddPlan(), new Command() {

            public void execute() {
                getMainPage().gotoPage(new AddOrEditPlanPage(), new HashMap<String, Object>());
            }
        });
        planBar.addItem(messages.labelPlanList(), new Command() {

            public void execute() {
                getMainPage().gotoPage(new PlanListPage(), new HashMap<String, Object>());
            }
        });
    }

    public void onUnload() {
        RootPanel.get().remove(rootBar);
    }
}
