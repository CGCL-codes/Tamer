package org.nakedobjects.noa.reflect.checks;

import org.nakedobjects.noa.adapter.Naked;
import org.nakedobjects.noa.reflect.ValueAssociationInstance;

/**
 * Checks that the {@link ValueAssociationInstance} is not hidden
 * imperatively (as per the <tt>hideXxx</tt> method, or equivalent).
 * 
 */
public class CheckValueAssociationInstanceNotHiddenImperatively extends AbstractCheckValueAssociationInstance {

    public CheckValueAssociationInstanceNotHiddenImperatively(ValueAssociationInstance valueAssociationInstance) {
        super(valueAssociationInstance);
    }

    public String check(Naked[] values) {
        ValueAssociationInstance vai = getValueAssociationInstance();
        return vai.getValueAssociation().isVisible(vai.getNakedObject()) ? null : "Hidden";
    }
}
