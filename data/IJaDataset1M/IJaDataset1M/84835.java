package org.cristal.xml.metadata.jdo;

import org.apache.commons.lang.StringUtils;
import org.cristal.xml.BaseElement;
import org.cristal.xml.InvalidAttributeValueException;
import org.jdom.Element;

/**
 * 
 * @jdo.persistence-capable
 * @sql.table
 * 		table-name="AbstractDiscriminator"
 * @author T. Kia Ntoni
 * 
 * 29 nov. 2005 
 * AbstractDiscriminator @version 
 */
public abstract class AbstractDiscriminator extends JdoBaseElement {

    public static final String ATT_INDEXED = "indexed";

    public static final String ATT_INDEXED_VALUE_TRUE = "true";

    public static final String ATT_INDEXED_VALUE_FALSE = "false";

    public static final String ATT_INDEXED_VALUE_UNIQUE = "unique";

    public static final String ATT_COLUMN = "column";

    public static final String[] CHILDREN_NAMES = { Extension.ELEMENTNAME, Column.ELEMENTNAME, Index.ELEMENTNAME };

    /**
     * @param name
     * @param AttributesNames
     * @param childrenNames
     */
    public AbstractDiscriminator(String name, String[] AttributesNames, String[] childrenNames) {
        super(name, AttributesNames, childrenNames);
    }

    /**
     * @param element
     * @param name
     * @param attributesNames
     * @param childrenNames
     */
    public AbstractDiscriminator(Element element, String name, String[] attributesNames, String[] childrenNames) {
        super(element, name, attributesNames, childrenNames);
    }

    /**
     * @param baseElement
     * @param name
     * @param attributesNames
     * @param childrenNames
     */
    public AbstractDiscriminator(BaseElement baseElement, String name, String[] attributesNames, String[] childrenNames) {
        super(baseElement, name, attributesNames, childrenNames);
    }

    public String getIndexed() {
        return element.getAttributeValue(ATT_INDEXED);
    }

    public void setIndexed(String name) {
        if (StringUtils.equals(name, ATT_INDEXED_VALUE_FALSE) || StringUtils.equals(name, ATT_INDEXED_VALUE_TRUE) || StringUtils.equals(name, ATT_INDEXED_VALUE_UNIQUE)) {
            throw new InvalidAttributeValueException("TODO");
        }
        element.setAttribute(ATT_INDEXED, name);
    }

    public String getColumn() {
        return element.getAttributeValue(ATT_COLUMN);
    }

    public void setColumn(String name) {
        element.setAttribute(ATT_COLUMN, name);
    }

    protected void addColumn(Element child) {
        if (!StringUtils.equals(Column.ELEMENTNAME, child.getName())) {
            return;
        }
        int index = maxIndexOf(Column.ELEMENTNAME);
        if (index != -1) {
            element.addContent(index + 1, child);
            return;
        }
        index = firstIndexOf(Index.ELEMENTNAME);
        if (index != -1) {
            element.addContent(index, child);
            return;
        }
        element.addContent(child);
    }

    protected void addIndex(Element child) {
        if (!StringUtils.equals(Index.ELEMENTNAME, child.getName())) {
            return;
        }
        int index = firstIndexOf(Index.ELEMENTNAME);
        if (index != -1) {
            element.setContent(index, child);
            return;
        }
        index = maxIndexOf(Column.ELEMENTNAME);
        if (index != -1) {
            element.addContent(index + 1, child);
            return;
        }
        element.addContent(index, child);
    }

    public BaseElement addChild(Element child) {
        addExtension(child);
        addColumn(child);
        addIndex(child);
        return this;
    }

    public boolean isValidChild(Element child) {
        if (!super.isValidChild(child)) {
            return false;
        } else if (StringUtils.equals(Extension.ELEMENTNAME, child.getName())) {
            return new Extension().isValidElement(child);
        } else if (StringUtils.equals(Index.ELEMENTNAME, child.getName())) {
            return new Index().isValidElement(child);
        } else {
            return new Column().isValidElement(child);
        }
    }
}
