package org.aplikator.server.descriptor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Form extends ServerDescriptorBase {

    private Widget layout;

    private List<Property<? extends Serializable>> properties = new ArrayList<Property<? extends Serializable>>();

    public Form() {
        super(null);
    }

    public List<Property<? extends Serializable>> getProperties() {
        return properties;
    }

    public Form addProperty(Property<? extends Serializable> property) {
        properties.add(property);
        return this;
    }

    public Widget getLayout() {
        return layout;
    }

    public Form setLayout(Widget layout) {
        this.layout = layout;
        this.layout.registerProperties(this);
        return this;
    }

    public static Form form(Widget layout) {
        Form retval = new Form();
        return retval.setLayout(layout);
    }
}
