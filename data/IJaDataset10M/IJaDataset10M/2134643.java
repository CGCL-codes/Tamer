package com.aelitis.azureus.plugins.startstoprules.defaultplugin;

import org.gudy.azureus2.plugins.download.Download;

/**
 * @author TuxPaper
 * @created Jun 11, 2007
 *
 */
public class StartStopRulesFPListener {

    /**
	 * This method should return true to force a download to be first priority. You can only use this
	 * listener to force downloads to be first priority - you can't force downloads <b>not</b> to be
	 * first priority - if you return false, then the other first priority settings and logic will be
	 * used to determine its status.
	 * 
	 * Listeners will not be called for all downloads - the following checks may prevent listeners
	 * being called:
	 *   - Non persistent downloads
	 *   - STOPPED or ERROR state
	 *   - Incomplete downloads
	 *   
	 * This means that listeners don't have to do these basic checks.
	 * 
	 * The StringBuffer argument is intended to output debug information about why the item
	 * is (or isn't) first priority. The item may be null if debugging is not enabled. It is
	 * not mandatory to log to the buffer. 
	 */
    public boolean isFirstPriority(Download download, int numSeeds, int numPeers, StringBuffer debug) {
        return false;
    }
}
