package com.dilanperera.rapidws.codegen;

import com.dilanperera.rapidws.core.TextContent;
import com.dilanperera.rapidws.core.XMLHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a WSDL Document.
 */
public class WSDLDocument {

    /**
	 * The XML document containing the WSDL content.
	 */
    private Document wsdlDocument = null;

    /**
	 * Initializes a new instance of the WSDLDocument class with the specified file path.
	 * @param wsdlFilePath the path of the WSDL file.
	 * @throws ParserConfigurationException thrown if the parser is incorrectly configured.
	 * @throws IOException thrown if an Input/Output error occurs.
	 * @throws SAXException thrown if a parsing error takes place.
	 */
    public WSDLDocument(String wsdlFilePath) throws ParserConfigurationException, IOException, SAXException {
        this(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(wsdlFilePath));
    }

    /**
	 * Initializes a new instance of the WSDLDocument class with the specified file path.
	 * @param wsdlDocument the XML document containing the WSDL content.
	 */
    protected WSDLDocument(Document wsdlDocument) {
        this.wsdlDocument = wsdlDocument;
    }

    /**
	 * Gets the WSDL document.
	 * @return the WSDL document.
	 */
    public Document getWSDLDocument() {
        return this.wsdlDocument;
    }

    public Node getRootDefinitionNode() {
        return this.wsdlDocument.getFirstChild();
    }

    public String getRootDefinitionTargetNamespace() {
        String targetNamespace = TextContent.EMPTY;
        Node parentDefinitionNode = this.getRootDefinitionNode();
        targetNamespace = XMLHelper.getAttributeValue(parentDefinitionNode, "targetNamespace");
        return targetNamespace;
    }

    public String getRootDefinitionTargetNamespaceCleaned() {
        String targetNamespace = TextContent.EMPTY;
        Node parentDefinitionNode = this.getRootDefinitionNode();
        targetNamespace = XMLHelper.getAttributeValue(parentDefinitionNode, "targetNamespace");
        targetNamespace = targetNamespace.replace("http://", TextContent.EMPTY);
        targetNamespace = targetNamespace.replace("/", ".");
        return targetNamespace;
    }

    public Node getService() {
        Node serviceNode = null;
        Node parentDefinitionNode = this.getRootDefinitionNode();
        serviceNode = XMLHelper.getNode(parentDefinitionNode, "wsdl:service");
        return serviceNode;
    }

    public String getServiceName() {
        return XMLHelper.getAttributeValue(this.getService(), "name");
    }

    public List<Node> getOperations() {
        List<Node> returnOperationNodes = new ArrayList<Node>();
        Node portTypeNode = this.getPortType();
        if ((portTypeNode != null) && (portTypeNode.getChildNodes() != null)) {
            returnOperationNodes = XMLHelper.getNodes(portTypeNode, "wsdl:operation");
        }
        return returnOperationNodes;
    }

    public Node getWSDLTypeNode() {
        Node wsdlTypeNode = null;
        Node parentDefinitionNode = this.getRootDefinitionNode();
        wsdlTypeNode = XMLHelper.getNode(parentDefinitionNode, "wsdl:types");
        return wsdlTypeNode;
    }

    public Node getPortType() {
        Node portTypeNode = null;
        Node parentDefinitionNode = this.getRootDefinitionNode();
        portTypeNode = XMLHelper.getNode(parentDefinitionNode, "wsdl:portType");
        return portTypeNode;
    }

    public String getOperationName(Node wsdlOperationNode) {
        return XMLHelper.getAttributeValue(wsdlOperationNode, "name");
    }

    public List<Node> getNamespaces() {
        return XMLHelper.getAttributesOnPattern(this.getRootDefinitionNode(), "xmlns:");
    }

    public boolean hasInputEx(Node operationNode) {
        return (this.getInputNode(operationNode) != null);
    }

    public boolean hasInput(Node operationNode) {
        boolean hasInput = false;
        Node inputNode = this.getInputNode(operationNode);
        if (inputNode != null) {
            try {
                String outputMessageName = this.getOutputMessageCleaned(operationNode);
                Node outputMessageNode = this.getMessageNode(outputMessageName);
                String messagePartName = this.getMessagePartName(outputMessageNode);
                String messagePartElement = this.getMessagePartElement(outputMessageNode);
                String messagePartElementNamespaceAlias = messagePartElement.substring(0, messagePartElement.indexOf(":"));
                String messagePartOperationName = messagePartElement.substring(messagePartElement.indexOf(":") + 1);
                Node messageNode = this.getMessageNode(messagePartOperationName);
                hasInput = (messageNode != null) && (messageNode.hasChildNodes());
            } catch (Exception ex) {
            }
        }
        return hasInput;
    }

    public Node getInputNode(Node operationNode) {
        return XMLHelper.getNode(operationNode, "wsdl:input");
    }

    public String getInputMessage(Node operationNode) {
        String inputMessage = TextContent.EMPTY;
        Node inputNode = this.getInputNode(operationNode);
        inputMessage = XMLHelper.getAttributeValue(inputNode, "message");
        return inputMessage;
    }

    public String getInputMessageCleaned(Node operationNode) {
        String inputMessage = this.getInputMessage(operationNode);
        return inputMessage.substring(inputMessage.indexOf(":") + 1);
    }

    public boolean hasOutputEx(Node operationNode) {
        return (this.getOutputNode(operationNode) != null);
    }

    public boolean hasOutput(Node operationNode) {
        boolean hasOutput = false;
        Node outputNode = this.getOutputNode(operationNode);
        if (outputNode != null) {
            try {
                String outputMessageName = this.getOutputMessageCleaned(operationNode);
                Node outputMessageNode = this.getMessageNode(outputMessageName);
                String messagePartName = this.getMessagePartName(outputMessageNode);
                String messagePartElement = this.getMessagePartElement(outputMessageNode);
                String messagePartElementNamespaceAlias = messagePartElement.substring(0, messagePartElement.indexOf(":"));
                String messagePartOperationName = messagePartElement.substring(messagePartElement.indexOf(":") + 1);
                Node messageNode = this.getMessageNode(messagePartOperationName);
                hasOutput = (messageNode != null) && (messageNode.hasChildNodes());
            } catch (Exception ex) {
            }
        }
        return hasOutput;
    }

    public boolean hasOutputVoid(Node operationNode) {
        boolean hasOutputVoid = false;
        Node outputNode = this.getOutputNode(operationNode);
        if (outputNode != null) {
            try {
                String outputMessageName = this.getOutputMessageCleaned(operationNode);
                Node outputMessageNode = this.getMessageNode(outputMessageName);
                hasOutputVoid = (outputMessageNode != null);
            } catch (Exception ex) {
            }
        }
        return hasOutputVoid;
    }

    public Node getOutputNode(Node operationNode) {
        return XMLHelper.getNode(operationNode, "wsdl:output");
    }

    public String getOutputMessage(Node operationNode) {
        String outputMessage = TextContent.EMPTY;
        Node outputNode = this.getOutputNode(operationNode);
        outputMessage = XMLHelper.getAttributeValue(outputNode, "message");
        return outputMessage;
    }

    public String getOutputMessageCleaned(Node operationNode) {
        String outputMessage = this.getOutputMessage(operationNode);
        return outputMessage.substring(outputMessage.indexOf(":") + 1);
    }

    public boolean containsMessage(String messageName) {
        Node message = this.getMessageNode(messageName);
        return (message != null);
    }

    public Node getMessageNode(String messageName) {
        return XMLHelper.getFilteredNode(this.getRootDefinitionNode(), "wsdl:message", "name", messageName);
    }

    public String getMessagePartName(String messageName) {
        Node messageNode = this.getMessageNode(messageName);
        return this.getMessagePartName(messageNode);
    }

    public String getMessagePartName(Node messageNode) {
        return XMLHelper.getNode(messageNode, "wsdl:part").getAttributes().getNamedItem("name").getNodeValue();
    }

    public String getMessagePartElement(String messageName) {
        Node messageNode = this.getMessageNode(messageName);
        return this.getMessagePartElement(messageNode);
    }

    public String getMessagePartElement(Node messageNode) {
        String messagePartElement = TextContent.EMPTY;
        try {
            messagePartElement = XMLHelper.getNode(messageNode, "wsdl:part").getAttributes().getNamedItem("element").getNodeValue();
        } catch (Exception ex) {
        }
        return messagePartElement;
    }

    public Node getSchemaByNamespace(String namespace) {
        Node wsdlTypeNode = this.getWSDLTypeNode();
        return XMLHelper.getFilteredNode(wsdlTypeNode, "xs:schema", "targetNamespace", namespace);
    }

    public Node getSchemaElementByNamespaceAndElementName(String namespace, String elementName) {
        Node schemaNode = this.getSchemaByNamespace(namespace);
        return XMLHelper.getFilteredNode(schemaNode, "xs:element", "name", elementName);
    }

    public List<Node> getTypeSequenceElements(String namespace, String elementName) {
        Node schemaNode = this.getSchemaByNamespace(namespace);
        Node schemaElementNode = XMLHelper.getFilteredNode(schemaNode, "xs:element", "name", elementName);
        Node schemaElementComplexTypeNode = XMLHelper.getNode(schemaElementNode, "xs:complexType");
        Node schemaElementComplexTypeSequenceNode = XMLHelper.getNode(schemaElementComplexTypeNode, "xs:sequence");
        return XMLHelper.getNodesOnPattern(schemaElementComplexTypeSequenceNode, "xs:element");
    }

    public String getTypeSequenceElementName(Node typeSequenceElementNode) {
        return XMLHelper.getAttributeValue(typeSequenceElementNode, "name");
    }

    public String getTypeSequenceElementDataTypeText(Node typeSequenceElementNode) {
        return XMLHelper.getAttributeValue(typeSequenceElementNode, "type");
    }

    public WSDLDataType getTypeSequenceElementDataType(Node typeSequenceElementNode) {
        WSDLDataType wsdlDataType;
        String wsdlDataTypeText = this.getTypeSequenceElementDataTypeText(typeSequenceElementNode);
        wsdlDataType = WSDLDataTypeHelper.getWSDLDataType(wsdlDataTypeText);
        return wsdlDataType;
    }

    public boolean isNillable(Node typeSequenceElementNode) {
        boolean isNillable = false;
        try {
            String nillableText = XMLHelper.getAttributeValue(typeSequenceElementNode, "nillable");
            isNillable = nillableText.equalsIgnoreCase("true");
        } catch (Exception ex) {
        }
        return isNillable;
    }

    public boolean isArray(Node typeSequenceElementNode) {
        boolean isArray = false;
        try {
            String wsdlDataTypeText = this.getTypeSequenceElementDataTypeText(typeSequenceElementNode);
            WSDLDataType wsdlDataType = WSDLDataTypeHelper.getWSDLDataType(wsdlDataTypeText);
            String minOccursText = XMLHelper.getAttributeValue(typeSequenceElementNode, "minOccurs");
            String maxOccursText = XMLHelper.getAttributeValue(typeSequenceElementNode, "maxOccurs");
            isArray = ((minOccursText.equalsIgnoreCase("0")) && (maxOccursText.equalsIgnoreCase("unbounded")));
        } catch (Exception ex) {
        }
        return isArray;
    }
}
