package org.apache.wicket.examples.compref;

import java.util.Arrays;
import java.util.List;
import org.apache.wicket.IClusterable;
import org.apache.wicket.examples.WicketExamplePage;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;

/**
 * Page with examples on {@link org.apache.wicket.markup.html.form.DropDownChoice}.
 * 
 * @author Eelco Hillenius
 */
public class DropDownChoicePage extends WicketExamplePage {

    /** available sites for selection. */
    private static final List SITES = Arrays.asList(new String[] { "The Server Side", "Java Lobby", "Java.Net" });

    /** available numbers for selection. */
    private static final List INTEGERS = Arrays.asList(new Integer[] { 1, 2, 3 });

    /**
	 * Constructor
	 */
    public DropDownChoicePage() {
        final Input input = new Input();
        setModel(new CompoundPropertyModel(input));
        FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        add(feedbackPanel);
        Form form = new Form("form") {

            protected void onSubmit() {
                info("input: " + input);
            }
        };
        add(form);
        form.add(new DropDownChoice("site", SITES));
        form.add(new DropDownChoice("integer", INTEGERS, new IChoiceRenderer() {

            /**
			 * Gets the display value that is visible to the end user.
			 * 
			 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getDisplayValue(java.lang.Object)
			 */
            public Object getDisplayValue(Object object) {
                String stringrep;
                int value = (Integer) object;
                switch(value) {
                    case 1:
                        stringrep = "One";
                        break;
                    case 2:
                        stringrep = "Two";
                        break;
                    case 3:
                        stringrep = "Three";
                        break;
                    default:
                        throw new IllegalStateException(value + " is not mapped!");
                }
                return stringrep;
            }

            /**
			 * Gets the value that is invisble to the end user, and that is used as the selection
			 * id.
			 * 
			 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getIdValue(java.lang.Object,
			 *      int)
			 */
            public String getIdValue(Object object, int index) {
                return String.valueOf(INTEGERS.get(index));
            }
        }));
    }

    /** Simple data class that acts as a model for the input fields. */
    private static class Input implements IClusterable {

        /** the selected site. */
        public String site;

        /** the selected integer. */
        public Integer integer = (Integer) INTEGERS.get(0);

        /**
		 * @see java.lang.Object#toString()
		 */
        public String toString() {
            return "site = '" + site + "', integer = " + integer;
        }
    }

    /**
	 * Override base method to provide an explanation
	 */
    protected void explain() {
        String html = "<select wicket:id=\"site\">\n" + "    <option>site 1</option>\n" + "    <option>site 2</option>\n" + "</select>\n" + "<select wicket:id=\"integer\">\n" + "    <option>Fifty</option>\n" + "    <option>Sixty</option>\n" + "</select>";
        String code = "/** available sites for selection. */\n" + "private static final List SITES = Arrays.asList(new String[] {\"The Server Side\", \"Java Lobby\", \"Java.Net\" });\n" + "/** available numbers for selection. */\n" + "private static final List INTEGERS = Arrays.asList(new Integer[] { new Integer(1), new Integer(2), new Integer(3) });\n" + "\n" + "public DropDownChoicePage() {\n" + "&nbsp;&nbsp;&nbsp;&nbsp;...\n" + "&nbsp;&nbsp;&nbsp;&nbsp;// Add a dropdown choice component that uses the model object's 'site' property to designate the\n" + "&nbsp;&nbsp;&nbsp;&nbsp;// current selection, and that uses the SITES list for the available options.\n" + "&nbsp;&nbsp;&nbsp;&nbsp;// Note that when the selection is null, Wicket will lookup a localized string to\n" + "&nbsp;&nbsp;&nbsp;&nbsp;// represent this null with key: \"id + '.null'\". In this case, this is 'site.null'\n" + "&nbsp;&nbsp;&nbsp;&nbsp;// which can be found in DropDownChoicePage.properties\n" + "&nbsp;&nbsp;&nbsp;&nbsp;form.add(new DropDownChoice(\"site\", SITES));\n" + "\n" + "&nbsp;&nbsp;&nbsp;&nbsp;// Allthough the default behavior of displaying the string representations of the choices\n" + "&nbsp;&nbsp;&nbsp;&nbsp;// by calling toString on the object might be alright in some cases, you usually want to have\n" + "&nbsp;&nbsp;&nbsp;&nbsp;// more control over it. You achieve this by providing an instance of IChoiceRenderer.\n" + "&nbsp;&nbsp;&nbsp;&nbsp;// Don't forget to check out the default implementation of IChoiceRenderer, ChoiceRenderer.\n" + "&nbsp;&nbsp;&nbsp;&nbsp;form.add(new DropDownChoice(\"integer\", INTEGERS, new IChoiceRenderer() {\n" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; // Gets the display value that is visible to the end user.\n" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;public String getDisplayValue(Object object) {\n" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;...\n" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}\n" + "\n" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; // Gets the value that is invisble to the end user, and that is used as the selection id.\n" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;public String getIdValue(Object object, int index) {\n" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;...\n" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}\n" + "&nbsp;&nbsp;&nbsp;&nbsp;}));\n" + "}";
        add(new ExplainPanel(html, code));
    }
}
