package org.corrib.s3b.recommendations.plugin;

import java.util.SortedSet;
import org.corrib.s3b.recommendations.impl.Recommendation;
import org.ontoware.rdf2go.model.node.Resource;

/**
 * @author sebastiankruk
 * This class implements the recommendation plugin interface and is configurable through the interface.
 */
public class OverallRecommendationPlugin extends AbstractRecommendationPlugin {

    /**
	 * ID of the recommendation
	 */
    protected String id;

    public OverallRecommendationPlugin(String id_) {
        this.id = id_;
    }

    public SortedSet<Recommendation> findRecommendations(Resource resource) {
        return this.getResultsArray();
    }
}
