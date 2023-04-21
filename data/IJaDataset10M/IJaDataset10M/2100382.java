package wicketrocks.border;

import org.apache.wicket.markup.html.WebPage;
import wicket.helloworld.HelloWorldPanel;

/**
 * @author manuelbarzi
 *
 */
public class BorderPage extends WebPage {

    public BorderPage() {
        RoundedBorder roundedBorder = new RoundedBorder("roundedBorder");
        add(roundedBorder);
        roundedBorder.getBodyContainer().add(new HelloWorldPanel("helloWorld"));
    }
}
