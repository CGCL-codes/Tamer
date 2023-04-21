package edu.uga.galileo.voci.model.listeners;

import edu.uga.galileo.voci.bo.Bundle;

/**
 * Defines the interface for those classes who wish to monitor bundle deletions.
 * 
 * @author <a href="mailto:mdurant@uga.edu">Mark Durant</a>
 * @version 1.0
 */
public interface BundleDeletionListener {

    /**
	 * Handle a bundle deletion event.
	 * 
	 * @param bundle
	 *            The bundle being deleted.
	 */
    public void handleBundleDeletion(Bundle bundle);
}
