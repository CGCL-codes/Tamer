package monet.editors.model.editions.properties;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import monet.editors.model.editions.PropertyEditionField;
import monet.editors.model.propertiesDeclarations.Property;
import monet.editors.model.propertiesDeclarations.PropertySimple;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class PropertyRange extends Property {

    public PropertyRange(PropertyEditionField parent) {
        super(parent);
        this.nameProperty = "range";
        this.valueProperty = "";
        if (this.attributes == null) this.attributes = new ArrayList<Property>();
        this.attributes.add(new PropertySimple(this, "min", "0"));
        this.attributes.add(new PropertySimple(this, "max", "100"));
    }

    public PropertyRange(PropertyEditionField parent, Node decl) {
        super(parent);
        this.nameProperty = "range";
        this.valueProperty = "";
        if (this.attributes == null) this.attributes = new ArrayList<Property>();
        NamedNodeMap attributes = decl.getAttributes();
        Node attr = attributes.getNamedItem("min");
        if (attr != null) this.attributes.add(new PropertySimple(this, attr.getNodeName(), attr.getNodeValue())); else this.attributes.add(new PropertySimple(this, "min", "0"));
        attr = attributes.getNamedItem("max");
        if (attr != null) this.attributes.add(new PropertySimple(this, attr.getNodeName(), attr.getNodeValue())); else this.attributes.add(new PropertySimple(this, "max", "100"));
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

    public void setValueProperty(String value) {
        if (valueProperty.equals(value)) return;
        valueProperty = value;
        ((PropertyEditionField) getParent()).valuePropertyChanged(this);
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
