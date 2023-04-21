package org.geoforge.guitlc.dialog.panel.io;

import java.util.EventListener;
import org.geoforge.guitlc.dialog.edit.panel.StringChangedEvent;

/**
 *
 * @author Amadeus.Sowerby
 *
 * email: Amadeus.Sowerby_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 */
public interface IGfrIoPathListener extends EventListener {

    public void filePathChanged(StringChangedEvent e);
}
