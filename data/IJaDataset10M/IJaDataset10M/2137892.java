package net.sourceforge.seqware.queryengine.webservice.prototypes;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import net.sourceforge.seqware.queryengine.backend.model.Mismatch;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;
import org.restlet.security.Guard;

public class FirstStepsApplication extends Application {

    private final ConcurrentMap<String, Mismatch> mismatches = new ConcurrentHashMap<String, Mismatch>();

    /**
   * Creates a root Restlet that will receive all incoming calls.
   */
    @Override
    public synchronized Restlet createRoot() {
        Router router = new Router(getContext());
        Directory directory = new Directory(getContext(), "file:///home.local/boconnor/svnroot.local/solexatools/solexa-queryengine/webservice/html");
        router.attach("//queryengine/static/variants", directory);
        router.attach("//queryengine/realtime/variants/mismatches", MismatchesResource.class);
        router.attach("//queryengine/realtime/variants/mismatches/{mismatchId}", MismatchResource.class);
        router.attach("//queryengine/realtime/coverage/basecoverages", CoveragesResource.class);
        router.attach("//queryengine/realtime/coverage/basecoverages/{coverageId}", CoverageResource.class);
        return router;
    }

    public ConcurrentMap<String, Mismatch> getMismatches() {
        return mismatches;
    }
}
