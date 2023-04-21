package org.gwt.formlayout.showcase.client.tutorial;

import java.util.Arrays;
import org.gwt.formlayout.client.builder.DefaultFormBuilder;
import org.gwt.formlayout.client.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;

public class Builder5Item extends PanelDataListItem {

    public Builder5Item() {
        super("Building 5 - Default Form");
    }

    @Override
    public LayoutContainer getPanel() {
        FormLayout layout = new FormLayout("right:[40dlu,pref], 3dlu, 70dlu, 7dlu, " + "right:[40dlu,pref], 3dlu, 70dlu");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.appendSeparator("Flange");
        builder.append("Identifier:", new TextField());
        builder.nextLine();
        builder.append("PTI/kW:", new TextField());
        builder.append("Power/kW:", new TextField());
        builder.append("s/mm:", new TextField());
        builder.nextLine();
        builder.appendSeparator("Diameters");
        builder.append("da/mm:", new TextField());
        builder.append("di/mm:", new TextField());
        builder.append("da2/mm:", new TextField());
        builder.append("di2/mm:", new TextField());
        builder.append("R/mm:", new TextField());
        builder.append("D/mm:", new TextField());
        builder.appendSeparator("Criteria");
        SimpleComboBox<String> locationCombo = new SimpleComboBox<String>();
        locationCombo.add(Arrays.asList(new String[] { "Propeller nut thread", "Stern tube front area", "Shaft taper" }));
        builder.append("Location:", locationCombo);
        builder.append("k-factor:", new TextField());
        return builder.getPanel();
    }

    @Override
    public String getDescription() {
        return "Demonstrates how to fill a FormLayout using the DefaultFormBuilder.\n\n" + "Defines columns statically and rows dynamically, and appends components via the DefaultFormBuilder.";
    }
}
