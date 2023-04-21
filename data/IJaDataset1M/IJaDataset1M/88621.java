package org.isi.monet.modelling.editor.model.properties.documents;

import org.isi.monet.modelling.editor.model.Constants;
import org.isi.monet.modelling.editor.model.declarations.Declaration;
import org.isi.monet.modelling.editor.model.properties.Property;

public class Certificate extends Property {

    public Certificate(Declaration parent) {
        super(parent);
        this.typeProperty = Constants.PROPERTY_CERTIFICATE;
        this.valueProperty = "";
    }

    public Certificate(Declaration parent, String value) {
        super(parent);
        this.typeProperty = Constants.PROPERTY_CERTIFICATE;
        this.valueProperty = value;
    }
}
