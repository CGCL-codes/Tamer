package org.fressia.asserts.xml;

import com.meterware.httpunit.WebResponse;
import org.fressia.asserts.Assert;
import org.fressia.core.sbes.SbeEvaluationException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.StringReader;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

/**
 *
 * @author Alvaro Egana
 *
 */
public class XmlValidatesSchemaAssert extends Assert {

    public XmlValidatesSchemaAssert(String argument) {
        super(argument);
    }

    @Override
    protected <T> boolean expressionArgument(T target, Class<?> targetType) throws SbeEvaluationException {
        try {
            String xml;
            if (targetType == String.class) {
                xml = (String) target;
            } else if (targetType == WebResponse.class) {
                xml = ((WebResponse) target).getText();
            } else {
                throw new SbeEvaluationException("Type not supported by 'validates' assert: " + targetType);
            }
            StringReader sr = new StringReader(xml);
            InputSource is = new InputSource(sr);
            is.setEncoding("utf-8");
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true);
            DocumentBuilder parser = domFactory.newDocumentBuilder();
            Document document = parser.parse(is);
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Source schemaFile = new StreamSource(new File(getArgument(true)));
            Schema schema = factory.newSchema(schemaFile);
            Validator validator = schema.newValidator();
            try {
                validator.validate(new DOMSource(document));
                return true;
            } catch (SAXException e) {
                return false;
            }
        } catch (Exception e) {
            throw new SbeEvaluationException(e);
        }
    }
}
