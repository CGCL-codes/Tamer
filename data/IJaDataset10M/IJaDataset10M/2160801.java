package org.apache.wicket.examples.portlet.menu;

import java.util.List;
import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import org.apache.wicket.RequestContext;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.portlet.PortletRequestContext;

/**
 * @author Ate Douma
 */
public class EditPage extends WebPage {

    private static final IChoiceRenderer exampleChoiceRenderer = new IChoiceRenderer() {

        /**
		 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getDisplayValue(java.lang.Object)
		 */
        public Object getDisplayValue(Object object) {
            return ((ExampleApplication) object).getDisplayName();
        }

        /**
		 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getIdValue(java.lang.Object, int)
		 */
        public String getIdValue(Object object, int index) {
            return Integer.toString(index);
        }
    };

    private DropDownChoice ddc;

    public EditPage() {
        Form form = new Form("form") {

            @Override
            protected void onSubmit() {
                ExampleApplication selected = (ExampleApplication) ddc.getModelObject();
                PortletRequestContext prc = (PortletRequestContext) RequestContext.get();
                PortletPreferences prefs = prc.getPortletRequest().getPreferences();
                prc.getPortletRequest().getPortletSession().setAttribute(WicketExamplesMenuPortlet.EXAMPLE_APPLICATION_ATTR, selected);
                try {
                    ((ActionResponse) prc.getPortletResponse()).setPortletMode(PortletMode.VIEW);
                    prefs.setValue(WicketExamplesMenuPortlet.EXAMPLE_APPLICATION_PREF, selected.getFilterPath());
                    prefs.store();
                } catch (Exception pe) {
                    throw new RuntimeException(pe);
                }
            }
        };
        List examples = WicketExamplesMenuApplication.getExamples();
        ddc = new DropDownChoice("examples", examples, exampleChoiceRenderer);
        ddc.setNullValid(false);
        PortletRequestContext prc = (PortletRequestContext) RequestContext.get();
        String eaFilterPath = prc.getPortletRequest().getPreferences().getValue(WicketExamplesMenuPortlet.EXAMPLE_APPLICATION_PREF, null);
        Model selected = new Model((ExampleApplication) examples.get(0));
        if (eaFilterPath != null) {
            for (int i = 0, size = examples.size(); i < size; i++) {
                if (((ExampleApplication) examples.get(i)).getFilterPath().equals(eaFilterPath)) {
                    selected.setObject((ExampleApplication) examples.get(i));
                    break;
                }
            }
        }
        ddc.setModel(selected);
        form.add(ddc);
        form.add(new Button("setButton"));
        add(form);
    }
}
