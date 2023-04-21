package subget.threads.download.search;

import java.util.EventListener;

/**
 *
 * @author povder
 */
public interface SearchDirExceptionEventListener extends EventListener {

    public void searchDirException(SearchDirExceptionEvent evt);
}
