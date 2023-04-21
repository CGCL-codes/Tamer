package org.isi.monet.core.model;

import java.util.Iterator;
import java.util.List;
import org.jdom.Element;

public class FactDefinition extends ModelDefinition {

    public FactDefinition() {
        super();
    }

    @SuppressWarnings("unchecked")
    public Boolean unserializeFromXML(Element oElement) {
        List<Element> lLabel;
        Iterator<Element> oIterator;
        if (oElement.getAttribute("code") != null) this.code = oElement.getAttributeValue("code");
        lLabel = oElement.getChildren("label");
        oIterator = lLabel.iterator();
        this.hmLabels.clear();
        while (oIterator.hasNext()) {
            Element oLabel = oIterator.next();
            this.hmLabels.put(oLabel.getAttributeValue("language"), oLabel.getText());
        }
        return true;
    }
}
