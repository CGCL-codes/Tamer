package ch.bbv.unittests.ejb3;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ch.bbv.application.Application;
import ch.bbv.application.ComponentException;
import ch.bbv.dog.DataObjectMgr;
import ch.bbv.dog.LightweightObjectHandlerImpl;
import ch.bbv.dog.persistence.hibernate.PersistenceHandlerHib;

public class ServerApplication extends Application {

    /**
	 * Returns the server application singleton instance. The application uses
	 * the locator pattern.
	 * 
	 * @return the server application unique instance
	 */
    public static ServerApplication getApplication() {
        return (ServerApplication) Application.getApplication();
    }

    /**
	 * Default constructor of the class.
	 * 
	 * @throws ComponentException
	 *             if an error occured creating the server application
	 */
    public ServerApplication() throws ComponentException {
        Log log = LogFactory.getLog(Application.class);
        log.info("Starting Java EE test server...");
        DataObjectMgr manager = null;
        register(this);
        manager = new DataObjectMgr(new PersistenceHandlerHib(), new LightweightObjectHandlerImpl(), null, EJBApplication.DATASOURCE);
        setDataObjectMgr(manager);
        ch.bbv.unittests.props.TypeFactory factory = new ch.bbv.unittests.props.TypeFactory(manager);
        factory.registerAllTypes();
        ch.bbv.unittests.indexedprops.TypeFactory ipFactory = new ch.bbv.unittests.indexedprops.TypeFactory(manager);
        ipFactory.registerAllTypes();
        initialize();
        startup();
        manager.getDataObjectHandler().openDatasource(EJBApplication.DATASOURCE, false);
        log.info("Java EE test server is running");
    }

    /**
	 * Entry point to the server application.
	 * 
	 * @param args arguments of the client application
	 */
    public static void main(String[] args) {
        try {
            new ServerApplication();
        } catch (ComponentException e) {
            e.printStackTrace();
        }
    }
}
