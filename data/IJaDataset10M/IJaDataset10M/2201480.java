package monet.editors.model.propertiesDeclarations;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class PropertySource extends Property {

    public PropertySource(PropertyLink propertyLink) {
        super(propertyLink);
        this.nameProperty = "source";
        this.valueProperty = "";
        if (attributes == null) attributes = new ArrayList<Property>();
        attributes.add(new PropertySimple(this, "value", ""));
    }

    public PropertySource(PropertyLink parent, Node declaration) {
        super(parent);
        NamedNodeMap attr = declaration.getAttributes();
        if (attributes == null) attributes = new ArrayList<Property>();
        Node valueAttr = attr.getNamedItem("value");
        if (valueAttr != null) attributes.add(new PropertySimple(this, "value", valueAttr.getNodeValue())); else attributes.add(new PropertySimple(this, "value", ""));
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
        ((Property) getParent()).valuePropertyChanged(this, property);
    }

    @Override
    public void valuePropertyChanged(Property propertyEdition, Property property) {
    }
}
