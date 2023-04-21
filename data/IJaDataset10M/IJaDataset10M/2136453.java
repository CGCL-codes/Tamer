package org.apache.wicket.examples.compref;

import org.apache.wicket.examples.WicketExamplePage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

/**
 * Page with examples on {@link org.apache.wicket.markup.html.form.Button}.
 * 
 * @author Eelco Hillenius
 */
public class ButtonPage extends WicketExamplePage {

    /**
	 * Constructor
	 */
    public ButtonPage() {
        FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        add(feedbackPanel);
        Form form = new Form("form") {

            protected void onSubmit() {
                info("Form.onSubmit executed");
            }
        };
        Button button1 = new Button("button1") {

            public void onSubmit() {
                info("button1.onSubmit executed");
            }
        };
        form.add(button1);
        Button button2 = new Button("button2") {

            public void onSubmit() {
                info("button2.onSubmit executed");
            }
        };
        button2.setDefaultFormProcessing(false);
        form.add(button2);
        add(form);
    }

    /**
	 * Override base method to provide an explanation
	 */
    protected void explain() {
        String html = " <form wicket:id=\"form\">\n" + "  <input type=\"submit\" value=\"non wicket submit button\" />\n" + "  <input wicket:id=\"button1\" type=\"submit\" value=\"default wicket button\" />\n" + "  <input wicket:id=\"button2\" type=\"submit\" value=\"wicket button with setDefaultFormProcessing(false)\" />\n" + " </form>";
        String code = "&nbsp;&nbsp;&nbsp;&nbsp;// Add a form with an onSubmit implementation that sets a message\n" + "&nbsp;&nbsp;&nbsp;&nbsp;Form form = new Form(\"form\") {\n" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;protected void onSubmit() {\n" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;info(\"Form.onSubmit executed\");\n" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}\n" + "&nbsp;&nbsp;&nbsp;&nbsp;};\n" + "\n" + "&nbsp;&nbsp;&nbsp;&nbsp;Button button1 = new Button(\"button1\") {\n" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;protected void onSubmit() {\n" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;info(\"button1.onSubmit executed\");\n" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}\n" + "&nbsp;&nbsp;&nbsp;&nbsp;};\n" + "&nbsp;&nbsp;&nbsp;&nbsp;form.add(button1);\n" + "\n" + "&nbsp;&nbsp;&nbsp;&nbsp;Button button2 = new Button(\"button2\") {\n" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;protected void onSubmit() {\n" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;info(\"button2.onSubmit executed\");\n" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}\n" + "&nbsp;&nbsp;&nbsp;&nbsp;};\n" + "&nbsp;&nbsp;&nbsp;&nbsp;button2.setDefaultFormProcessing(false);\n" + "&nbsp;&nbsp;&nbsp;&nbsp;form.add(button2);\n" + "\n" + "&nbsp;&nbsp;&nbsp;&nbsp;add(form);";
        add(new ExplainPanel(html, code));
    }
}
