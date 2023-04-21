package org.gvsig.gpe.gml.parser;

import java.util.ArrayList;
import org.gvsig.gpe.containers.Layer;

public class GMLEscapeCharacters extends GMLReaderBaseTest {

    public String getFile() {
        return "testdata/escape-characters.gml";
    }

    public boolean hasSchema() {
        return false;
    }

    public void makeAsserts() {
        assertEquals(1, getLayers().length);
        Layer layer = getLayers()[0];
        ArrayList features = layer.getFeatures();
        assertEquals(85, features.size());
    }
}
