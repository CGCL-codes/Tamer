package org.apache.ibatis.abator.ui.content;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.ibatis.abator.internal.sqlmap.XmlConstants;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class does an elemental SAX parse to see if the given input
 * stream represents an Abator configuration file.  The tests performed include:
 * 
 * <ul>
 *   <li>Ensuring that the public ID is correct</li>
 *   <li>Ensuring that the root element is abatorConfiguration</li>
 * </ul>
 * 
 * @author Jeff Butler
 *
 */
public class AbatorConfigVerifyer extends DefaultHandler {

    private InputStream inputStream;

    private boolean isAbatorConfig;

    private boolean rootElementRead;

    /**
     * 
     */
    public AbatorConfigVerifyer(InputStream inputStream) {
        super();
        this.inputStream = inputStream;
    }

    public boolean isAbatorConfigFile() {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            SAXParser parser = factory.newSAXParser();
            parser.parse(inputStream, this);
        } catch (Exception e) {
            ;
        }
        return isAbatorConfig;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (rootElementRead) {
            throw new SAXException("Root element was not abatorConfiguration");
        }
        rootElementRead = true;
        if ("abatorConfiguration".equals(qName)) {
            isAbatorConfig = true;
            throw new SAXException("Ignore the rest of the file");
        }
    }

    public InputSource resolveEntity(String publicId, String systemId) throws IOException, SAXException {
        if (!XmlConstants.ABATOR_CONFIG_PUBLIC_ID.equals(publicId)) {
            throw new SAXException("Not an Abator configuration file");
        }
        StringReader nullStringReader = new StringReader("");
        return new InputSource(nullStringReader);
    }
}
