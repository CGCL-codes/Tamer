package com.volantis.xml.pipeline.sax.convert;

import com.volantis.xml.pipeline.sax.PipelineTestAbstract;
import com.volantis.xml.pipeline.sax.IntegrationTestHelper;
import com.volantis.xml.pipeline.sax.ContentWriter;
import com.volantis.xml.pipeline.sax.XMLProcessingException;
import com.volantis.xml.pipeline.sax.XMLStreamingException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ContentHandler;
import java.io.Writer;

/**
 * Test case for the URL to URLC converter.
 */
public class URLToURLCConversionTestCase extends PipelineTestAbstract {

    public URLToURLCConversionTestCase(String name) {
        super(name);
    }

    public void testURLToURLCConversion() throws Exception {
        doTest(new IntegrationTestHelper().getPipelineFactory(), "URLToURLCConversionTestCase.input.xml", "URLToURLCConversionTestCase.expected.xml");
    }

    public void testBadServer() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(), "URLToURLCConversionTestCase.bad_server.xml", "URLToURLCConversionTestCase.bad_server.xml");
            fail("Should have had an exception");
        } catch (XMLProcessingException e) {
        }
    }

    public void testConversionProblem() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(), "URLToURLCConversionTestCase.bad_convert.xml", "URLToURLCConversionTestCase.bad_convert.xml");
            fail("Should have had an exception");
        } catch (XMLStreamingException e) {
            assertTrue("Cause exception not a URLConversionException (was " + e.getCause().getClass().getName() + ")", e.getCause() instanceof URLConversionException);
        }
    }

    /**
     * A factory method that creates a handler that encodes '&' characters in
     * attribute values.
     */
    protected ContentHandler createContentHandler(Writer writer) {
        return new ContentWriter(writer) {

            protected void writeAttributes(Attributes attrs) throws SAXException {
                if (attrs != null) {
                    for (int i = 0; i < attrs.getLength(); i++) {
                        String aName = attrs.getLocalName(i);
                        if ("".equals(aName)) {
                            aName = attrs.getQName(i);
                        }
                        write(" " + aName + "=\"" + encodedValue(attrs.getValue(i)) + "\"");
                    }
                }
            }

            /**
             * Utility method that encodes '&' characters in the given string.
             *
             * @param value the string to be encoded
             * @return an encoded version of the given string
             */
            protected String encodedValue(String value) {
                StringBuffer buffer = new StringBuffer(value);
                for (int i = buffer.length() - 1; i >= 0; i--) {
                    if (buffer.charAt(i) == '&') {
                        buffer.replace(i, i + 1, "&amp;");
                    }
                }
                return buffer.toString();
            }
        };
    }
}
