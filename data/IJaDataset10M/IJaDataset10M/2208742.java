package ca.uhn.hunit.msg;

import ca.uhn.hunit.ex.ConfigurationException;
import ca.uhn.hunit.iface.TestMessage;
import ca.uhn.hunit.xsd.XmlMessageDefinition;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * XML Message implementation
 */
public class XmlMessageImpl extends AbstractMessage<Document> {

    private Document myDocument;

    private String myText;

    public XmlMessageImpl(String theId) {
        super(theId);
    }

    public XmlMessageImpl(XmlMessageDefinition theDefinition) throws ConfigurationException {
        super(theDefinition);
        try {
            final String text = theDefinition.getText();
            setSourceMessage(text);
        } catch (PropertyVetoException ex) {
            throw new ConfigurationException(ex.getMessage(), ex);
        }
    }

    /**
     * Subclasses should make use of this method to export AbstractInterface properties into
     * the return value for {@link #exportConfigToXml()}
     */
    protected XmlMessageDefinition exportConfig(XmlMessageDefinition theConfig) {
        super.exportConfig(theConfig);
        theConfig.setText(myText);
        return theConfig;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public XmlMessageDefinition exportConfigToXml() {
        return exportConfig(new XmlMessageDefinition());
    }

    @Override
    public Class<Document> getMessageClass() {
        return Document.class;
    }

    @Override
    public String getSourceMessage() {
        return myText;
    }

    @Override
    public TestMessage<Document> getTestMessage() {
        return new TestMessage<Document>(myText, myDocument);
    }

    @Override
    public void setSourceMessage(final String text) throws PropertyVetoException {
        String original = myText;
        try {
            myText = text.trim();
            DocumentBuilderFactory parserFactory = DocumentBuilderFactory.newInstance();
            parserFactory.setValidating(false);
            DocumentBuilder parser = parserFactory.newDocumentBuilder();
            StringReader inputStream = new StringReader(myText);
            myDocument = parser.parse(new InputSource(inputStream));
        } catch (SAXException ex) {
            throw new PropertyVetoException("Failed to parse XML message: " + ex.getMessage(), null);
        } catch (IOException ex) {
            throw new PropertyVetoException("Failed to parse XML message: " + ex.getMessage(), null);
        } catch (ParserConfigurationException ex) {
            throw new PropertyVetoException("Failed to parse XML message: " + ex.getMessage(), null);
        }
        firePropertyChange(SOURCE_MESSAGE_PROPERTY, original, myText);
    }
}
