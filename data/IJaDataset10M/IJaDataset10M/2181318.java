package org.isurf.dataintegrator.dim;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.isurf.dataintegrator.templates.TemplateDIM;

@SuppressWarnings("unchecked")
public class UnSubscribe extends TemplateDIM {

    private TextField idField;

    private Form unsubForm;

    public UnSubscribe(final PageParameters parameters) {
        super(parameters);
        final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        unsubForm = new UnsubForm("unsubForm");
        idField = new TextField("SubscriptionID", new Model(""));
        idField.setRequired(true);
        unsubForm.add(feedbackPanel);
        unsubForm.add(idField);
        add(unsubForm);
    }

    class UnsubForm extends Form {

        private static final long serialVersionUID = 1L;

        public UnsubForm(String id) {
            super(id);
        }

        @Override
        public void onSubmit() {
            if (unsubForm.isSubmitted()) {
                info("Form was successfully submitted.");
            }
            unsubForm.clearInput();
        }
    }

    @Override
    public String getPageTitle() {
        return "Unsubscribe";
    }
}
