package monet.editors.model.propertiesDeclarations;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import monet.editors.model.Declaration;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class PropertyMultiple extends Property {

    public PropertyMultiple(Declaration parent) {
        super(parent);
        this.nameProperty = "multiple";
        this.valueProperty = "";
        if (attributes == null) attributes = new ArrayList<Property>();
        attributes.add(new PropertySimple(this, "value", "false"));
    }

    public PropertyMultiple(Declaration parent, Node declaration) {
        super(parent);
        NamedNodeMap attr = declaration.getAttributes();
        if (attributes == null) attributes = new ArrayList<Property>();
        Node pageAttribute = attr.getNamedItem("value");
        if (pageAttribute != null) attributes.add(new PropertySimple(this, "value", pageAttribute.getNodeValue())); else attributes.add(new PropertySimple(this, "value", "false"));
        this.nameProperty = declaration.getNodeName();
        this.valueProperty = "";
    }

    public void setValueProperty(String value) {
    }

    @Override
    public void appendText(PrintWriter writer, String indent) {
        writer.print(indent + "<" + nameProperty + " ");
        Iterator<Property> iter = attributes.iterator();
        while (iter.hasNext()) {
            Property prop = iter.next();
            if (prop instanceof PropertySimple) prop.appendText(writer, "");
        }
        writer.print("></" + nameProperty + ">");
    }

    @Override
    public Property[] getAttributes() {
        List<Property> children = new ArrayList<Property>();
        children.addAll(this.attributes);
        return children.toArray(new Property[children.size()]);
    }

    @Override
    public void valuePropertyChanged(Property property) {
        int index = attributes.indexOf(property);
        attributes.get(index).setValueProperty(property.getValueProperty());
        ((Declaration) getParent()).valuePropertyChanged(this);
    }

    @Override
    public void valuePropertyChanged(Property propertyEdition, Property property) {
    }
}
