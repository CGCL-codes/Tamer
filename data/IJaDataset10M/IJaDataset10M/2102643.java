package at.ac.ait.enviro.dssos.util.digester;

import at.ac.ait.enviro.dssos.container.Feature;
import at.ac.ait.enviro.dssos.util.Parser;
import at.ac.ait.enviro.dssos.util.ParserFactory;
import com.vividsolutions.jts.geom.Geometry;
import org.apache.commons.digester.Rule;
import org.xml.sax.Attributes;

/**
 * Digester {@link Rule} for parsing the coordinates information the SOS GetFeatureOfInterest response.
 * Must be used with the {@link CreateFOIRule}.
 * @author Arndt Bonitz
 */
public class FOICoordsRule extends Rule {

    private final boolean isEasting;

    /**
     * Creates a new FOICoordsRule
     * @param gmlNamespace
     *      GML namespace URI
     */
    public FOICoordsRule(String gmlNamespace, boolean isEasting) {
        super.namespaceURI = gmlNamespace;
        this.isEasting = isEasting;
    }

    @Override
    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        super.begin(namespace, name, attributes);
        final String srsName = attributes.getValue("srsName");
        if (digester.peek() instanceof Feature) {
            ((Feature) digester.peek()).setSrsName(srsName);
        }
    }

    @Override
    public void body(String namespace, String name, String text) throws Exception {
        super.body(namespace, name, text);
        if (digester.peek() instanceof Feature) {
            final Feature feature = (Feature) digester.peek();
            final Parser parser = ParserFactory.getInstance().getParser(feature.getSrsName(), true);
            final Geometry coordinates = parser.parseToGeometry(text);
            feature.setCoordinates(coordinates);
        }
    }
}
