package sales.wicket.app.home;

import org.apache.wicket.markup.html.link.Link;
import org.modelibra.wicket.container.DmPage;
import sales.wicket.app.SalesApp;
import sales.wicket.app.chapter3.CheeseListPage;

public class HomePage extends DmPage {

    public HomePage() {
        final SalesApp salesApp = (SalesApp) getApplication();
        add(new Link("domainLink") {

            public void onClick() {
                setResponsePage(salesApp.getDomainPageClass());
            }
        });
        add(new Link("chapter3Link") {

            public void onClick() {
                setResponsePage(new CheeseListPage());
            }
        });
    }
}
