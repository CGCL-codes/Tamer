package common.gui;

import common.log.Log;
import java.io.File;
import java.net.URI;
import javax.help.DefaultHelpBroker;
import javax.help.HelpSet;

/**
   This class provides simple access to the Java Help browser.
*/
public class HelpMain {

    /**
     Starts the browser.

     @param theArgs the file name of the help set.
  */
    public static void main(String[] theArgs) {
        if (theArgs.length != 1) {
            error("usage:\n  HelpMain help-set-file");
        }
        File helpSetFile = new File(theArgs[0]);
        if (!helpSetFile.exists()) {
            error("help set file not found: " + helpSetFile);
        } else if (!helpSetFile.isFile()) {
            error("help set file not a file: " + helpSetFile);
        }
        HelpSet hs = null;
        try {
            URI uri = helpSetFile.toURI();
            hs = new HelpSet(null, uri.toURL());
        } catch (Exception ee) {
            Log.main.println(Log.ERROR, "Can't find helpset: " + helpSetFile);
            hs = null;
        }
        if (hs != null) {
            DefaultHelpBroker helpBroker = (DefaultHelpBroker) hs.createHelpBroker();
            helpBroker.initPresentation();
            try {
                helpBroker.setCurrentID(hs.getHomeID());
                helpBroker.setDisplayed(true);
                while (true) {
                    try {
                        Thread.sleep(5000);
                        if (!helpBroker.isDisplayed()) {
                            break;
                        }
                    } catch (Exception e) {
                        break;
                    }
                }
            } catch (Exception e) {
                error(e.getMessage());
            }
            System.exit(0);
        }
    }

    private static void error(String theMessage) {
        Log.main.println(Log.ERROR, theMessage);
        System.exit(10);
    }
}
