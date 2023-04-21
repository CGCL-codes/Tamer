package agilehk.wineBar;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.Router;

public class FirstStepsApplication extends Application {

    public FirstStepsApplication(Context parentContext) {
        super(parentContext);
    }

    /**
    * Creates a root Restlet that will receive all incoming calls.
    */
    @Override
    public synchronized Restlet createRoot() {
        Router router = new Router(getContext());
        router.attachDefault(HelloWorldResource.class);
        return router;
    }
}
