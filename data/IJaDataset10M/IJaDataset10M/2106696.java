package app.sentinel;

import system.core.Component;
import java.io.File;
import java.util.HashMap;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import remedium.Remedium;

/**
 *
 * @author Nuno Brito, 12th of June 2011 in Darmstadt, Germany.
 */
public class SnapshotComponent extends Component {

    HashMap<String, SnapshotTracker> snaps = new HashMap();

    public SnapshotComponent(Remedium assignedInstance, Component assignedFather) {
        super(assignedInstance, assignedFather);
    }

    /** Add a new snapshot object to our group */
    public void add(String folder) {
        File root = new File(folder);
    }

    @Override
    public void onStart() {
        log(INFO, "Snapshot service is ready");
    }

    @Override
    public void onRecover() {
    }

    @Override
    public void onLoop() {
    }

    @Override
    public void onStop() {
        log(INFO, "Stopping");
    }

    @Override
    public String getTitle() {
        return "snapshot";
    }

    public void doSnapshot() {
    }

    @Override
    public String doWebResponse(Request request, Response response) {
        return getTitle();
    }
}
