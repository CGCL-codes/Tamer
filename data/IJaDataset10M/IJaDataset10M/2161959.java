package wicketrocks.button;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import core.console.ConsoleTool;

/**
 * @author manuelbarzi
 * @version 20111201180314 
 */
public class ButtonPage extends WebPage {

    public ButtonPage() {
        WebMarkupContainer button = new WebMarkupContainer("button");
        button.setOutputMarkupId(true);
        button.add(new AjaxEventBehavior("onclick") {

            protected void onEvent(AjaxRequestTarget target) {
                ConsoleTool.getInstance().writeLine("ajax here!");
            }
        });
        add(button);
    }
}
