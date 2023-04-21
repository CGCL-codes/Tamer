package org.cristal.xml.metadata.torque;

import org.cristal.xml.BaseElement;
import org.jdom.Element;

/**
 * 
 * @jdo.persistence-capable
 * @sql.table
 * 		table-name="Index"
 * @author T. Kia Ntoni
 * 
 * 19 nov. 2005 
 * Index @version 
 */
public class Index extends BaseElement {

    public static final String ELEMENTNAME = "index";

    public static final String ATT_NAME = "name";

    private static final String[] ATTRIBUTES_NAMES = null;

    private static final String[] CHILDREN_NAMES = null;

    public Index() throws Exception {
        super(ELEMENTNAME, ATTRIBUTES_NAMES, CHILDREN_NAMES);
    }

    public Index(Element element) throws Exception {
        super(element, ELEMENTNAME, ATTRIBUTES_NAMES, CHILDREN_NAMES);
    }

    public Index(BaseElement baseElement) throws Exception {
        super(baseElement, ELEMENTNAME, ATTRIBUTES_NAMES, CHILDREN_NAMES);
    }

    @Override
    public BaseElement addChild(Element child) {
        if (child.getName().equals(IndexColumn.ELEMENTNAME)) {
            element.addContent(child);
        }
        return this;
    }

    @Override
    public boolean hasValidAttributes(Element element) {
        String[] names = { ATT_NAME };
        attributesNames = names;
        return super.hasValidAttributes(element);
    }

    @Override
    public boolean isValidChild(Element child) {
        String[] names = { IndexColumn.ELEMENTNAME };
        childrenNames = names;
        return super.isValidChild(child);
    }

    @Override
    public boolean isValidElement(Element element) {
        elementName = ELEMENTNAME;
        return super.isValidElement(element);
    }

    public String getName() {
        return element.getAttributeValue(ATT_NAME);
    }

    public void setName(String name) {
        element.setAttribute(ATT_NAME, name);
    }
}
