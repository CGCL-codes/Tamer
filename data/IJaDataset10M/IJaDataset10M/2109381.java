package yapgen.base.type;

import java.util.logging.Level;
import java.util.logging.Logger;
import yapgen.base.relation.Relation;
import yapgen.base.util.AttributeMap;
import yapgen.base.util.XmlUtils;

/**
 *
 * @author riccardo
 */
public class RelationType implements Type {

    private Relation value;

    public RelationType() {
    }

    @Override
    public void valueFromString(String stringValue) {
        value = new Relation();
        value.valueFromString(stringValue);
    }

    @Override
    public String valueToString() {
        return value.valueToString();
    }

    @Override
    public String toString() {
        return value != null ? value.toString() : "null";
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public Object clone() {
        RelationType copy = null;
        try {
            copy = (RelationType) super.clone();
            copy.value = (Relation) value.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(RelationType.class.getName()).log(Level.SEVERE, null, ex);
        }
        return copy;
    }

    @Override
    public String toXml() {
        StringBuilder builder = new StringBuilder();
        AttributeMap attributes = new AttributeMap();
        builder.append(XmlUtils.tagString("relation", attributes, value.toXml()));
        return builder.toString();
    }
}
