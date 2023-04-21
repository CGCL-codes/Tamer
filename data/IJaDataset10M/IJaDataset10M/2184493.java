package com.varaneckas.hawkscope.command;

import com.varaneckas.hawkscope.gui.WindowFactory;
import com.varaneckas.hawkscope.gui.settings.SettingsWindow;

/**
 * Settings {@link Command}
 * 
 * Displays Settings dialog
 *
 * @author Tomas Varaneckas
 * @version $Id: SettingsCommand.java 250 2009-02-17 13:10:44Z tomas.varaneckas $
 */
public class SettingsCommand implements Command {

    /**
     * Opens {@link SettingsWindow}
     */
    public void execute() {
        WindowFactory.getSettingsWindow().open();
    }
}
