package com.citep.web.gwt.widgets;

import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public abstract class StepPanel extends VerticalPanel {

    protected Label title = new Label();

    protected Label description = new Label();

    public StepPanel() {
        super();
        HorizontalPanel holder = new HorizontalPanel();
        holder.add(title);
        holder.add(description);
        holder.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
        this.add(holder);
        this.setWidth("100%");
        this.setSpacing(5);
    }

    public void setTitle(String text) {
        title.setText(text);
    }

    public void setDescription(String text) {
        description.setText(text);
    }

    public void setEnabled(boolean enabled) {
        if (enabled == true) {
            title.setStyleName("operation_step_title_enabled");
            description.setStyleName("operation_step_description_enabled");
            this.setStyleName("operation_step_enabled");
        } else {
            title.setStyleName("operation_step_title_disabled");
            description.setStyleName("operation_step_description_disabled");
            this.setStyleName("operation_step_disabled");
        }
    }
}
