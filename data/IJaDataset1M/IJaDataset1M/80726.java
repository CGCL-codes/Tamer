package com.volantis.mcs.protocols.css;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.runtime.debug.StrictStyledDOMHelper;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Tests the transforms carried out by the PseudoElementDOMTransformer against
 * various pieces of XML.
 */
public class PseudoElementDOMTransformerTestCase extends DOMTransformerTestAbstract {

    /**
     * Tests the transformer against a document containing only ::before
     * pseudo elements.
     *
     * @throws IOException                  if an IO exception occurs.
     * @throws SAXException                 if a SAX exception occurs.
     * @throws ParserConfigurationException
     */
    public void testBeforeTransform() throws IOException, SAXException, ParserConfigurationException {
        String inputXML = "<html>" + "<head/>" + "<body>" + "<div style=\"{} ::before {content:\'before__\'; color: red}\">" + "blah" + "</div>" + "</body>" + "</html>";
        String expectedXML = "<html>" + "<head/>" + "<body>" + "<div>" + "<span style='color: red'>before__</span>" + "blah" + "</div>" + "</body>" + "</html>";
        doTransform(inputXML, expectedXML);
    }

    /**
     * Tests the transformer against a document containing only :after
     * pseudo elements.
     *
     * @throws IOException                  if an IO exception occurs.
     * @throws SAXException                 if a SAX exception occurs.
     * @throws ParserConfigurationException
     */
    public void testAfterTransform() throws IOException, SAXException, ParserConfigurationException {
        String inputXML = "<html>" + "<head>" + "</head>" + "<body>" + "<div style='{} ::after {content:\"__after\"}'>" + "blah" + "</div>" + "</body>" + "</html>";
        String expectedXML = "<html>" + "<head>" + "</head>" + "<body>" + "<div>" + "blah" + "<span>__after</span>" + "</div>" + "</body>" + "</html>";
        doTransform(inputXML, expectedXML);
    }

    /**
     * Tests the transformer against a document containing ::before and :after
     * pseudo elements.
     *
     * @throws IOException                  if an IO exception occurs.
     * @throws SAXException                 if a SAX exception occurs.
     * @throws ParserConfigurationException
     */
    public void testBeforeAndAfterTransform() throws IOException, SAXException, ParserConfigurationException {
        String inputXML = "<html>" + "<head>" + "</head>" + "<body>" + "<div style='{}" + "            ::before {content:\"before__\"} " + "            ::after {content:\"__after\"}'>" + "blah" + "</div>" + "</body>" + "</html>";
        String expectedXML = "<html>" + "<head>" + "</head>" + "<body>" + "<div>" + "<span>before__</span>" + "blah" + "<span>__after</span>" + "</div>" + "</body>" + "</html>";
        doTransform(inputXML, expectedXML);
    }

    /**
     * Tests the transformer against a document containing an explicit display
     * value at block level.
     *
     * @throws IOException                  if an IO exception occurs.
     * @throws SAXException                 if a SAX exception occurs.
     * @throws ParserConfigurationException
     */
    public void testBlockDisplay() throws IOException, SAXException, ParserConfigurationException {
        String inputXML = "<html>" + "<head>" + "</head>" + "<body>" + "<div style='{} ::before {content:\"before__\"; " + "                         display:block}'>" + "blah" + "</div>" + "</body>" + "</html>";
        String expectedXML = "<html>" + "<head>" + "</head>" + "<body>" + "<div>" + "<div>before__</div>" + "blah" + "</div>" + "</body>" + "</html>";
        doTransform(inputXML, expectedXML);
    }

    /**
     * Tests the transformer against a document containing an explicit display
     * value at inline level.
     *
     * @throws IOException                  if an IO exception occurs.
     * @throws SAXException                 if a SAX exception occurs.
     * @throws ParserConfigurationException
     */
    public void testInlineDisplay() throws IOException, SAXException, ParserConfigurationException {
        String inputXML = "<html>" + "<head>" + "</head>" + "<body>" + "<div style='{} ::before {content:\"before__\"; " + "                         display:inline}'>" + "blah" + "</div>" + "</body>" + "</html>";
        String expectedXML = "<html>" + "<head>" + "</head>" + "<body>" + "<div>" + "<span>before__</span>" + "blah" + "</div>" + "</body>" + "</html>";
        doTransform(inputXML, expectedXML);
    }

    /**
     * Tests the transformer against a document containing an unsupported
     * display value.
     *
     * @throws IOException                  if an IO exception occurs.
     * @throws SAXException                 if a SAX exception occurs.
     * @throws ParserConfigurationException
     */
    public void testUnsupportedDisplayValue() throws IOException, SAXException, ParserConfigurationException {
        String inputXML = "<html>" + "<head>" + "</head>" + "<body>" + "<div style='{} ::before {content:\"before__\"; " + "                         display:none}'>" + "blah" + "</div>" + "</body>" + "</html>";
        try {
            doTransform(inputXML, "");
            fail("An UnsupportedOperationException should have been thrown");
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * Do the transform using the input XML and check the results against the
     * expected XML.
     *
     * @param inputXML    the XML to build the document from.
     * @param expectedXML the XML the document should represent post-transform.
     * @throws IOException  if an IO exception occurs.
     * @throws SAXException if a SAX exception occurs.
     */
    private void doTransform(String inputXML, String expectedXML) throws IOException, SAXException, ParserConfigurationException {
        StrictStyledDOMHelper helper = new StrictStyledDOMHelper(null);
        Document document = helper.parse(inputXML);
        ReplacementPseudoElementFactory replacementPseudoElementFactory = new DefaultReplacementPseudoElementFactory(createDOMProtocol());
        PseudoElementDOMTransformer transformer = new PseudoElementDOMTransformer(replacementPseudoElementFactory);
        transformer.transform(null, document);
        String actualXML = helper.render(document);
        String canonicalExpectedXML = helper.normalize(expectedXML);
        assertXMLEquals("Actual XML does not match expected XML", canonicalExpectedXML, actualXML);
    }
}
