package uk.ac.ebi.pride.gui.listener;

import java.util.EventListener;

/**
 * ToDo: move this into Desktop
 * User: rwang
 * Date: 21-Jan-2010
 * Time: 15:27:22
 */
public interface ExitListener extends EventListener {

    public boolean isReady();

    public void performExit();
}
