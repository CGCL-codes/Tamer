package org.gvsig.normalization.operations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.gvsig.normalization.patterns.Element;
import org.gvsig.normalization.patterns.NormalizationPattern;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.XMLException;

public class TestMarshall extends TestCase {

    private static final Logger log = PluginServices.getLogger();

    public void testMarshallUnmarshall() throws MarshalException, ValidationException, XMLException, IOException {
        log.info("testMarshallUnmarshall. start test");
        NormalizationPattern pat = new NormalizationPattern();
        NormalizationPattern pat3 = new NormalizationPattern();
        File file = new File("src-test/org/gvsig/normalization/operations/testdata/patSplitChain.xml");
        pat.loadFromXML(file);
        assertEquals(11, pat.getElements().size());
        assertEquals(0, pat.getNofirstrows());
        Element elem1 = (Element) pat.getElements().get(0);
        assertNotNull(elem1);
        assertEquals("NewField", elem1.getFieldname());
        File ftemp = File.createTempFile("temp", "txt");
        pat.saveToXML(ftemp);
        assertNotNull(ftemp);
        pat3.loadFromXML(ftemp);
        Element elem2 = (Element) pat3.getElements().get(0);
        assertNotNull(elem2);
        assertEquals(elem1.getImportfield(), elem2.getImportfield());
        assertEquals(elem1.getFieldwidth(), elem2.getFieldwidth());
        assertEquals(elem1.getFieldname(), elem2.getFieldname());
        assertEquals(elem1.getInfieldseparators().getDecimalseparator(), elem2.getInfieldseparators().getDecimalseparator());
    }

    public void testUnmarshall() throws MarshalException, FileNotFoundException, UnsupportedEncodingException, ValidationException, XMLException {
        log.info("testUnmarshall. start test");
        File file = new File("src-test/org/gvsig/normalization/operations/testdata/patSplitChain.xml");
        NormalizationPattern pat = new NormalizationPattern();
        pat.loadFromXML(file);
        log.info("pattern loaded");
        assertEquals(11, pat.getElements().size());
        assertEquals(0, ((Element) pat.getElements().get(3)).getFieldwidth());
        assertEquals(true, ((Element) pat.getElements().get(2)).getImportfield());
        assertEquals(true, ((Element) pat.getElements().get(4)).getFieldseparator().getSemicolonsep());
        log.info("testMarshallUnmarshall. test finished");
    }
}
