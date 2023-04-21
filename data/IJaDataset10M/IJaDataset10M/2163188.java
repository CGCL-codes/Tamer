package net.sf.tinyPayroll.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import net.sf.tinyPayroll.event.FatalException;
import net.sf.tinyPayroll.event.WarningException;

/**
 * Sets up a preferences framework for the application.
 */
public class AppPreferences {

    /** Properties file containing all the saved-off preferences for the application. */
    Properties properties = new Properties();

    /**
	 * Default constructor, just loads the application's preferences.
     * @throws FatalException if there is a problem loading the controller.
     *
	 */
    public AppPreferences() throws FatalException {
        load();
    }

    /**
	 * Loads up the default preferences file (creating it if it has to), and
	 * @throws FatalException - Any trouble in this block 
	 */
    private void load() throws FatalException {
        File prefFile = OSSpecific.getDefaultPreferencesFile();
        File appDir = OSSpecific.getDefaultAppDirectory();
        if (!appDir.exists() && !appDir.mkdir()) {
            throw new FatalException("Couldn't make the ~/.tinyPayroll folder: " + appDir.getAbsolutePath());
        }
        if (!prefFile.exists()) {
            try {
                if (!prefFile.createNewFile()) {
                    throw new FatalException("Couldn't create the default preferences file.");
                }
            } catch (IOException e) {
                throw new FatalException("Had trouble creating the default preferences file: " + e.getMessage());
            }
        }
        if (prefFile.canRead()) {
            try {
                FileInputStream inStream = new FileInputStream(prefFile);
                properties.load(inStream);
                inStream.close();
            } catch (FileNotFoundException e) {
                throw new FatalException("The default preferences file is not there, and I tried " + "really hard to make it for you: " + e.getMessage());
            } catch (IOException e) {
                throw new FatalException("The default preferences file is there, but I'm having " + "trouble reading it: " + e.getMessage());
            }
        } else {
            throw new FatalException("The default preferences file exists, but I can't read it " + "for some reason.");
        }
    }

    /**
	 * Saves off the proeprties file before the application shuts down.
     * @throws WarningException if there is a serious problem storing preferences.
	 */
    public void save() throws WarningException {
        try {
            FileOutputStream outStream = new FileOutputStream(OSSpecific.getDefaultPreferencesFile());
            properties.store(outStream, "Please don't mess with this file, it's important!");
        } catch (IOException e) {
            throw new WarningException("Couldn't save off the default preferences file.");
        }
    }
}
