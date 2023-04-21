package org.vizzini.util.xml;

import java.io.StringReader;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;
import junit.framework.TestCase;
import junit.textui.TestRunner;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Provides JUnit tests for <code>TransformUtilities</code>.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.3
 */
public class TransformUtilitiesTest extends TestCase {

    /** XML. */
    private static final String XML = "<?xml version='1.0' encoding='UTF-8'?>" + "<TestRequest>" + "<Address>" + "<Address1>5970 Greenwood Plaza Blvd</Address1>" + "<City>Greenwood Village</City>" + "<State>CO</State>" + "<ZipCode>80111</ZipCode>" + "<Zip4>4703</Zip4>" + "</Address>" + "</TestRequest>";

    /** XSLT. */
    private static final String XSLT = "<?xml version='1.0' encoding='UTF-8'?>" + "<xsl:stylesheet version='1.1' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>" + "<xsl:output method='xml' version='1.0' omit-xml-declaration='no' indent='yes'/>" + "<xsl:template match='/'>" + "<xsl:apply-templates/>" + "</xsl:template>" + "<xsl:template match='TestRequest/Address'>" + "<Request xmlns='http://www.wildblue.com/XMLSchema/v1.0' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:schemaLocation='http://www.wildblue.com/XMLSchema/v1.0 http://wbjthompsonxp:7001/XMLSchema/v1.0/SIEBASRequest.xsd'>" + "<SchemaVersion>1.0</SchemaVersion>" + "<TransactionID>4444</TransactionID>" + "<TransactionMode>sync</TransactionMode>" + "<Requestor>Somebody</Requestor>" + "<ResponseURL>https://www.wildblue.com</ResponseURL>" + "<Transaction TransactionType='Query'>" + "<ServiceAvailabilityRequest>" + "<Address>" + "<Address1>" + "<xsl:value-of select='Address1'/>" + "</Address1>" + "<City>" + "<xsl:value-of select='City'/>" + "</City>" + "<State>" + "<xsl:value-of select='State'/>" + "</State>" + "<ZipCode>" + "<xsl:value-of select='ZipCode'/>" + "</ZipCode>" + "<Zip4>" + "<xsl:value-of select='Zip4'/>" + "</Zip4>" + "</Address>" + "</ServiceAvailabilityRequest>" + "</Transaction>" + "</Request>" + "</xsl:template>" + "</xsl:stylesheet>";

    /** Transformed XML. */
    private static final String TRANSFORMED_XML = "<?xml version='1.0' encoding='UTF-8'?><Request xmlns='http://www.wildblue.com/XMLSchema/v1.0' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:schemaLocation='http://www.wildblue.com/XMLSchema/v1.0 http://wbjthompsonxp:7001/XMLSchema/v1.0/SIEBASRequest.xsd'><SchemaVersion>1.0</SchemaVersion><TransactionID>4444</TransactionID><TransactionMode>sync</TransactionMode><Requestor>Somebody</Requestor><ResponseURL>https://www.wildblue.com</ResponseURL><Transaction TransactionType='Query'><ServiceAvailabilityRequest><Address><Address1>5970 Greenwood Plaza Blvd</Address1><City>Greenwood Village</City><State>CO</State><ZipCode>80111</ZipCode><Zip4>4703</Zip4></Address></ServiceAvailabilityRequest></Transaction></Request>";

    /**
     * Application method.
     *
     * @param  args  Application arguments.
     *
     * @since  v0.3
     */
    public static void main(String[] args) {
        TestRunner.run(TransformUtilitiesTest.class);
    }

    /**
     * Test the <code>transform()</code> method.
     *
     * @since  v0.3
     */
    public void testTransformDocument() {
        Exception exception = null;
        XMLUtilities xmlUtils = new XMLUtilities();
        TransformUtilities transformUtils = new TransformUtilities();
        try {
            Document xmlDoc = xmlUtils.parseDocument(XML, true, false);
            Document xsltDoc = xmlUtils.parseDocument(XSLT, true, false);
            DOMResult domResult = transformUtils.transform(xmlDoc, xsltDoc);
            Node node = domResult.getNode();
            String result = xmlUtils.convertToString(node);
            assertEquals(TRANSFORMED_XML, result);
        } catch (Exception e) {
            exception = e;
        }
        assertNull(exception);
    }

    /**
     * Test the <code>transform()</code> method.
     *
     * @since  v0.3
     */
    public void testTransformSource() {
        XMLUtilities xmlUtils = new XMLUtilities();
        TransformUtilities transformUtils = new TransformUtilities();
        Source xml = new StreamSource(new StringReader(XML));
        Source xslt = new StreamSource(new StringReader(XSLT));
        DOMResult domResult = transformUtils.transform(xml, xslt);
        Node node = domResult.getNode();
        String result = xmlUtils.convertToString(node);
        assertEquals(TRANSFORMED_XML, result);
    }

    /**
     * Test the <code>transform()</code> method.
     *
     * @since  v0.3
     */
    public void testTransformString() {
        XMLUtilities xmlUtils = new XMLUtilities();
        TransformUtilities transformUtils = new TransformUtilities();
        DOMResult domResult = transformUtils.transform(XML, XSLT);
        Node node = domResult.getNode();
        String result = xmlUtils.convertToString(node);
        assertEquals(TRANSFORMED_XML, result);
    }
}
