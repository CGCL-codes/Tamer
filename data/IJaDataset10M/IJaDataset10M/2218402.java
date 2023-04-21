package org.ws4d.java.schema;

import java.io.IOException;
import org.ws4d.java.structures.LinkedList;
import org.xmlpull.v1.XmlSerializer;

/**
 * Container for complexType:choice.
 */
public class ChoiceContainer extends ElementContainer {

    public ChoiceContainer() {
        super(new LinkedList());
    }

    public String toString() {
        int all = getElementCount();
        return "ChoiceContainer [ own=" + elementCount + ", inherit=" + (all - elementCount) + ", all=" + all + ", min=" + min + ", max=" + max + ", container=" + container + " ]";
    }

    public int getSchemaIdentifier() {
        return XSD_CHOICEMODEL;
    }

    public int getContainerType() {
        return ComplexType.CONTAINER_CHOICE;
    }

    void serialize(XmlSerializer serializer, Schema schema) throws IOException {
        serializer.startTag(XMLSCHEMA_NAMESPACE, TAG_CHOICE);
        serialize0(serializer, schema);
        serializer.endTag(XMLSCHEMA_NAMESPACE, TAG_CHOICE);
    }
}
