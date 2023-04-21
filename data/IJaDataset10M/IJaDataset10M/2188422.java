package nts.noad;

import nts.base.Dimen;

public class MathOffNode extends MathNode {

    public MathOffNode(Dimen kern) {
        super(kern);
    }

    public boolean allowsSpaceBreaking() {
        return true;
    }

    protected boolean isOn() {
        return false;
    }

    protected MathNode resizedCopy(Dimen kern) {
        return new MathOffNode(kern);
    }
}
