package nts.node;

import nts.base.Dimen;
import nts.io.Log;
import nts.io.CntxLog;

public class AccKernNode extends IntHKernNode {

    public AccKernNode(Dimen kern) {
        super(kern);
    }

    public void addOn(Log log, CntxLog cntx) {
        log.addEsc("kern ").add(kern.toString()).add(" (for accent)");
    }

    public String toString() {
        return "AccKern(" + kern + ')';
    }
}
