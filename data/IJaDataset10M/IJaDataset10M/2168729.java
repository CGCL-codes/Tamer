package org.uc3m.verbus.bpel.parser;

import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.XSModelImpl;
import org.apache.xerces.xs.XSModel;
import org.uc3m.verbus.bpel.BPELException;
import org.uc3m.verbus.bpel.util.WSDLDefinition;
import org.uc3m.verbus.util.Logger;
import org.uc3m.verbus.util.xschema.XSchemaParser;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WSDLParser {

    protected WSDLDefinition definition;

    /**
     * Constructs a new WSDLParser object.
     *
     */
    public WSDLParser() {
        definition = new WSDLDefinition();
    }

    /**
     * Constructs a new WSDLParser object, and parses the specified
     * WSDL definition. It can be obtained later using the 
     * getDefinition method.
     *
     * @param path filename of the WSDL definition
     * @see #getDefinition()
     *
     */
    public WSDLParser(String path) throws BPELException {
        definition = new WSDLDefinition();
        parse(path);
    }

    /**
     * Parses a new WSDL file and adds it to the list of definitions
     * of this object.
     * 
     * @param path the path of the WSDL file
     * @throws BPELException if any error occurs
     */
    public void parse(String path) throws BPELException {
        try {
            WSDLFactory factory = WSDLFactory.newInstance("org.uc3m.verbus.bpel.parser.WSDLFactoryImpl");
            WSDLReader reader = factory.newWSDLReader();
            reader.setFeature("javax.wsdl.verbose", false);
            definition.addDefinition(reader.readWSDL(path));
            XSModel schema = getXSModel(((WSDLReaderImpl) reader).getTypesElement());
            if (schema != null) definition.addSchema(schema); else {
                Logger.logger.log(Logger.WARNING_LEVEL, "WSDLParser", "null xml-schema definition");
                definition.addSchema(new XSModelImpl(new SchemaGrammar[0]));
            }
        } catch (WSDLException ex) {
            throw new BPELException("[WSDLException] " + ex.getMessage(), ex);
        }
    }

    /**
     * Returns the parsed WSDL definition
     *
     * @return the parsed WSDL definition
     *
     */
    public WSDLDefinition getDefinition() {
        return definition;
    }

    protected XSModel getXSModel(Element typesElement) throws BPELException {
        Element schemaElement = null;
        XSModel schema = null;
        if (typesElement != null && typesElement.getNodeType() == Node.ELEMENT_NODE) {
            Element element = typesElement;
            NodeList children = element.getElementsByTagNameNS(XSchemaParser.SCHEMA_NAMESPACE, "schema");
            if (children.getLength() > 0) {
                schemaElement = (Element) children.item(0);
            }
        }
        if (schemaElement != null && XSchemaParser.SCHEMA_NAMESPACE.equals(schemaElement.getNamespaceURI()) && "schema".equals(schemaElement.getLocalName())) {
            try {
                Logger.logger.log(Logger.MAX_DEBUG_LEVEL, "WSDLParser", "Parsing XML Schema");
                XSchemaParser xparser = new XSchemaParser(schemaElement);
                schema = xparser.getSchema();
            } catch (IllegalArgumentException e) {
                throw new BPELException(e.getMessage(), e);
            }
        }
        return schema;
    }
}
