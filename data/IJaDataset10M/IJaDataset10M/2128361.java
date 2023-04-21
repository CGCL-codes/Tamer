package pdmeditor.PDM.DataElement;

import pdmeditor.PDM.Operation.PDMMOperation;
import pdmeditor.PDM.PDMDesignModel;
import java.io.*;
import java.util.HashMap;
import org.w3c.dom.*;
import pdmeditor.PDM.DataElement.ExtendedNode.PDMMSystemNode;
import pdmeditor.PDM.FileHandling.XMLNodeCreator;

/**
 *
 * @author P.M. de Lange
 */
public class PDMMDataElement extends PDMMSystemNode {

    private PDMDesignModel modelReference;

    public void setReference(PDMDesignModel inModel) {
        modelReference = inModel;
    }

    private String dataElementID;

    private Integer dataElementNR;

    private String description;

    private Object Type;

    private Object initial;

    private Integer elementType;

    public void setElementType(Integer inElementType) {
        elementType = inElementType;
    }

    public Integer getElementType() {
        return elementType;
    }

    private ElementObjectType elementObjectType;

    public void setElementObjectType(ElementObjectType inElementObjectType) {
        elementObjectType = inElementObjectType;
    }

    public ElementObjectType getElementObjectType() {
        return elementObjectType;
    }

    private Boolean miElement = false;

    public void setMIValue(Boolean inMIElement) {
        miElement = inMIElement;
    }

    public Boolean getMIValue() {
        return miElement;
    }

    private boolean hasValue = false;

    public void triggerHasValue() {
        hasValue = true;
    }

    public void resetHasValue() {
        if (elementType == 2) {
            hasValue = true;
        } else {
            hasValue = false;
        }
    }

    public boolean getHasValue() {
        return hasValue;
    }

    private double xPosition;

    private double yPosition;

    private double radius;

    private HashMap elementTypes = new HashMap();

    private HashMap designTypes = new HashMap();

    private void setElementTypes() {
        elementTypes.put("DataElementDesign", 2);
        elementTypes.put("Range", 4);
    }

    private void setDesignTypes() {
        designTypes.put("xPosition", 1);
        designTypes.put("yPosition", 2);
        designTypes.put("radius", 3);
        designTypes.put("elementType", 4);
        designTypes.put("elementObjectType", 5);
        designTypes.put("Description", 6);
        designTypes.put("caseExistenceProbability", 7);
    }

    /**
     * Returns an array with all the properties of the element in text form.
     *
     * @return an array with all the properties of the element in text form.
     */
    public String[] returnProperties() {
        String tempString[] = new String[2];
        tempString[0] = "ID: " + dataElementID;
        tempString[1] = "Description: " + description;
        return tempString;
    }

    /**
     * Creates a Data Element with identifier "id". An integer is added to the .nl
     * 
     *
     * @param model The design model in which to create the data element.
     * @param id String The identifier of the data element.
     */
    public PDMMDataElement(PDMDesignModel model, String id) {
        this.dataElementID = id + model.returnI();
        this.description = "description";
        this.modelReference = model;
        this.setCaseExistenceProbability("0.0");
        this.setElementObjectType(this.modelReference.objectTypes.getElementType(1));
        model.addDataElement(this);
    }

    /**
     * Creates a data element with identifier "id" and description "descr" within the PDM model "model"
     *
     * @param model PDMModel
     * @param id String
     * @param descr String
     */
    public PDMMDataElement(PDMDesignModel model, String id, String descr, String caseExistence, ElementObjectType type) {
        this.dataElementID = id;
        this.description = descr;
        this.modelReference = model;
        this.setCaseExistenceProbability(caseExistence);
        this.setElementObjectType(type);
        model.addDataElement(this);
    }

    /**
     * Returns the identifier of the Data Element
     *
     * @return String
     */
    public String getID() {
        return dataElementID;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param id
     */
    public void setID(String id) {
        System.out.println("dataElementID: " + dataElementID);
        String previousID = dataElementID;
        dataElementID = id;
        System.out.println(modelReference);
        modelReference.updateDataElement(this, previousID);
    }

    /**
     *
     * @return
     */
    public Double getXPosition() {
        return xPosition;
    }

    /**
     *
     * @return
     */
    public Double getYPosition() {
        return yPosition;
    }

    /**
     *
     * @return
     */
    public Double getRadius() {
        return radius;
    }

    /**
     * Returns true if the data element is one of the input or output elements
     * of the operation. Returns false otherwise
     *
     * @param operation PDMOperation
     * @return Boolean
     */
    public Boolean intersectsWith(PDMMOperation operation) {
        Boolean result = false;
        if (operation.input.containsKey(this.dataElementID)) {
            result = true;
        } else if (operation.output.containsKey(this.dataElementID)) {
            result = true;
        }
        return result;
    }

    /**
     *
     * @param inX
     * @param inY
     */
    public void setXYPosition(Double inX, Double inY) {
        xPosition = inX;
        yPosition = inY;
    }

    /**
     *
     * @param inRadius
     */
    public void setRadius(Double inRadius) {
        radius = inRadius;
    }

    /**
     *
     * @param inDescription
     */
    public void setDescription(String inDescription) {
        description = inDescription;
    }

    public ElementObjectType[] getElementObjectTypes() {
        return modelReference.objectTypes.objectTypeList();
    }

    public Element writeToV1PDM(Document doc, Element root) throws IOException {
        Element child = doc.createElement("DataElement");
        child.setAttribute("DataElementID", dataElementID);
        return child;
    }

    /**
     *
     * @param doc
     * @param root
     * @return
     * @throws IOException
     */
    public Element writeToPDMDesign(Document doc, Element root) throws IOException {
        Element child = writeToV1PDM(doc, root);
        Element designElement = doc.createElement("DataElementDesign");
        designElement.appendChild(XMLNodeCreator.returnXMLNode(doc, "xPosition", xPosition));
        designElement.appendChild(XMLNodeCreator.returnXMLNode(doc, "yPosition", yPosition));
        designElement.appendChild(XMLNodeCreator.returnXMLNode(doc, "radius", radius));
        designElement.appendChild(XMLNodeCreator.returnXMLNode(doc, "elementType", elementType));
        designElement.appendChild(XMLNodeCreator.returnXMLNode(doc, "elementObjectType", elementObjectType.getID()));
        designElement.appendChild(XMLNodeCreator.returnXMLNode(doc, "Description", description));
        designElement.appendChild(XMLNodeCreator.returnXMLNode(doc, "caseExistenceProbability", this.getCaseExistenceProbability()));
        child.appendChild(designElement);
        if (miElement) {
            child.appendChild(writeRange(doc, root));
        }
        return child;
    }

    public String returnDataElementExport(boolean inHeaderCheck) {
        String outputs = new String();
        if (inHeaderCheck) {
            outputs = outputs.concat("Data Elemend ID;Description;Element Object Type;Element Type;Case Existence Probability\n");
        }
        outputs = outputs + dataElementID;
        outputs = outputs.concat(";" + description);
        outputs = outputs.concat(";" + elementObjectType.toString());
        outputs = outputs.concat(";" + elementType);
        if (elementType == 2) {
            outputs = outputs.concat(";" + getCaseExistenceProbability());
        }
        outputs = outputs.concat("\n");
        return outputs;
    }

    /**
     *
     * @param rootNode
     * @param model
     */
    public PDMMDataElement(Node rootNode, PDMDesignModel model) {
        modelReference = model;
        setElementTypes();
        setDesignTypes();
        NodeList nodes = rootNode.getChildNodes();
        System.out.println("# Childs: " + nodes.getLength());
        dataElementID = rootNode.getAttributes().getNamedItem("DataElementID").getNodeValue();
        for (int i = 1; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            System.out.println(node.getNodeName());
            if (nodes.item(i).getNodeType() != Node.TEXT_NODE) {
                switch((Integer) (elementTypes.get(node.getNodeName()))) {
                    case 2:
                        parseDesignDataElement(node);
                        break;
                    case 4:
                        parseRange(node);
                        miElement = true;
                        break;
                }
            }
        }
        elementTypes = null;
        designTypes = null;
    }

    private void parseDesignDataElement(Node rootNode) {
        NodeList nodes = rootNode.getChildNodes();
        for (int i = 1; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeType() != Node.TEXT_NODE) {
                Node node = nodes.item(i);
                System.out.println("Node name: " + node.getNodeName() + " Node value: " + node.getTextContent());
                switch((Integer) (designTypes.get(node.getNodeName()))) {
                    case 1:
                        xPosition = Double.parseDouble(node.getTextContent());
                        break;
                    case 2:
                        yPosition = Double.parseDouble(node.getTextContent());
                        break;
                    case 3:
                        radius = Double.parseDouble(node.getTextContent());
                        break;
                    case 4:
                        elementType = Integer.parseInt(node.getTextContent());
                        break;
                    case 5:
                        System.out.println(node.getTextContent());
                        this.setElementObjectType(this.modelReference.objectTypes.getElementType(Integer.parseInt(node.getTextContent())));
                        System.out.println(node.getTextContent());
                        break;
                    case 6:
                        description = (node.getTextContent());
                        break;
                    case 7:
                        setCaseExistenceProbability(node.getTextContent());
                        break;
                }
            }
        }
    }
}
