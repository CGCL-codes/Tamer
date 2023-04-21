package irrigator;

import irrigator.pages.Index;
import irrigator.scheduling.IrrigationScheduler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wicket.ISessionFactory;
import wicket.Request;
import wicket.Session;
import wicket.protocol.http.WebApplication;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 * 
 * @see irrigator.pages.Start#main(String[])
 */
public class IrrigatorApplication extends WebApplication {

    /** Logging */
    private static final Log log = LogFactory.getLog(IrrigatorApplication.class);

    /**
     * Constructor
     */
    public IrrigatorApplication() {
        IrrigationScheduler.startScheduler();
    }

    /**
	 * @see wicket.Application#getHomePage()
	 */
    public Class<Index> getHomePage() {
        return Index.class;
    }

    /**
     * @see wicket.protocol.http.WebApplication#getSessionFactory()
     */
    public ISessionFactory getSessionFactory() {
        return new ISessionFactory() {

            public Session newSession(Request request) {
                return new IrrigatorSession(IrrigatorApplication.this);
            }
        };
    }
}
