package com.simconomy.usermanagement.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.simconomy.usermanagement.gwt.client.wizards.CreateUserWizard;
import com.simconomy.widgets.client.wizards.impl.flow.FlowWizardWidget;

public class ChangePassword implements EntryPoint {

    public void onModuleLoad() {
        Label label = new Label();
        FlowWizardWidget newWizardWidget = new FlowWizardWidget(new CreateUserWizard());
        RootPanel.get("slot1").add(newWizardWidget.createWidget());
        RootPanel.get("slot2").add(label);
        RootPanel.get("email").add(new Label("ghj"));
    }
}
