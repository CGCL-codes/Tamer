package com.alexmcchesney.poster.plugins.delicious;

import java.net.URI;
import java.net.URISyntaxException;
import com.alexmcchesney.poster.PosterApp;
import com.alexmcchesney.poster.os.OperatingSystem;
import com.alexmcchesney.poster.os.OperatingSystemException;

/**
 * Action called when the user wishes to launch one
 * or more links in the web 
 * @author amcchesney
 *
 */
public class LaunchAction {

    /**
	 * Actually performs the operation
	 * @param objects	Selected objects.  For each post, a browser
	 * will be launched.
	 */
    public static void doAction(Object[] objects) {
        OperatingSystem os = PosterApp.getOperatingSystem();
        for (Object obj : objects) {
            if (obj instanceof DeliciousPost) {
                String sURL = ((DeliciousPost) obj).getURL();
                if (sURL != null && sURL.length() > 0) {
                    try {
                        os.open(new URI(sURL));
                    } catch (OperatingSystemException opEx) {
                        PosterApp.showExceptionDialog(opEx);
                    } catch (URISyntaxException uriEx) {
                        PosterApp.showExceptionDialog(uriEx);
                    }
                }
            }
        }
    }
}
