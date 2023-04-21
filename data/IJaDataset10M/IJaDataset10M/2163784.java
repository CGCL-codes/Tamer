package com.gite.application.chat;

import javax.swing.JComponent;
import com.gite.application.Toolable;

/**
 * LinkToolable is the interface to be implemented
 * by the link plugins
 * 
 * @author Cristian Manole
 * @version 1.0
 */
public interface LinkToolable extends Toolable {

    /**
     * This method is called once during application life cycle to allow
     * tool to initialize and show itself.
	 * @return The JComponent to be drawn 
	 */
    JComponent init();
}
