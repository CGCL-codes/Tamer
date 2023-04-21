package org.nakedobjects.noa.reflect.checks;

import org.nakedobjects.noa.adapter.Naked;
import org.nakedobjects.noa.annotations.DisabledAnnotation;
import org.nakedobjects.noa.reflect.OneToManyAssociationInstance;

/**
 * Checks that the {@link OneToManyAssociationInstance} is not annotated
 * with the <tt>@Disabled</tt> annotation (or equivalent).
 * 
 */
public class CheckOneToManyAssociationInstanceNotDisabledDeclaratively extends AbstractCheckOneToManyAssociationInstance {

    public CheckOneToManyAssociationInstanceNotDisabledDeclaratively(OneToManyAssociationInstance oneToManyAssociationInstance) {
        super(oneToManyAssociationInstance);
    }

    public String check(Naked[] values) {
        return hasAnnotation(getOneToManyAssociationInstance(), DisabledAnnotation.class) ? "Disabled" : null;
    }
}
