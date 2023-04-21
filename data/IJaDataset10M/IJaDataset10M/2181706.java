package org.gvsig.topology.ui.util;

/**
 * Interface callback for FeatureSelectionListener.
 * 
 * When FeatureSelectionListener select a feature, it calls
 * featureSelected method of this callback interface.
 * @author Alvaro Zabala
 *
 */
public interface FeatureSelectionCallBack {

    public void featureSelected();
}
