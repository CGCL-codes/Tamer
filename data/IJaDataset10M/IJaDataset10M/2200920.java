package nts.noad;

import nts.base.Dimen;

public class MathOnNode extends MathNode {

    public MathOnNode(Dimen kern) {
        super(kern);
    }

    public boolean forbidsSpaceBreaking() {
        return true;
    }

    protected boolean isOn() {
        return true;
    }

    protected MathNode resizedCopy(Dimen kern) {
        return new MathOnNode(kern);
    }
}
