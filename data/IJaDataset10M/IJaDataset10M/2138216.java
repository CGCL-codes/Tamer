package net.sf.jailer.ui.databrowser;

import java.io.File;
import net.sf.jailer.CommandLineParser;
import net.sf.jailer.Jailer;

/**
 * Holds information about the context, in which the data browser is used.
 * 
 * @author Ralf Wisser
 */
public class DataBrowserContext {

    private static String STANDALONE_APP_NAME = "DBeauty";

    private static String STANDALONE_APP_VERSION = "2.0.4";

    private static boolean supportsDataModelUpdates = true;

    private static Boolean standAlone = null;

    public static synchronized boolean isSupportsDataModelUpdates() {
        return supportsDataModelUpdates;
    }

    public static synchronized void setSupportsDataModelUpdates(boolean supportsDataModelUpdates) {
        DataBrowserContext.supportsDataModelUpdates = supportsDataModelUpdates;
    }

    public static synchronized boolean isStandAlone() {
        if (standAlone == null) {
            try {
                File samf = CommandLineParser.getInstance().newFile(".standalone");
                standAlone = samf.exists();
            } catch (Throwable e) {
                standAlone = false;
            }
        }
        return standAlone;
    }

    public static String getAppName(boolean shortName) {
        if (isStandAlone()) {
            return STANDALONE_APP_NAME + " " + STANDALONE_APP_VERSION + (shortName ? "" : " - Relational Data Browser");
        }
        return Jailer.APPLICATION_NAME + " " + Jailer.VERSION + (shortName ? "" : " - Relational Data Browser");
    }

    public static String getAppName() {
        if (isStandAlone()) {
            return STANDALONE_APP_NAME;
        }
        return Jailer.APPLICATION_NAME;
    }
}
