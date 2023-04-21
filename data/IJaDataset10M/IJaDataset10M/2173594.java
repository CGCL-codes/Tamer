package com.tysanclan.site.projectewok.components;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import com.tysanclan.site.projectewok.entities.Bug;
import com.tysanclan.site.projectewok.entities.Bug.ReportType;

public abstract class CreateBugPanel extends Panel {

    private static final long serialVersionUID = 1L;

    public CreateBugPanel(String id, ReportType type) {
        super(id);
        add(new Label("title", "Create " + type.getDescription()));
        final TextField<String> titleField = new TextField<String>("titleField", new Model<String>(""));
        titleField.setRequired(true);
        final TextArea<String> descriptionArea = new TextArea<String>("descriptionArea", new Model<String>(""));
        descriptionArea.setRequired(true);
        Form<Bug> form = new Form<Bug>("form") {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                CreateBugPanel.this.onFormSubmit(titleField.getModelObject(), descriptionArea.getModelObject());
            }
        };
        form.add(titleField);
        form.add(descriptionArea);
        add(form);
    }

    public abstract void onFormSubmit(String title, String description);
}
