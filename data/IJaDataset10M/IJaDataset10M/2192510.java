package spc.gaius.actalis.fe;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.Router;
import commonj.sdo.DataGraph;

/**
 * The main Web application.
 *
 * @author Francesco Barcellini
 */
public class GaiusApplication extends Application {

    public GaiusApplication(Context parentContext) {
        super(parentContext);
    }

    @Override
    public Restlet createRoot() {
        Router router = new Router(getContext());
        router.attach("/ProvisioningGroup", GaiusGenericResource.class);
        router.attach("/ProvisioningAction", GaiusGenericResource.class);
        router.attach("/TargetSystem", GaiusGenericResource.class);
        router.attach("/Organization", GaiusGenericResource.class);
        router.attach("/UpdateRequestType", GaiusGetResources.class);
        router.attach("/Role", GaiusGenericResource.class);
        router.attach("/Profile", GaiusGenericResource.class);
        router.attach("/GaiusUser", GaiusGenericResource.class);
        return router;
    }
}
