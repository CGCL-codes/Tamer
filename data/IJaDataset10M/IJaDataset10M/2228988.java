package org.esk.dablog.gwt.dablog.client;

import com.google.gwt.user.client.ui.TreeItem;

/**
 * This interface handles tree item expand/collapse actions in the forum engine
 * User: esk
 * Date: 06.01.2007
 * Time: 0:35:33
 * $Id:$
 */
public interface TreeItemHandler {

    /**
     * handles expand/collapse action
     * @param item
     */
    void handle(TreeItem item);
}
