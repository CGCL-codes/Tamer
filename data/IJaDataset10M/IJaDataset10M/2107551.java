package com.jedox.etl.core.config.source;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import com.jedox.etl.core.component.ConfigurationException;
import com.jedox.etl.core.node.Column;
import com.jedox.etl.core.node.IColumn;
import com.jedox.etl.core.node.IColumn.ElementTypes;
import com.jedox.etl.core.node.TreeElement;
import com.jedox.etl.core.node.TreeManager;
import com.jedox.etl.core.node.IColumn.ColumnTypes;
import com.jedox.etl.core.source.IView.Views;

/**
 * Basic Configurator class for Sources based on trees and thus providing multiple rendering possibilities as tables.
 * @author Christian Schwarzinger. Mail: christian.schwarzinger@proclos.com
 *
 */
public class TreeSourceConfigurator extends TableSourceConfigurator {

    private static final Log log = LogFactory.getLog(TreeSourceConfigurator.class);

    private TreeManager manager;

    private double parseWeight(String weightString) {
        double weight = 1.0;
        try {
            weight = Double.parseDouble(weightString.trim());
        } catch (Exception e) {
            log.error("Failed to set weigth of node. Falling back to default 1.");
        }
        return weight;
    }

    private void processNodeAttributes(TreeManager manager, TreeElement node, List<?> attributes) throws ConfigurationException {
        for (int j = 0; j < attributes.size(); j++) {
            Element attribute = (Element) attributes.get(j);
            String name = attribute.getAttributeValue("name", attribute.getName());
            IColumn temp = new Column();
            String type = attribute.getAttributeValue("type", "TEXT");
            ColumnTypes columnType;
            if (type.equalsIgnoreCase(ElementTypes.TEXT.toString())) {
                temp.setElementType(ElementTypes.TEXT.toString());
                columnType = ColumnTypes.attribute;
            } else if (type.equalsIgnoreCase(ElementTypes.NUMERIC.toString())) {
                temp.setElementType(ElementTypes.NUMERIC.toString());
                columnType = ColumnTypes.attribute;
            } else {
                temp.setElementType(ElementTypes.TEXT.toString());
                columnType = ColumnTypes.alias;
            }
            manager.addAttribute(node.getName(), name, attribute.getTextNormalize(), columnType, temp.getElementType());
        }
    }

    private void processNodes(List<?> children, TreeManager manager, TreeElement parent) throws ConfigurationException {
        for (int i = 0; i < children.size(); i++) {
            Element child = (Element) children.get(i);
            String name = child.getAttributeValue("name");
            String weightString = child.getAttributeValue("weight", "1");
            TreeElement c = manager.createNode(name, parent, parseWeight(weightString)).getElement();
            c.setElementType(child.getAttributeValue("type"));
            processNodeAttributes(manager, c, child.getChildren("attribute"));
            processNodeAttributes(manager, c, child.getChildren("rule"));
            processNodes(child.getChildren("constant"), manager, c);
        }
    }

    protected void setNodes() throws ConfigurationException {
        processNodes(getChildren(getXML(), "constant"), manager, manager.getRoot());
    }

    /**
	 * gets the root node name of the internal tree
	 * @return the root node name
	 * @throws ConfigurationException
	 */
    public String getRootName() throws ConfigurationException {
        return "#!__ROOT__!#" + getName();
    }

    /**
	 * Determines, if the internal tree should regard its root node as normal data node.
	 * @return true, if so. Default is false
	 * @throws ConfigurationException
	 */
    public boolean hasRoot() throws ConfigurationException {
        return false;
    }

    /**
	 * gets the Tree Node Manager, which manages the internal tree representation
	 * @return the Tree Node Manager
	 */
    public TreeManager getNodeManager() {
        return manager;
    }

    /**
	 * return the default format the tree should be rendered in
	 */
    public Views getFormat() throws ConfigurationException {
        return Views.FH;
    }

    public void configure() throws ConfigurationException {
        super.configure();
        manager = new TreeManager(getName(), getRootName());
        setNodes();
    }
}
