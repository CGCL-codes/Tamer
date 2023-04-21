package org.systemsbiology.apmlparser.v2;

import org.systemsbiology.apmlparser.v2.datatype.AlignedFeature;
import org.systemsbiology.apmlparser.v2.datatype.FeatureSource;

/**
 * Interface for listening to AlignedFeature events from APMLReader.
 * The default implementation will simply store everything.
 * Implementations that want to do different things, e.g., put everything in the database as it comes in and
 * throw away the in-memory objects, should implement this.
 */
public interface AlignmentListener {

    public void reportFeatureSource(FeatureSource featureSource);

    public void reportAlignedFeature(AlignedFeature alignedFeature);
}
