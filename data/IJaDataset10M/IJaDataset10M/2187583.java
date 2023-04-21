package org.gvsig.gpe.gml.writer.v2;

import org.gvsig.gpe.writer.GPEFeatureWithElementsTest;

/**
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public class GMLFeatureWithElementsTest extends GPEFeatureWithElementsTest {

    public Class getGPEParserClass() {
        return org.gvsig.gpe.gml.parser.GPEGml2_1_2_Parser.class;
    }

    public Class getGPEWriterHandlerClass() {
        return org.gvsig.gpe.gml.writer.GPEGmlv2WriterHandlerImplementor.class;
    }
}
