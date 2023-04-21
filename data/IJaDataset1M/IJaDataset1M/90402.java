package org.cristal.xml.metadata.jdo;

import org.cristal.xml.BaseElement;
import org.jdom.Element;

public class Sequence extends JdoBaseElement {

    public static final String ELEMENTNAME = "sequence";

    public static final String[] ATTRIBUTES_NAMES = null;

    public static final String[] CHILDREN_NAMES = { Extension.ELEMENTNAME, Query.ELEMENTNAME, Package.ELEMENTNAME };

    public Sequence() {
        super(ELEMENTNAME, ATTRIBUTES_NAMES, CHILDREN_NAMES);
    }

    public Sequence(Element element) {
        super(element, ELEMENTNAME, ATTRIBUTES_NAMES, CHILDREN_NAMES);
    }

    public Sequence(BaseElement baseElement) {
        super(baseElement, ELEMENTNAME, ATTRIBUTES_NAMES, CHILDREN_NAMES);
    }
}
