package edu.asu.csid.irrigation.events;

import edu.asu.csid.event.AbstractEvent;
import edu.asu.csid.net.Identifier;

/**
 * @author Sanket
 *
 */
public class FileDownloadCompleteEvent extends AbstractEvent {

    private static final long serialVersionUID = 2566276891605754835L;

    public FileDownloadCompleteEvent(Identifier id) {
        super(id);
    }
}
