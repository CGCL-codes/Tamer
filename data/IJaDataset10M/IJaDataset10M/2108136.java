package org.gvsig.gpe.kml.writer.v21.kml;

import org.gvsig.gpe.writer.GPELayerWithBboxTest;

/**
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public class KMLLayerWithBboxTest extends GPELayerWithBboxTest {

    public Class getGPEParserClass() {
        return org.gvsig.gpe.kml.parser.GPEKml2_1_Parser.class;
    }

    public Class getGPEWriterHandlerClass() {
        return org.gvsig.gpe.kml.writer.GPEKml21WriterHandlerImplementor.class;
    }
}
