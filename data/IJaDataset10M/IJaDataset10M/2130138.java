package org.xmlcml.cml.converters.rdf.owl;

import java.io.IOException;
import java.io.StringReader;
import org.junit.Ignore;
import org.junit.Test;
import org.xmlcml.cif.CIFConstants;
import org.xmlcml.cml.base.CMLBuilder;
import org.xmlcml.cml.base.CMLConstants;
import org.xmlcml.cml.base.CMLElement;
import org.xmlcml.cml.converters.AbstractConverterTestBase;

/** converts CML dictionaries to OWL
 * 
 * @author pm286
 *
 */
public class CML2OWLConverterTest extends AbstractConverterTestBase {

    public final String getLocalDirName() {
        return "rdf/cml2owl";
    }

    public final String getInputSuffix() {
        return "xml";
    }

    public final String getOutputSuffix() {
        return "owl";
    }

    @Test
    @Ignore
    public void testConverter() throws IOException {
        setQuiet(true);
        runBasicConverterTest();
    }

    @Test
    @Ignore
    public void testCreateOWL() {
        String s = "<dictionary " + CMLConstants.CML_XMLNS + " id='d1' " + CIFConstants.IUCR_XMLNS_PREFIX + ">" + "     <entry id='e1' term='a' iucr:_type='numb'/>" + "   </dictionary>";
        CMLElement element = null;
        try {
            element = (CMLElement) new CMLBuilder().build(new StringReader(s)).getRootElement();
        } catch (Exception e) {
            throw new RuntimeException("parse failed", e);
        }
        element.debug();
        CML2OWL cml2OWL = new CML2OWL();
        cml2OWL.convertCMLElement(element);
    }
}
