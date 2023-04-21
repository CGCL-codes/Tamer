package issrg.policytester.config;

import issrg.editor2.configurations.EnvParametersConfiguration;
import issrg.editor2.configurations.LDAPConfiguration;
import issrg.editor2.configurations.TAPFileConfiguration;
import issrg.editor2.configurations.WSDLFileConfiguration;
import issrg.editor2.configurations.WebDAVConfiguration;
import issrg.policytester.PKCConfiguration;
import issrg.utils.gui.AbstractConfigComponent;
import issrg.utils.gui.xml.ValidationException;
import java.io.IOException;
import java.util.Arrays;
import java.util.TreeSet;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.apache.xerces.dom.DOMImplementationImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

/**
 *
 * @author Christian Azzopardi
 */
public class ConfigDocHolder extends AbstractConfigComponent {

    public Document document;

    public LDAPConfiguration ldapConfig;

    public EnvParametersConfiguration envParams;

    public TAPFileConfiguration tapFileConfig;

    public WSDLFileConfiguration wsdlFileConfig;

    public WebDAVConfiguration webDAVConfig;

    public PKCConfiguration pkcConfig;

    public ConfigDocHolder() {
        super();
        ldapConfig = new LDAPConfiguration(this);
        envParams = new EnvParametersConfiguration(this);
        tapFileConfig = new TAPFileConfiguration(this);
        wsdlFileConfig = new WSDLFileConfiguration(this);
        webDAVConfig = new WebDAVConfiguration(this);
        pkcConfig = new PKCConfiguration(this);
        addXMLChangeListener(ldapConfig);
        addXMLChangeListener(envParams);
        addXMLChangeListener(tapFileConfig);
        addXMLChangeListener(wsdlFileConfig);
        addXMLChangeListener(webDAVConfig);
        addXMLChangeListener(pkcConfig);
        load("pt.cfg", "issrg/policytester/pt.cfg");
    }

    /**
     * Method that is used to return a good Valid Configuration XML Document.
     * If there is no current document set, the method creates a Document
     * itself.
     * <p>
     * After a document ConfigDocHolderis obtained it will add the required tags and attributes,
     * to make it valid.
     *
     * @return   A valid Document that is usable and conformable to the XML
     *           tags we would like to expect for this configuration file.
     */
    public Document getValidDocument() {
        Element rootElement = this.document.getDocumentElement();
        Node rootNode = (Node) rootElement;
        NodeList nlist = rootNode.getChildNodes();
        if (this.document == null || !rootElement.getTagName().equals("PolicyEditorConfiguration") || nlist.getLength() != 1 || !nlist.item(0).equals("LDAPConfiguration")) {
            DOMImplementation domImpl = new DOMImplementationImpl();
            DocumentType docType = domImpl.createDocumentType("DTD", null, null);
            this.document = domImpl.createDocument(null, "PolicyEditorConfiguration", docType);
            Element childElement = this.document.createElement("LDAPConfiguration");
            this.document.getDocumentElement().appendChild(childElement);
        }
        String validAttributes[] = { "Name", "Host", "Version", "Port", "BaseDN", "Login", "Password", "Active" };
        TreeSet set = new TreeSet(Arrays.asList(validAttributes));
        NodeList nodelist = nlist.item(0).getChildNodes();
        for (int i = 0; i < nodelist.getLength(); i++) {
            if (!nodelist.item(i).getNodeName().equals("LDAPDirectory")) {
                try {
                    nlist.item(0).removeChild(nodelist.item(i));
                } catch (DOMException DOMe) {
                }
            } else {
                NodeList nl = nodelist.item(i).getChildNodes();
                for (int j = 0; j < nl.getLength(); j++) {
                    if (!nl.item(j).equals("BinaryAttribute")) {
                        try {
                            nodelist.item(i).removeChild(nl.item(j));
                        } catch (DOMException DOMe) {
                        }
                    }
                    if (nl.item(j).hasChildNodes()) {
                    }
                }
            }
            for (int j = 0; j < nlist.item(i).getAttributes().getLength(); j++) {
                if (!set.contains(nlist.item(i).getAttributes().item(j))) {
                    nlist.item(i).getAttributes().removeNamedItem(nlist.item(i).getAttributes().item(j).toString());
                }
            }
        }
        return this.document;
    }

    /**
     * Method that validates the current Document. Will validate the document
     * with the specified 'ptconfiguration.dtd'. This is done by parsing the
     * document.
     *
     * @throws a ValidationException if an error occurs.
     */
    public void validateDocument() throws ValidationException {
        if (this.document == null) {
            throw new ValidationException();
        }
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(false);
        try {
            XMLReader parser = factory.newSAXParser().getXMLReader();
            InputSource is = new InputSource(ConfigDocHolder.class.getClassLoader().getResourceAsStream("issrg/policytester/pt.cfg"));
            parser.parse(is);
        } catch (SAXParseException saxpe) {
            throw new ValidationException(saxpe.getMessage());
        } catch (SAXException saxe) {
            throw new ValidationException(saxe.getMessage());
        } catch (ParserConfigurationException pce) {
            throw new ValidationException(pce.getMessage());
        } catch (IOException ioe) {
            throw new ValidationException(ioe.getMessage());
        }
    }
}
