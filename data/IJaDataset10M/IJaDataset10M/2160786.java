package org.jpox.samples.models.cyclic_nonnullable;

/**
 *
 */
public class NullInverseEntity2 {

    private NullInverseEntity inverse = null;

    private NullInverseEntity3 forward = null;

    private CompoundType values = null;

    public NullInverseEntity getInverse() {
        return inverse;
    }

    public void setInverse(NullInverseEntity inverse) {
        this.inverse = inverse;
    }

    public NullInverseEntity3 getForward() {
        return forward;
    }

    public void setForward(NullInverseEntity3 forward) {
        this.forward = forward;
    }

    public CompoundType getValues() {
        return values;
    }

    public void setValues(CompoundType values) {
        this.values = values;
    }
}
