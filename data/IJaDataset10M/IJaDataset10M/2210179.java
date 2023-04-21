package org.sodeja.generator.uml.argouml;

import org.sodeja.generator.uml.UmlModel;
import org.xml.sax.Attributes;

public class UmlModelParserStrategy extends XmiParserStrategy {

    @Override
    public void begin(XmiParser context, Attributes attributes) {
        UmlModel model = new UmlModel();
        fill(model, attributes);
        context.push(model);
    }
}
