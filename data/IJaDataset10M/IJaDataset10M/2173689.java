package org.geonetwork.domain.csw202.record;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
import org.geonetwork.domain.ebrim.test.utilities.csw202.record.RecordFactory;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class RecordTest {

    @Test
    public void testUnmarshal() throws FileNotFoundException, JiBXException {
        FileInputStream fis = new FileInputStream(new File("src/test/resources/csw202-record/RecordTestData.xml"));
        IBindingFactory bfact = BindingDirectory.getFactory(Record.class);
        IUnmarshallingContext unMarshallingContext = bfact.createUnmarshallingContext();
        Record unMarshallingResult = (Record) unMarshallingContext.unmarshalDocument(fis, "UTF-8");
        System.out.println("actual: " + unMarshallingResult.toString());
        Record expectedResult = RecordFactory.create();
        System.out.println("expect: " + expectedResult.toString());
        assertEquals("Unmarshalling Record", expectedResult, unMarshallingResult);
        fis = new FileInputStream(new File("src/test/resources/csw202-record/RecordTestData2.xml"));
        bfact = BindingDirectory.getFactory(Record.class);
        unMarshallingContext = bfact.createUnmarshallingContext();
        unMarshallingResult = (Record) unMarshallingContext.unmarshalDocument(fis, "UTF-8");
        System.out.println("actual: " + unMarshallingResult.toString());
        expectedResult = RecordFactory.create2();
        System.out.println("expect: " + expectedResult.toString());
        assertEquals("Unmarshalling Record", expectedResult, unMarshallingResult);
    }

    @Test
    public void testMarshal() throws JiBXException, SAXException, IOException {
        Record o = RecordFactory.create();
        IBindingFactory bfact = BindingDirectory.getFactory(Record.class);
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
        FileInputStream fis = new FileInputStream(new File("src/test/resources/csw202-record/RecordTestData.xml"));
        InputSource expectedResult = new InputSource(fis);
        DifferenceListener differenceListener = new IgnoreTextAndAttributeValuesDifferenceListener();
        Diff diff = new Diff(expectedResult, marshallingResult);
        diff.overrideDifferenceListener(differenceListener);
        assertTrue("Marshalled Record matches expected XML " + diff, diff.similar());
        o = RecordFactory.create2();
        bfact = BindingDirectory.getFactory(Record.class);
        marshallingContext = bfact.createMarshallingContext();
        outConsole = new BufferedWriter(new OutputStreamWriter(System.out));
        marshallingContext.setOutput(outConsole);
        marshallingContext.setIndent(3);
        marshallingContext.marshalDocument(o, "UTF-8", null);
        outputStream = new ByteArrayOutputStream();
        out = new BufferedWriter(new OutputStreamWriter(outputStream));
        marshallingContext.setIndent(3);
        marshallingContext.setOutput(out);
        marshallingContext.marshalDocument(o, "UTF-8", null);
        marshallingResult = new InputSource(new ByteArrayInputStream(outputStream.toByteArray()));
        fis = new FileInputStream(new File("src/test/resources/csw202-record/RecordTestData2.xml"));
        expectedResult = new InputSource(fis);
        differenceListener = new IgnoreTextAndAttributeValuesDifferenceListener();
        diff = new Diff(expectedResult, marshallingResult);
        diff.overrideDifferenceListener(differenceListener);
        assertTrue("Marshalled Record matches expected XML " + diff, diff.similar());
    }
}
