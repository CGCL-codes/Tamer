package org.in4ama.datasourcemanager.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import org.in4ama.datasourcemanager.AbstractDataSet;
import org.in4ama.datasourcemanager.DataSet;
import org.in4ama.datasourcemanager.DataSource;
import org.in4ama.datasourcemanager.ScalarDataSet;
import org.in4ama.datasourcemanager.exception.DataSourceException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/** Adapts XML node list to data sets. */
public class XmlDataSet extends AbstractDataSet {

    private static final Object EMPTY_VALUE = "";

    private final Map<String, XmlDataSet> dataSets = new HashMap<String, XmlDataSet>();

    private ArrayList<String> columnNames = new ArrayList<String>();

    private int currentRow = -1;

    private final NodeList nodeList;

    private final XPath xpath;

    /** Creates a new instance of the XmlDataSet. */
    public XmlDataSet(String name, DataSource dataSource, NodeList nodeList, XPath xpath) throws DataSourceException {
        super(name, dataSource);
        this.nodeList = nodeList;
        this.xpath = xpath;
        hasData = true;
        nextRow();
    }

    /** Moves the underlying iteration state to the next position. */
    public boolean nextRow() {
        if (hasData = (currentRow < nodeList.getLength() - 1)) {
            currentRow++;
            currentColumn = 0;
            dataSets.clear();
            columnNames = buildColumnNames(getCurrentNode());
        }
        return hasData;
    }

    /** Returns the current node from the underlying node list. */
    protected Node getCurrentNode() {
        return nodeList.item(currentRow);
    }

    /** Returns the names of the intermediate element names
	 * of the specified node. */
    private ArrayList<String> buildColumnNames(Node node) {
        ArrayList<String> names = new ArrayList<String>();
        if (node instanceof Element) {
            Element element = (Element) node;
            NamedNodeMap attributes = element.getAttributes();
            for (int i = 0; i < attributes.getLength(); i++) {
                String attrName = attributes.item(i).getNodeName();
                names.add("@" + attrName);
            }
        }
        NodeList childList = node.getChildNodes();
        for (int i = 0, cc = childList.getLength(); i < cc; i++) {
            Node childNode = childList.item(i);
            if (childNode instanceof Element) {
                Element childElem = (Element) childNode;
                names.add(childElem.getTagName());
            }
        }
        return names;
    }

    /** Gets the data set for the specified name. */
    @Override
    public DataSet getDataSet(String dataSetName) throws DataSourceException {
        String prefix = dataSetName;
        String suffix = null;
        int idx = dataSetName.indexOf(".");
        if (idx >= 0) {
            prefix = dataSetName.substring(0, idx);
            suffix = dataSetName.substring(idx + 1);
        }
        XmlDataSet childDataSet = dataSets.get(prefix);
        if (childDataSet == null) {
            dataSets.put(prefix, childDataSet = createDataSet(prefix));
        }
        return (suffix != null) ? childDataSet.getDataSet(suffix) : childDataSet;
    }

    /** Restores the specified inner data set object. */
    @Override
    public DataSet restoreDataSet(String dataSetName) throws DataSourceException {
        String prefix = dataSetName;
        String suffix = null;
        int idx = dataSetName.indexOf(".");
        if (idx >= 0) {
            prefix = dataSetName.substring(0, idx);
            suffix = dataSetName.substring(idx + 1);
        }
        XmlDataSet childDataSet = dataSets.get(prefix);
        if (childDataSet == null) {
            dataSets.put(prefix, childDataSet = createDataSet(prefix));
        }
        return (suffix != null) ? childDataSet.restoreDataSet(suffix) : childDataSet;
    }

    /** Returns the XML data set at the specified index. */
    @Override
    public DataSet getDataSet(int colIdx) throws DataSourceException {
        int colCount = getColumnCount();
        if ((colIdx < 0) || (colIdx >= colCount)) {
            if (allowEmpty) return ScalarDataSet.EMPTY;
            String msg = "Can't retrieve the data set at the index '" + colIdx + "'." + "The data set '" + name + "' contains '" + getColumnCount() + "' fields.";
            throw new DataSourceException(msg);
        }
        return getDataSet(getColumnName(colIdx));
    }

    /** Creates a new XML data set object from the specified XPATH. */
    private XmlDataSet createDataSet(String xpathQuery) throws DataSourceException {
        XmlDataSet dataSet = null;
        try {
            XPathExpression expr = xpath.compile(xpathQuery);
            Node currentNode = getCurrentNode();
            Object result = expr.evaluate(currentNode, XPathConstants.NODESET);
            NodeList childNodeList = (NodeList) result;
            dataSet = new XmlDataSet(xpathQuery, dataSource, childNodeList, xpath);
        } catch (Exception ex) {
            String msg = "Unable to create the XML data " + "set from the XPath query: " + xpathQuery;
            throw new DataSourceException(msg, ex);
        }
        return dataSet;
    }

    /** Gets the number of columns in this data set. */
    public int getColumnCount() {
        return columnNames.size();
    }

    /** Returns the name of the column at the specified index. */
    public String getColumnName(int idx) {
        return columnNames.get(idx);
    }

    /** Returns a list containing names of all available columns. */
    public ArrayList<String> getColumnNames() {
        return columnNames;
    }

    /** Returns the number of rows in this data set. */
    public int getRowCount() {
        return nodeList.getLength();
    }

    /** Returns the index of the current row. */
    public int getCurrentRow() {
        return currentRow;
    }

    /** Returns the value of the current XML node. */
    public Object getValue() throws DataSourceException {
        if (!hasData) {
            String msg = "The data set '" + getName() + "' contains no data.";
            throw new DataSourceException(msg);
        }
        Object value = EMPTY_VALUE;
        Node node = getCurrentNode();
        value = node.getTextContent();
        return (value != null) ? value : EMPTY_VALUE;
    }
}
