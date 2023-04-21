package es.gva.cit.gazetteer.drivers;

import java.awt.geom.Point2D;
import java.net.URI;
import es.gva.cit.catalog.drivers.DiscoveryServiceCapabilities;
import es.gva.cit.catalog.exceptions.NotSupportedProtocolException;
import es.gva.cit.catalog.ui.search.SearchAditionalPropertiesPanel;
import es.gva.cit.gazetteer.querys.Feature;
import es.gva.cit.gazetteer.querys.GazetteerQuery;

/**
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public class ExampleNewDriver extends AbstractGazetteerServiceDriver {

    public DiscoveryServiceCapabilities getCapabilities(URI uri) {
        return new GazetteerCapabilities();
    }

    public Feature[] getFeature(URI uri, GazetteerQuery query) throws Exception {
        String prop = (String) query.getProperty("PROP1");
        Feature[] features = new Feature[1];
        features[0] = new Feature("1", "Result 1", "description 1", new Point2D.Double(0, 0));
        return features;
    }

    public String getServiceName() {
        return "My service";
    }

    public SearchAditionalPropertiesPanel getAditionalSearchPanel() {
        return new ExampleNewPanel();
    }
}
