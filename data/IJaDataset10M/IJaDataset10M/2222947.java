package org.gvsig.gpe.kml.writer.v21.kmz;

import org.gvsig.gpe.writer.GPEPolygonsLayerTest;

/**
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public class KMZPolygonLayerTest extends GPEPolygonsLayerTest {

    public Class getGPEParserClass() {
        return org.gvsig.gpe.kml.parser.GPEKml2_1_Parser.class;
    }

    public Class getGPEWriterHandlerClass() {
        return org.gvsig.gpe.kml.writer.GPEKml21WriterHandlerImplementor.class;
    }

    public String getFormat() {
        return "KMZ";
    }
}
