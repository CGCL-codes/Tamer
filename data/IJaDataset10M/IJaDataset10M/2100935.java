package com.volantis.mcs.migrate.unit.config.lpdm.xsl;

import com.volantis.mcs.migrate.api.config.ConfigFactory;
import com.volantis.mcs.repository.xml.PolicySchemas;
import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.MCSTransformerMetaFactory;
import com.volantis.shared.content.StringContentInput;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.schema.validator.SchemaValidator;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Provides a means of testing XSL transformations.
 */
public abstract class XSLTestAbstract extends TestCaseAbstract {

    /**
     * The meta factory to use for creating JAXP XSL transformer factories.
     * <p>
     * Since we are a test tool we always use the repackaged MCS version of
     * Xalan which we ship rather than relying on the standard JAXP dynamic
     * lookup process.
     */
    private static MCSTransformerMetaFactory transformerMetaFactory = new MCSTransformerMetaFactory();

    /**
     * Tests that the input is transformed into the output using the given
     * stylesheet.
     *
     * @param input         input XML
     * @param expected      expected result of transforming the input XML
     * @param stylesheet    stylesheet to use to transform
     * @throws Exception    if there was a problem running the test
     */
    protected void doTransformation(Source input, InputStream expected, Source stylesheet) throws Exception {
        boolean previousWhitespaceSetting = XMLUnit.getIgnoreWhitespace();
        try {
            StreamSource streamSource = (StreamSource) input;
            String inputString = readAsString(streamSource.getInputStream());
            input = new StreamSource(new StringReader(inputString), streamSource.getSystemId());
            EntityResolver resolver = ConfigFactory.getDefaultInstance().createRepositoryEntityResolver();
            SAXTransformerFactory factory = (SAXTransformerFactory) transformerMetaFactory.createTransformerFactory();
            Source xslSource = stylesheet;
            TransformerHandler robotInDisguise = factory.newTransformerHandler(xslSource);
            XMLReader reader = new com.volantis.xml.xerces.parsers.SAXParser();
            reader.setEntityResolver(resolver);
            reader.setContentHandler(robotInDisguise);
            reader.setDTDHandler(robotInDisguise);
            reader.setEntityResolver(resolver);
            reader.setProperty("http://xml.org/sax/properties/lexical-handler", robotInDisguise);
            InputSource inputSource;
            if (input instanceof StreamSource) {
                StreamSource inputStreamSource = (StreamSource) input;
                InputStream inputStream = inputStreamSource.getInputStream();
                Reader inputReader = inputStreamSource.getReader();
                if (inputReader == null && inputStream == null) {
                    throw new IllegalArgumentException("Input source has neither an " + "InputStream or Reader");
                } else if (inputReader != null && inputStream != null) {
                    throw new IllegalArgumentException("Input source has both an " + "InputStream or Reader");
                } else if (inputReader != null) {
                    inputSource = new InputSource(inputReader);
                } else {
                    inputSource = new InputSource(inputStream);
                }
                inputSource.setSystemId(inputSource.getSystemId());
            } else {
                throw new IllegalArgumentException("Unknown input source type: " + input);
            }
            StringWriter output = new StringWriter();
            Result result = new StreamResult(output);
            robotInDisguise.setResult(result);
            reader.parse(inputSource);
            XMLUnit.setIgnoreWhitespace(false);
            String expectedString = readAsString(expected);
            String actualString = output.toString();
            SchemaValidator schemaValidator = new SchemaValidator();
            schemaValidator.addSchema(PolicySchemas.MARLIN_LPDM_V3_0);
            schemaValidator.addSchemata(PolicySchemas.MARLIN_RPDM_DTDS);
            schemaValidator.addSchemata(PolicySchemas.REPOSITORY_2005_09);
            schemaValidator.addSchemata(PolicySchemas.REPOSITORY_2005_12);
            schemaValidator.addSchemata(PolicySchemas.REPOSITORY_2006_02);
            schemaValidator.validate(new StringContentInput(inputString));
            schemaValidator.validate(new StringContentInput(expectedString));
            boolean worked = false;
            try {
                XMLAssert.assertXMLEqual("Result should be as expected", new StringReader(expectedString), new StringReader(actualString));
                worked = true;
            } finally {
                if (!worked) {
                    System.out.println("Expected: " + expectedString);
                    System.out.println("Actual: " + actualString);
                }
            }
        } finally {
            XMLUnit.setIgnoreWhitespace(previousWhitespaceSetting);
        }
    }

    private String readAsString(InputStream stream) throws IOException {
        StringBuffer buffer = new StringBuffer();
        Reader reader = new InputStreamReader(stream);
        int read = 0;
        while ((read = reader.read()) != -1) {
            buffer.append((char) read);
        }
        return buffer.toString();
    }
}
