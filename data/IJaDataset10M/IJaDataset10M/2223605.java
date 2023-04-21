package org.identifylife.key.editor.gwt.shared.core;

import java.util.List;
import java.util.Set;
import org.identifylife.key.editor.gwt.shared.model.Feature;
import org.identifylife.key.editor.gwt.shared.model.FeatureSet;
import org.identifylife.key.editor.gwt.shared.model.State;

/**
 * @author dbarnier
 *
 */
public interface FeatureStore {

    void addFeature(String parentId, Feature feature);

    void addFeatureSet(FeatureSet featureSet);

    Feature getFeature(String featureId);

    List<Feature> getFeatures();

    int getFeatureCount();

    FeatureSet getFeatureSet(String parentId);

    FeatureSet getFeatureSetWithFeatureId(String featureId);

    State getState(String stateId);

    Set<State> getStates();

    int getStateCount();

    boolean hasFeature(String featureId);

    boolean hasState(String stateId);
}
