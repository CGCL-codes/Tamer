package org.isi.monet.modelling.editor.model.properties.editions.properties;

import org.isi.monet.modelling.editor.model.Constants;
import org.isi.monet.modelling.editor.model.properties.Property;

public class Label extends Property {

    public Label(Property parent, String value) {
        super(parent);
        this.typeProperty = Constants.PROPERTY_LABEL;
        this.valueProperty = value;
    }

    public Label(Property parent) {
        super(parent);
        this.typeProperty = Constants.PROPERTY_LABEL;
        this.valueProperty = "";
    }
}
