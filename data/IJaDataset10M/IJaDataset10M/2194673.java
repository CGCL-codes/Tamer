package org.argouml.notation2;

import java.beans.PropertyChangeListener;
import org.argouml.model.Model;

class UmlNotationLanguage implements NotationLanguage {

    public String getName() {
        return "UML";
    }

    public NotationText createNotationText(NotatedItem item) {
        final NotationText nt;
        if (Model.getCoreHelper().isSubType(Model.getMetaTypes().getStereotype(), item.getMetaType())) {
            nt = new StereotypeUmlNotation(item);
        } else {
            nt = new NameUmlNotation(item);
        }
        Model.getPump().addModelEventListener((PropertyChangeListener) nt, item.getOwner());
        ((PropertyChangeListener) nt).propertyChange(null);
        return nt;
    }
}
