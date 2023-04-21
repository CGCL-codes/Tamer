package wicket.helloworld;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import core.string.StringTool;

/**
 * @author manuelbarzi
 * @version 20111201170910
 */
public class HelloWorldPanel extends Panel {

    public HelloWorldPanel(String id) {
        this(id, null);
    }

    public HelloWorldPanel(String id, String message) {
        super(id);
        add(new Label("message", new Model<String>(StringTool.getInstance().ifNullGetEmpty(message))));
    }
}
