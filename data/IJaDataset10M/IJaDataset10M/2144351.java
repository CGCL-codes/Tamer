package edu.cmu.sphinx.decoder;

import edu.cmu.sphinx.frontend.Feature;

/**
 *  The listener interface for being informed when a non-content
 *  feature is generated
 */
public interface FeatureListener {

    /**
     * Method called when a non-content feature is detected
     *
     * @param feature the non-content feature
     *
     */
    public void featureOccurred(Feature feature);
}
