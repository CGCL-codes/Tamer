package gov.nasa.jpf.network.listener;

import gov.nasa.jpf.network.cache.CacheLayer;
import gov.nasa.jpf.ListenerAdapter;
import gov.nasa.jpf.search.Search;

public class CacheNotifier extends ListenerAdapter {

    public void stateAdvanced(Search search) {
        int id = search.getStateNumber();
        CacheLayer.getInstance().changeState(id);
    }

    public void stateBacktracked(Search search) {
        int id = search.getStateNumber();
        CacheLayer.getInstance().changeState(id);
        CacheLayer.getInstance().backtrack(id);
    }

    public void stateRestored(Search search) {
        stateBacktracked(search);
    }

    public void searchFinished(Search search) {
        CacheLayer.getInstance().closePhysicalConnections();
    }
}
