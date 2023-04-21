package subget.threads.download.search;

import java.util.EventListener;

/**
 *
 * @author povder
 */
public interface SearchVideoFinishedEventListener extends EventListener {

    public void searchVideoFinished(SearchVideoFinishedEvent evt);
}
