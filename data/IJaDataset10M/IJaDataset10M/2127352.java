package co.edu.unal.ungrid.grid.master;

import co.edu.unal.space.util.SpaceProxy;
import co.edu.unal.ungrid.client.worker.SpaceResult;

public interface ResultListener {

    void resultReceived(SpaceResult sr);

    void allResultsReceived(SpaceProxy proxy);
}
