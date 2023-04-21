package com.liferay.util.xml.descriptor;

import org.dom4j.Document;
import com.liferay.util.xml.ElementIdentifier;

/**
 * <a href="TilesDefsDescriptor.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Jorge Ferrer
 * @version $Revision: 1.4 $
 *
 */
public class TilesDefsDescriptor extends SimpleXMLDescriptor {

    public boolean canHandleType(String doctype, Document root) {
        if (doctype.indexOf("tiles-config") != -1) {
            return true;
        } else {
            return false;
        }
    }

    public String[] getRootChildrenOrder() {
        return _ROOT_CHILDREN_ORDER;
    }

    public ElementIdentifier[] getElementsIdentifiedByAttribute() {
        return _ELEMENTS_IDENTIFIED_BY_ATTR;
    }

    public String[] getUniqueElements() {
        return _UNIQUE_ELEMENTS;
    }

    private static final String[] _ROOT_CHILDREN_ORDER = { "definition" };

    private static final ElementIdentifier[] _ELEMENTS_IDENTIFIED_BY_ATTR = { new ElementIdentifier("definition", "name") };

    private static final String[] _UNIQUE_ELEMENTS = {};
}
