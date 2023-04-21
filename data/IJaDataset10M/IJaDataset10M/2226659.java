package org.jalgo.module.dijkstra.util;

import org.jalgo.main.gui.JAlgoGUIConnector;

/**
 * @author Frank
 *
 */
public class DefaultExceptionHandler {

    public DefaultExceptionHandler(Exception e) {
        e.printStackTrace();
        if (e.getMessage() == null) return;
        JAlgoGUIConnector.getInstance().showErrorMessage(e.getMessage());
    }
}
