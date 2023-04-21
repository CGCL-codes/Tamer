package org.nakedobjects.viewer.nuthatch.field;

import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectField;
import org.nakedobjects.object.OneToManyAssociation;
import org.nakedobjects.object.control.Consent;
import org.nakedobjects.viewer.nuthatch.ContextTree;
import org.nakedobjects.viewer.nuthatch.View;

public class AddToFieldContext extends FieldContext {

    public AddToFieldContext(final String name, final NakedObject object, final NakedObjectField field) {
        super(name, object, field);
    }

    public String getPrompt() {
        return name + " (add to)";
    }

    void addReferenceToCollection(final ContextTree contextTree, View view, NakedObject associate) {
        OneToManyAssociation oneToManyAssociation = ((OneToManyAssociation) field);
        Consent consent = oneToManyAssociation.isValidToAdd(object, associate);
        if (consent.isVetoed()) {
            view.error("Can't add reference: " + consent.getReason());
        } else {
            oneToManyAssociation.addElement(object, associate);
            contextTree.removeFrom(this);
        }
    }
}
