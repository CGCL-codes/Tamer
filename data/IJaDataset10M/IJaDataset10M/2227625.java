package restlet.remote;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.Router;
import org.restlet.util.RouteList;

public class RESTletAdminApplication extends Application {

    private Router rootRouter;

    public RESTletAdminApplication(Context parentContext) {
        super(parentContext);
    }

    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createRoot() {
        rootRouter = new Router(getContext());
        return rootRouter;
    }

    public Router getRootRouter() {
        return rootRouter;
    }

    public void setRootRouter(Router rootRouter) {
        this.rootRouter = rootRouter;
    }
}
