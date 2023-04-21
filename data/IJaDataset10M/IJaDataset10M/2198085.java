package org.qedeq.gui.se.control;

import org.qedeq.kernel.se.common.SourceFileException;

/**
 * Listener for warning selection events.
 *
 * @author  Michael Meyling
 */
public interface WarningSelectionListener {

    /**
     * This warning was selected.
     *
     * @param   number      Selected warning number. Starts with 0.
     * @param   sf          Selected warning.
     */
    public void selectWarning(int number, SourceFileException sf);
}
