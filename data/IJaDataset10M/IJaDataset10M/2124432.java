package org.apache.wicket.examples.compref;

import org.apache.wicket.IClusterable;
import org.apache.wicket.examples.WicketExamplePage;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;

/**
 * Page with examples on {@link org.apache.wicket.markup.html.form.TextArea}.
 * 
 * @author Eelco Hillenius
 */
public class CheckBoxPage extends WicketExamplePage {

    /**
	 * Constructor
	 */
    public CheckBoxPage() {
        final Input input = new Input();
        setModel(new CompoundPropertyModel(input));
        FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        add(feedbackPanel);
        Form form = new Form("form") {

            protected void onSubmit() {
                if (input.bool.booleanValue()) {
                    info("Ok, ok... we'll check it");
                } else {
                    info("So you don't want it checked huh?");
                }
            }
        };
        add(form);
        form.add(new CheckBox("bool"));
    }

    /** Simple data class that acts as a model for the input fields. */
    private static class Input implements IClusterable {

        /** a boolean. */
        public Boolean bool = Boolean.TRUE;

        /**
		 * @see java.lang.Object#toString()
		 */
        public String toString() {
            return "bool = '" + bool + "'";
        }
    }

    /**
	 * Override base method to provide an explanation
	 */
    protected void explain() {
        String html = "<input type=\"checkbox\" wicket:id=\"bool\" />";
        String code = "&nbsp;&nbsp;&nbsp;&nbsp;// add a check box component that uses the model object's 'bool' property.\n" + "&nbsp;&nbsp;&nbsp;&nbsp;form.add(new CheckBox(\"bool\"));";
        add(new ExplainPanel(html, code));
    }
}
