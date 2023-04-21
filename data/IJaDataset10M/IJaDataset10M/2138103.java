package org.xmlcml.cml.converters.cif;

import java.io.IOException;
import org.junit.Test;
import org.xmlcml.cml.converters.AbstractConverterTestBase;

public class CIF2CIFXMLConverterTest extends AbstractConverterTestBase {

    public final String getLocalDirName() {
        return "cif/cif";
    }

    public final String getInputSuffix() {
        return "cif";
    }

    public final String getOutputSuffix() {
        return "cif.xml";
    }

    @Test
    public void testConverter() throws IOException {
        setQuiet(true);
        runBasicConverterTest();
    }
}
