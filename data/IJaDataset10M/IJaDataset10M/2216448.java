package org.open.force.schema;

import java.rmi.RemoteException;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.open.force.common.OFProps;
import org.open.force.connector.OFConnection;
import org.open.force.connector.OFConnector;
import org.open.force.sforce.DescribeGlobalResult;
import org.open.force.sforce.DescribeGlobalSObjectResult;
import org.open.force.sforce.DescribeSObjectResult;
import org.open.force.sforce.Field;
import org.open.force.sforce.PicklistEntry;
import org.open.force.sforce.UnexpectedErrorFault;

/**
 * @author nlugert@openforcesoftware.com
 *
 */
public class OFGetSchema {

    private OFConnection connection;

    public OFGetSchema(OFProps props) {
        this.connection = OFConnector.getConnection(props);
    }

    public OFGetSchema(OFConnection connection) {
        this.connection = connection;
    }

    private void reconnect() {
        try {
            Thread.sleep(15000L);
            connection = OFConnector.reconnect(connection);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
	 * Get's the Global Schema from Salesforce as a JDOM Document
	 * @return JDOM Document
	 */
    public Document getGlobalSchema() {
        Document doc = new Document();
        Element rootNode = new Element(OFGetSchemaConst.TREE);
        Attribute rootNodeIdAttribute = new Attribute(OFGetSchemaConst.ID, OFGetSchemaConst.ZERO);
        rootNode.setAttribute(rootNodeIdAttribute);
        doc.setRootElement(rootNode);
        DescribeGlobalResult describeGlobalResult;
        try {
            describeGlobalResult = this.connection.getBinding().describeGlobal();
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        if (describeGlobalResult != null) {
            DescribeGlobalSObjectResult[] types = describeGlobalResult.getSobjects();
            if (types != null) {
                for (int i = 0; i < types.length; i++) {
                    try {
                        this.describeObject(types[i].getName(), rootNode);
                    } catch (Exception e) {
                        reconnect();
                        this.describeObject(types[i].getName(), rootNode);
                    }
                }
            }
        }
        return doc;
    }

    public Document getSchemaByObjectName(String objectName) {
        Document doc = new Document();
        Element rootNode = new Element(OFGetSchemaConst.TREE);
        Attribute rootNodeIdAttribute = new Attribute(OFGetSchemaConst.ID, OFGetSchemaConst.ZERO);
        rootNode.setAttribute(rootNodeIdAttribute);
        doc.setRootElement(rootNode);
        try {
            this.describeObject(objectName, rootNode);
        } catch (Exception e) {
            reconnect();
            this.describeObject(objectName, rootNode);
        }
        return doc;
    }

    public Document getSchemaByArrayOfObjectNames(String[] arrayOfObjectNames) {
        Document doc = new Document();
        Element rootNode = new Element(OFGetSchemaConst.TREE);
        Attribute rootNodeIdAttribute = new Attribute(OFGetSchemaConst.ID, OFGetSchemaConst.ZERO);
        rootNode.setAttribute(rootNodeIdAttribute);
        doc.setRootElement(rootNode);
        for (int i = 0; i < arrayOfObjectNames.length; i++) {
            try {
                this.describeObject(arrayOfObjectNames[i], rootNode);
            } catch (Exception e) {
                reconnect();
                this.describeObject(arrayOfObjectNames[i], rootNode);
            }
        }
        return doc;
    }

    private void describeObject(String sObjectName, Element xml) {
        try {
            DescribeSObjectResult describeSObjectResult = this.connection.getBinding().describeSObject(sObjectName);
            if (describeSObjectResult != null) {
                Field fields[] = describeSObjectResult.getFields();
                if (fields != null) {
                    String objectName = describeSObjectResult.getName();
                    Element child = new Element(OFGetSchemaConst.ITEM_LOWERCASE);
                    xml.addContent(child);
                    Attribute textAttribute = new Attribute(OFGetSchemaConst.TEXT, objectName);
                    Attribute idAttribute = new Attribute(OFGetSchemaConst.ID, objectName);
                    child.setAttribute(textAttribute);
                    child.setAttribute(idAttribute);
                    addFieldUserData(child, OFGetSchemaConst.REPLICABLE, Boolean.toString(describeSObjectResult.isReplicateable()));
                    addFieldUserData(child, OFGetSchemaConst.IS_OBJECT, Boolean.toString(true));
                    for (int f = 0; f < fields.length; f++) {
                        Field field = fields[f];
                        this.printField(field, child, objectName);
                    }
                }
            }
        } catch (UnexpectedErrorFault uef) {
            throw new RuntimeException(uef.getExceptionMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Failed to get " + ex.getMessage());
        }
    }

    /**
	 * 
	 * @param Field field
	 * @param Element xml
	 */
    public void printField(Field field, Element xml, String objectName) {
        Element fieldXML = new Element(OFGetSchemaConst.ITEM_LOWERCASE);
        xml.addContent(fieldXML);
        Attribute fieldName = new Attribute(OFGetSchemaConst.TEXT, field.getName());
        fieldXML.setAttribute(fieldName);
        Attribute fieldId = new Attribute(OFGetSchemaConst.ID, objectName + "_" + field.getName());
        fieldXML.setAttribute(fieldId);
        addFieldUserData(fieldXML, OFGetSchemaConst.NAME, field.getName());
        addFieldUserData(fieldXML, OFGetSchemaConst.TYPE, field.getType().getValue());
        addFieldUserData(fieldXML, OFGetSchemaConst.BYTE_LENGTH, Integer.toString(field.getByteLength()));
        addFieldUserData(fieldXML, OFGetSchemaConst.LENGTH, Integer.toString(field.getLength()));
        addFieldUserData(fieldXML, OFGetSchemaConst.DIGITS, Integer.toString(field.getDigits()));
        addFieldUserData(fieldXML, OFGetSchemaConst.LABEL, field.getLabel());
        addFieldUserData(fieldXML, OFGetSchemaConst.PRECISION, Integer.toString(field.getPrecision()));
        addFieldUserData(fieldXML, OFGetSchemaConst.SCALE, Integer.toString(field.getScale()));
        addFieldUserData(fieldXML, OFGetSchemaConst.IS_NILLABLE, Boolean.toString(field.isNillable()));
        addFieldUserData(fieldXML, OFGetSchemaConst.IS_CREATEABLE, Boolean.toString(field.isCreateable()));
        addFieldUserData(fieldXML, OFGetSchemaConst.IS_UPDATEABLE, Boolean.toString(field.isUpdateable()));
        addFieldUserData(fieldXML, OFGetSchemaConst.IS_CUSTOM, Boolean.toString(field.isCustom()));
        addFieldUserData(fieldXML, OFGetSchemaConst.IS_FILTERABLE, Boolean.toString(field.isFilterable()));
        addFieldUserData(fieldXML, OFGetSchemaConst.IS_RESTRICTED_PICKLIST, Boolean.toString(field.isRestrictedPicklist()));
        addFieldUserData(fieldXML, OFGetSchemaConst.IS_OBJECT, Boolean.toString(false));
        addFieldUserData(fieldXML, OFGetSchemaConst.HELP_TEXT, field.getInlineHelpText());
        String[] references = field.getReferenceTo();
        if (null != references) {
            String referenceString = "";
            for (int i = 0; i < references.length; i++) {
                if (i != 0) referenceString += ",";
                referenceString += references[i];
            }
            addFieldUserData(fieldXML, OFGetSchemaConst.REFERENCE_TO, referenceString);
        }
        PicklistEntry picklistValues[] = field.getPicklistValues();
        if (picklistValues != null) {
            String pickListString = "";
            if (field.isRestrictedPicklist()) {
                for (int p = 0; p < picklistValues.length; p++) {
                    if (p != 0) pickListString += ",";
                    pickListString += picklistValues[p].getValue();
                }
                addFieldUserData(fieldXML, OFGetSchemaConst.PICKLIST, pickListString);
            }
        }
    }

    private void addFieldUserData(Element fieldXML, String name, String value) {
        fieldXML.addContent(new Element(OFGetSchemaConst.USERDATA).setAttribute(new Attribute(OFGetSchemaConst.NAME_LOWERCASE, name)).addContent(value));
    }
}
