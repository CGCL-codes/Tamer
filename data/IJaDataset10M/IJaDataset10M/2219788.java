package tardistv.controller;

import java.util.EventListener;
import tardistv.model.ListShowDownloadedEvent;

/**
 *
 * @author TV Tardis Team
 */
public interface ListShowDownloadedListener extends EventListener {

    public void listShowsDownloaded(ListShowDownloadedEvent event);
}
