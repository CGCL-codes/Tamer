package com.jedox.etl.components.extract;

import java.util.HashMap;
import java.util.HashSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.palo.api.Attribute;
import org.palo.api.Connection;
import org.palo.api.Database;
import org.palo.api.Dimension;
import org.palo.api.ElementNode;
import org.palo.api.Element;
import com.jedox.etl.components.config.extract.DimensionExtractConfigurator;
import com.jedox.etl.components.config.extract.DimensionFilterDefinition;
import com.jedox.etl.core.component.InitializationException;
import com.jedox.etl.core.component.RuntimeException;
import com.jedox.etl.core.connection.IConnection;
import com.jedox.etl.core.connection.IOLAPConnection;
import com.jedox.etl.core.extract.IExtract;
import com.jedox.etl.core.node.IColumn;
import com.jedox.etl.core.node.TreeElement;
import com.jedox.etl.core.node.TreeManager;
import com.jedox.etl.core.node.TreeNode;
import com.jedox.etl.core.source.TreeSource;

/**
 * 
 * @author Christian Schwarzinger. Mail: christian.schwarzinger@proclos.com
 *
 */
public class DimensionExtract extends TreeSource implements IExtract {

    private Dimension dimension;

    private String dimensionName;

    private DimensionFilter filter;

    private DimensionFilterDefinition definition;

    private String[] dimensionAttributesNames;

    private Object[] elementAttributesValues;

    private int attributesLength;

    private boolean withAttributes;

    private static final Log log = LogFactory.getLog(DimensionExtract.class);

    public DimensionExtract() {
        setConfigurator(new DimensionExtractConfigurator());
    }

    public DimensionExtractConfigurator getConfigurator() {
        return (DimensionExtractConfigurator) super.getConfigurator();
    }

    public IOLAPConnection getConnection() throws RuntimeException {
        IConnection connection = super.getConnection();
        if ((connection != null) && (connection instanceof IOLAPConnection)) return (IOLAPConnection) connection;
        throw new RuntimeException("OLAP connection is needed for extract " + getName() + ".");
    }

    private void generateTree(ElementNode node, ElementNode parent, HashMap<ElementNode, TreeElement> map, Attribute[] attributes, HashSet<String> accepted) throws RuntimeException {
        try {
            if (node == null) return;
            TreeElement p = map.get(parent);
            TreeNode c = getTreeManager().createNode(node.getElement().getName(), p, (node.getConsolidation() != null) ? node.getConsolidation().getWeight() : 1);
            if (node.getElement().getType() == Element.ELEMENTTYPE_STRING) c.setElementType(IColumn.ElementTypes.TEXT.toString());
            map.put(node, c.getElement());
            if (withAttributes) {
                Element e = node.getElement();
                Object[] attributeValues = e.getAttributeValues();
                for (int i = 0; i < attributesLength; i++) {
                    elementAttributesValues[i] = attributeValues[i].toString();
                    getTreeManager().addAttribute(c.getName(), dimensionAttributesNames[i], elementAttributesValues[i], IColumn.ColumnTypes.attribute);
                }
            }
            ElementNode[] children = node.getChildren();
            for (ElementNode en : children) {
                generateTree(en, node, map, attributes, accepted);
            }
            if (!accepted.contains(c.getName())) {
                getTreeManager().removeElement(c.getName());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to add Dimension Element " + node.getElement().getName() + " to internal representation: " + e.getMessage());
        }
    }

    protected TreeManager buildTree() throws RuntimeException {
        super.buildTree();
        Connection con = getConnection().open();
        Database d = con.getDatabaseByName(getConnection().getDatabase());
        if (d != null) {
            dimension = d.getDimensionByName(dimensionName);
            if (dimension != null) {
                filter = new DimensionFilter(dimension, definition);
                if (!filter.isEmpty()) filter.configure();
                ElementNode[] tree = dimension.getDefaultHierarchy().getElementsTree();
                HashMap<ElementNode, TreeElement> map = new HashMap<ElementNode, TreeElement>();
                Attribute[] attributes = null;
                if (withAttributes) {
                    attributes = dimension.getDefaultHierarchy().getAttributes();
                    attributesLength = attributes.length;
                    dimensionAttributesNames = new String[attributesLength];
                    elementAttributesValues = new Object[attributesLength];
                    for (int i = 0; i < attributesLength; i++) {
                        dimensionAttributesNames[i] = attributes[i].getName();
                        if (attributes[i].getType() == Attribute.TYPE_NUMERIC) {
                            log.debug("Attribute " + attributes[i].getName() + " has type Numeric but will be treated as Text, because attributes are always treated as type Text");
                        }
                    }
                }
                for (ElementNode node : tree) {
                    generateTree(node, null, map, attributes, filter.getAccepted());
                }
            } else throw new RuntimeException("Dimension " + dimensionName + " not found in database " + getConnection().getDatabase() + ".");
        } else throw new RuntimeException("Database " + getConnection().getDatabase() + " not found. Failed to read Dimension " + getName());
        return getTreeManager();
    }

    public void setDimensionName(String dimensionName) {
        this.dimensionName = dimensionName;
    }

    public void setWithAttributes(boolean withAttributes) {
        this.withAttributes = withAttributes;
    }

    public void init() throws InitializationException {
        try {
            super.init();
            definition = getConfigurator().getFilterDefinition_exp();
            setDimensionName(getConfigurator().getDimensionName());
            setWithAttributes(getConfigurator().getWithAttributes());
        } catch (Exception e) {
            throw new InitializationException(e);
        }
    }
}
