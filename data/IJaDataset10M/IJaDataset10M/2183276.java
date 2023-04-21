package org.isi.monet.modelling.editor.model.properties;

import org.isi.monet.modelling.editor.model.Constants;
import org.isi.monet.modelling.editor.model.declarations.Declaration;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class FieldsOrder extends Property {

    public FieldsOrder(Declaration parent, Node node) {
        super(parent);
        this.typeProperty = Constants.PROPERTY_FIELDSORDER;
        NamedNodeMap nodeAttr = node.getAttributes();
        Node attr = nodeAttr.getNamedItem("value");
        if (attr != null) this.valueProperty = attr.getNodeValue(); else this.valueProperty = "";
    }

    public FieldsOrder(Declaration parent) {
        super(parent);
        this.typeProperty = Constants.PROPERTY_FIELDSORDER;
        this.valueProperty = "";
    }
}
