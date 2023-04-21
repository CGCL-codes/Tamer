package com.fundsmart.workplan.client.page.plan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fundsmart.workplan.client.callback.AsyncDefaultCallback;
import com.fundsmart.workplan.client.page.BaseIncludedPage;
import com.fundsmart.workplan.client.page.BaseMainPage;
import com.fundsmart.workplan.client.page.MenuBarPage;
import com.fundsmart.workplan.pojo.Plan;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

public class AddOrEditPlanPage extends BaseMainPage {

    private Grid addPlanGrid = new Grid(4, 2);

    private TextBox name = new TextBox();

    private TextArea description = new TextArea();

    private Button save = new Button(messages.labelSave());

    private List<BaseIncludedPage> includedPage = new ArrayList<BaseIncludedPage>();

    public List<BaseIncludedPage> getIncludedPage() {
        return includedPage;
    }

    public AddOrEditPlanPage() {
        this.includedPage.add(new MenuBarPage());
    }

    public void onLoad(Map<String, Object> parameters) {
        addPlanGrid.setText(0, 0, messages.labelPlanAddPlanDesc());
        addPlanGrid.setText(1, 0, messages.labelPlanName() + ":");
        addPlanGrid.setWidget(1, 1, name);
        addPlanGrid.setText(2, 0, messages.labelPlanDescription() + ":");
        addPlanGrid.setWidget(2, 1, description);
        addPlanGrid.setWidget(3, 0, save);
        save.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                Plan plan = new Plan();
                plan.setName(name.getText());
                plan.setDescription(description.getText());
                workspaceService.addPlan(plan, new AsyncDefaultCallback<Void>(getInstance(), new PlanListPage(), new HashMap<String, Object>()));
            }
        });
        RootPanel.get().add(addPlanGrid);
    }

    public void onUnload() {
        RootPanel.get().remove(addPlanGrid);
    }
}
