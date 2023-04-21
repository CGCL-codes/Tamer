package org.jul.dsm.representation;

import org.jul.dsm.*;
import org.jul.dsm.constructor.PrimitiveValueConstructor;

public class PrimitiveValueRepresentation extends Representation {

    @Override
    public Change clone(PatchConstructor patchConstructor) {
        return null;
    }

    @Override
    public Change findChanges(PatchConstructor patchConstructor) {
        return null;
    }

    @Override
    public void apply(PatchApplicator patchApplicator, Change change) {
        raiseIncompatibleChange(change);
    }

    @Override
    public String toString() {
        return super.toString() + " [" + getReference() + "]";
    }

    public PrimitiveValueRepresentation(SharedMemory memory, Representation parentRepresentation, long id, Object reference, boolean useWeakReference) {
        super(memory, parentRepresentation, id, reference);
        setConstructor(new PrimitiveValueConstructor(getConstructorFactory(), getReference()));
    }
}
