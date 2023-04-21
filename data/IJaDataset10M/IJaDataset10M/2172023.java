package com.ibm.wala.ipa.slicer;

import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ssa.SSAPhiInstruction;

/**
 * identifier of a phi instruction
 */
public class PhiStatement extends Statement {

    private final SSAPhiInstruction phi;

    public PhiStatement(CGNode node, SSAPhiInstruction phi) {
        super(node);
        this.phi = phi;
    }

    @Override
    public Kind getKind() {
        return Kind.PHI;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass().equals(obj.getClass())) {
            PhiStatement other = (PhiStatement) obj;
            return getNode().equals(other.getNode()) && phi.getDef() == other.phi.getDef();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return 3691 * phi.getDef() + getNode().hashCode();
    }

    @Override
    public String toString() {
        return "PHI " + getNode() + ":" + phi;
    }

    public SSAPhiInstruction getPhi() {
        return phi;
    }
}
