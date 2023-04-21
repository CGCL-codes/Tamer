package org.geonetwork.domain.filter110;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.DifferenceListener;
import org.custommonkey.xmlunit.IgnoreTextAndAttributeValuesDifferenceListener;
import org.custommonkey.xmlunit.XMLTestCase;
import org.geonetwork.domain.filter110.sort.SortBy;
import org.geonetwork.domain.filter110.test.utilities.sort.SortByFactory;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Tests sortBy.
 * 
 * @author heikki doeleman
 * 
 */
public class SortByTest extends XMLTestCase {

    public SortByTest(String name) {
        super(name);
    }

    /**
	 * Tests unmarshalling (XML is instantiated as Java object) for SortBy.
	 * 
	 * @throws FileNotFoundException
	 * @throws JiBXException
	 */
    public void testUnmarshal() throws FileNotFoundException, JiBXException {
        FileInputStream fis = new FileInputStream(new File("src/test/resources/filter/SortByTestData.xml"));
        IBindingFactory bfact = BindingDirectory.getFactory(SortBy.class);
        IUnmarshallingContext unMarshallingContext = bfact.createUnmarshallingContext();
        SortBy unMarshallingResult = (SortBy) unMarshallingContext.unmarshalDocument(fis, "UTF-8");
        SortBy expectedResult = SortByFactory.create();
        assertEquals("Unmarshalling SortBy", expectedResult, unMarshallingResult);
    }

    /**
	 * Tests marshalling (Java object puts out XML) a SortBy.
	 * 
	 * @throws JiBXException
	 * @throws SAXException
	 * @throws IOException
	 */
    public void testMarshal() throws JiBXException, SAXException, IOException {
        SortBy o = SortByFactory.create();
        IBindingFactory bfact = BindingDirectory.getFactory(SortBy.class);
        IMarshallingContext marshallingContext = bfact.createMarshallingContext();
        Writer outConsole = new BufferedWriter(new OutputStreamWriter(System.out));
        marshallingContext.setOutput(outConsole);
        marshallingContext.setIndent(3);
        marshallingContext.marshalDocument(o, "UTF-8", null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer out = new BufferedWriter(new OutputStreamWriter(outputStream));
        marshallingContext.setIndent(3);
        marshallingContext.setOutput(out);
        marshallingContext.marshalDocument(o, "UTF-8", null);
        InputSource marshallingResult = new InputSource(new ByteArrayInputStream(outputStream.toByteArray()));
        FileInputStream fis = new FileInputStream(new File("src/test/resources/filter/SortByTestData.xml"));
        InputSource expectedResult = new InputSource(fis);
        DifferenceListener differenceListener = new IgnoreTextAndAttributeValuesDifferenceListener();
        Diff diff = new Diff(expectedResult, marshallingResult);
        diff.overrideDifferenceListener(differenceListener);
        assertTrue("Marshalled SortBy matches expected XML " + diff, diff.similar());
    }
}
