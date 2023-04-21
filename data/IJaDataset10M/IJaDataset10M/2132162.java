package org.icehockeymanager.ihm.clients.matchtest.controller;

import java.io.File;
import org.icehockeymanager.ihm.lib.IhmSettingsXML;

/**
 * ClientSettings contains all settings for the DevGui client.
 * 
 * @author Bernhard von Gunten
 * @created September, 2006
 */
public class ClientSettings extends IhmSettingsXML {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The LOGLEVEL. */
    public String LOGLEVEL = "CONFIG";

    /** The LOGLEVE l_ DOC. */
    public String LOGLEVEL_DOC = "Client Loglevel.";

    /**
   * Instantiates a new client settings.
   * 
   * @param fileToRead the file to read
   * 
   * @throws Exception the exception
   */
    public ClientSettings(File fileToRead) throws Exception {
        this.readSettingsFromXml(fileToRead);
    }

    /**
   * Instantiates a new client settings.
   */
    public ClientSettings() {
    }
}
