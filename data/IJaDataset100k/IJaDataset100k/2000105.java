package playground.gregor.shapeFileToMATSim;

import java.util.Collection;
import org.geotools.data.FeatureSource;
import org.geotools.feature.Feature;

public class PolygonGenerator {

    private Collection<Feature> graph;

    private FeatureSource featuresSource;

    public PolygonGenerator(Collection<Feature> graph, FeatureSource featureSource) {
        this.graph = graph;
        this.featuresSource = featureSource;
    }
}
