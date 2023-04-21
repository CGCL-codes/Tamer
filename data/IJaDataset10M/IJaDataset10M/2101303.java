package com.sebulli.fakturama.office;

import ag.ion.bion.officelayer.application.IApplicationAssistant;
import ag.ion.bion.officelayer.application.ILazyApplicationInfo;
import ag.ion.bion.officelayer.application.OfficeApplicationException;
import ag.ion.bion.officelayer.internal.application.ApplicationAssistant;

/**
 * Get the home application in an extra thread.
 * So it can be canceled, if the NOA plugin couln't find it
 * in 10 seconds.
 * 
 * @author Gerd Bartelt
 */
public class OfficeHomeApplication implements Runnable {

    private String home = null;

    /**
	 * Get the result of the search
	 * 
	 * @return the path of the OpenOffice installation
	 */
    public String getHome() {
        return home;
    }

    /**
	 * Search for the OpenOfficeOrg home folder
	 * 
	 * @see java.lang.Runnable#run()
	 */
    public void run() {
        try {
            IApplicationAssistant applicationAssistant;
            applicationAssistant = new ApplicationAssistant();
            ILazyApplicationInfo appInfo = null;
            appInfo = applicationAssistant.getLatestLocalApplication();
            if (appInfo != null) {
                home = appInfo.getHome();
            }
        } catch (OfficeApplicationException e) {
        }
    }
}
