package net.sourceforge.ecldbtool.model.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Vector;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import net.sourceforge.ecldbtool.model.Column;
import net.sourceforge.ecldbtool.model.DatabaseModel;
import net.sourceforge.ecldbtool.model.ForeignKey;
import net.sourceforge.ecldbtool.model.PrimaryKey;
import net.sourceforge.ecldbtool.model.Schema;
import net.sourceforge.ecldbtool.model.Table;

public class DatabaseModelXMLReader {

    Vector contexts = new Vector();

    DatabaseModel model;

    InputStream is;

    public DatabaseModelXMLReader() {
    }

    public void setInputStream(InputStream is) {
        this.is = is;
    }

    public DatabaseModel readDatabaseModel() {
        try {
            InputSource inputSource = new InputSource(is);
            DOMParser parser = new DOMParser();
            parser.parse(inputSource);
            Node node = parser.getDocument().getDocumentElement();
            model = new DatabaseModel();
            readDatabaseModel(model, node);
            is.close();
            return model;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void readDatabaseModel(DatabaseModel model, Node node) {
        NodeList childNodeList = node.getChildNodes();
        for (int i = 0; childNodeList != null && i < childNodeList.getLength(); i++) {
            Node childNode = childNodeList.item(i);
            if (childNode.getNodeName().equals("Schema")) {
                Schema schema = new Schema();
                model.addSchema(schema);
                readSchema(schema, childNode);
            }
        }
        for (int i = 0; i < contexts.size(); i++) {
            Object[] context = ((Object[]) contexts.elementAt(i));
            ForeignKey foreignKey = (ForeignKey) context[0];
            String schemaName = (String) context[1];
            String tableName = (String) context[2];
            setForeignKeyReferenceTable(foreignKey, schemaName, tableName);
        }
    }

    void setForeignKeyReferenceTable(ForeignKey foreignKey, String schemaName, String tableName) {
        Schema schema = null;
        if (schemaName == null || schemaName.equals("")) {
            schema = foreignKey.getTable().getShema();
        } else {
            schema = model.getSchemaByName(schemaName);
        }
        foreignKey.setReferencedTable(schema.getTableByName(tableName));
    }

    public void readSchema(Schema schema, Node node) {
        schema.setName(getAttributeValue(node, "name"));
        NodeList childNodeList = node.getChildNodes();
        for (int i = 0; childNodeList != null && i < childNodeList.getLength(); i++) {
            Node childNode = childNodeList.item(i);
            if (childNode.getNodeName().equals("Table")) {
                Table table = new Table();
                schema.addTable(table);
                readTable(table, childNode);
            }
        }
    }

    public void readTable(Table table, Node node) {
        table.setName(getAttributeValue(node, "name"));
        NodeList childNodeList = node.getChildNodes();
        for (int i = 0; childNodeList != null && i < childNodeList.getLength(); i++) {
            Node childNode = childNodeList.item(i);
            if (childNode.getNodeName().equals("Column")) {
                Column column = new Column();
                table.addColumn(column);
                readColumn(column, childNode);
            }
            if (childNode.getNodeName().equals("PrimaryKey")) {
                PrimaryKey primaryKey = new PrimaryKey();
                table.setPrimaryKey(primaryKey);
                readPrimaryKey(primaryKey, childNode);
            }
            if (childNode.getNodeName().equals("ForeignKey")) {
                ForeignKey foreignKey = new ForeignKey();
                table.addForeignKey(foreignKey);
                readForeignKey(foreignKey, childNode);
            }
        }
    }

    public void readPrimaryKey(PrimaryKey primaryKey, Node node) {
        primaryKey.setName(getAttributeValue(node, "name"));
        NodeList childNodeList = node.getChildNodes();
        for (int i = 0; childNodeList != null && i < childNodeList.getLength(); i++) {
            Node childNode = childNodeList.item(i);
            if (childNode.getNodeName().equals("Column")) {
                String columnName = getAttributeValue(childNode, "name");
                Column column = primaryKey.getTable().getColumnByName(columnName);
                primaryKey.addColumn(column);
            }
        }
    }

    public void readForeignKey(ForeignKey foreignKey, Node node) {
        foreignKey.setName(getAttributeValue(node, "name"));
        foreignKey.setDeleteRule(getAttributeValue(node, "deleterule"));
        foreignKey.setUpdateRule(getAttributeValue(node, "updaterule"));
        String refSchema = getAttributeValue(node, "refschema");
        String refTable = getAttributeValue(node, "reftable");
        NodeList childNodeList = node.getChildNodes();
        for (int i = 0; childNodeList != null && i < childNodeList.getLength(); i++) {
            Node childNode = childNodeList.item(i);
            if (childNode.getNodeName().equals("Column")) {
                String columnName = getAttributeValue(childNode, "name");
                Column column = foreignKey.getTable().getColumnByName(columnName);
                foreignKey.addColumn(column);
            }
        }
        contexts.add(new Object[] { foreignKey, refSchema, refTable });
    }

    public void readColumn(Column column, Node node) {
        column.setName(getAttributeValue(node, "name"));
        column.setTypeName(getAttributeValue(node, "type"));
        column.setNullable("true".equals(getAttributeValue(node, "nullable")));
    }

    public static void main(String[] args) {
        try {
            DatabaseModelXMLReader r = new DatabaseModelXMLReader();
            FileInputStream fis = new FileInputStream(new File("c:/test.xml"));
            r.setInputStream(fis);
            DatabaseModel model = r.readDatabaseModel();
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getAttributeValue(Node n, String key) {
        NamedNodeMap attributes = n.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node current = attributes.item(i);
            if (current.getNodeName().equals(key)) return current.getNodeValue();
        }
        return "";
    }
}
