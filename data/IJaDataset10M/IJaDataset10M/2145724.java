package wicketrocks.css;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.WebPage;
import wicket.header.HeaderTool;
import wicket.resource.ResourceTool;

/**
 * @author manuelbarzi
 * @version 20120229123320 
 */
public class CssPage extends WebPage {

    private static ResourceReference reference;

    public CssPage() {
        if (reference == null) {
            reference = ResourceTool.getInstance().createCssResourceReference(CssPage.class, "style.css", Boolean.TRUE);
        }
        add(HeaderTool.getInstance().createCssContributor(reference));
    }
}
