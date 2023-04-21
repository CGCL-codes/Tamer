package org.columba.mail.gui.table.model;

import java.util.EventListener;
import org.columba.mail.folder.IMailFolder;

/**
 * @author fdietz
 *
 */
public interface TableModelChangedListener extends EventListener {

    public void tableChanged(TableModelChangedEvent event);

    public boolean isInterestedIn(IMailFolder folder);
}
