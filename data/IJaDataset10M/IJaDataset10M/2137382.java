package ch.nostromo.jchessclock.controller;

import java.io.File;
import ch.nostromo.lib.clientcontroller.ClientControllerSettings;
import ch.nostromo.lib.clientcontroller.ClientSettingsException;

public class JChessClockSettings extends ClientControllerSettings {

    private static final long serialVersionUID = 1L;

    public double MINUTES_LEFT = 10;

    public double MINUTES_RIGHT = 15;

    public boolean START_LEFT = true;

    public JChessClockSettings() {
        super("jchessclock", "jchessclock.log");
    }

    public JChessClockSettings(File fileToRead) throws ClientSettingsException {
        super("jchessclock", "jchessclock.log");
        this.readSettingsFromXml(fileToRead);
    }
}
