package org.isi.monet.modelling.editor.model.properties.editions.properties;

import org.isi.monet.modelling.editor.model.Constants;
import org.isi.monet.modelling.editor.model.properties.Property;

public class Showcode extends Property {

    public Showcode(Property parent, String value) {
        super(parent);
        this.typeProperty = Constants.PROPERTY_SHOWCODE;
        this.valueProperty = value;
    }

    public Showcode(Property parent) {
        super(parent);
        this.typeProperty = Constants.PROPERTY_SHOWCODE;
        this.valueProperty = "false";
    }
}
