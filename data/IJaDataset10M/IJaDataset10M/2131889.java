package org.nakedobjects.plugins.dnd.configurable;

import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.runtime.userprofile.Options;
import org.nakedobjects.runtime.userprofile.OptionsAware;

public class NewObjectField implements OptionsAware {

    private final NakedObjectAssociation field;

    public NewObjectField(NakedObjectAssociation field) {
        this.field = field;
    }

    public boolean includeLabel() {
        return true;
    }

    public NakedObjectAssociation getField() {
        return field;
    }

    public void loadOptions(Options viewOptions) {
    }

    public void saveOptions(Options viewOptions) {
        viewOptions.addOption("field", field.getId());
    }
}
