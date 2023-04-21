package org.geonetwork.gaap.domain.web.request;

import org.junit.Test;
import org.jibx.runtime.*;
import org.geonetwork.gaap.domain.util.RequestFactory;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import org.custommonkey.xmlunit.DifferenceListener;
import org.custommonkey.xmlunit.IgnoreTextAndAttributeValuesDifferenceListener;
import org.custommonkey.xmlunit.Diff;
import java.io.*;
import junit.framework.TestCase;

/**
 * Test class for DeleteUser request
 *
 * @author Jose
 */
public class DeleteUserTest extends TestCase {

    @Test
    public void testUnmarshall() throws FileNotFoundException, JiBXException {
        FileInputStream fis = new FileInputStream(new File("src/test/resources/web/request/DeleteUserTestData.xml"));
        IBindingFactory bfact = BindingDirectory.getFactory(DeleteUser.class);
        IUnmarshallingContext unMarshallingContext = bfact.createUnmarshallingContext();
        DeleteUser unMarshallingResult = (DeleteUser) unMarshallingContext.unmarshalDocument(fis, "UTF-8");
        DeleteUser expectedResult = RequestFactory.createDeleteUserRequest();
        assertEquals("Unmarshalling DeleteUser", expectedResult, unMarshallingResult);
    }

    @Test
    public void testMarshall() throws JiBXException, SAXException, IOException {
        DeleteUser o = RequestFactory.createDeleteUserRequest();
        IBindingFactory bfact = BindingDirectory.getFactory(DeleteUser.class);
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
        FileInputStream fis = new FileInputStream(new File("src/test/resources/web/request/DeleteUserTestData.xml"));
        InputSource expectedResult = new InputSource(fis);
        DifferenceListener differenceListener = new IgnoreTextAndAttributeValuesDifferenceListener();
        Diff diff = new Diff(expectedResult, marshallingResult);
        diff.overrideDifferenceListener(differenceListener);
        assertTrue("Marshalled DeleteUser matches expected XML " + diff, diff.similar());
    }
}
