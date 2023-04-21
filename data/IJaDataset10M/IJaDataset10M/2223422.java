package wicketrocks.field;

import org.apache.wicket.markup.html.WebPage;

/**
 * @author manuelbarzi
 * @version 20111201180314 
 */
public class FieldPage extends WebPage {

    public FieldPage() {
        add(new TextFieldPanel("textPanel"));
        add(new NumericFieldPanel("numericPanel"));
    }
}
