package sales.wicket.app;

import org.apache.wicket.Application;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.modelibra.wicket.app.DomainApp;
import sales.PersistentSales;
import sales.Sales;
import sales.SalesConfig;
import sales.wicket.app.chapter3.CheeseListPage;

/**
 * Sales domain web application.
 * 
 * @author Dzenan Ridjanovic
 * @version 2009-01-05
 */
public class SalesApp extends DomainApp {

    /**
	 * Constructs the domain web application.
	 */
    public SalesApp() {
        super(new PersistentSales(new Sales(new SalesConfig().getDomainConfig())));
    }

    /**
	 * Gets the Sales domain.
	 * 
	 * @return Sales domain
	 */
    public Sales getSales() {
        return (Sales) getDomain();
    }

    public static SalesApp get() {
        return (SalesApp) Application.get();
    }

    @Override
    public Session newSession(Request request, Response response) {
        return new SalesAppSession(request);
    }

    @Override
    protected void init() {
        super.init();
        mountBookmarkablePage("/cheese", CheeseListPage.class);
    }
}
