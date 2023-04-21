package org.kineticsystem.commons.data.controller;

import java.util.EventListener;

/**
 * Any object interested in the operations of an editor must implement this
 * interface.
 * @author Giovanni Remigi
 * @version $Revision: 145 $
 * @see org.kineticsystem.commons.data.controller.DataNavigator
 */
public interface DataNavigatorListener extends EventListener {

    /**
     * This method is called when an editor changes its internal state.
     * @param event Object containing information about the current state of an
     *     editor.
     */
    public void editorStateChanged(DataNavigatorEvent event);
}
