package net.sourceforge.kas.cTree.cCombine.cStrich;

import net.sourceforge.kas.cTree.CElement;
import net.sourceforge.kas.cTree.CFrac;
import net.sourceforge.kas.cTree.CMinTerm;
import net.sourceforge.kas.cTree.CMixedNumber;
import net.sourceforge.kas.cTree.CNum;
import net.sourceforge.kas.cTree.CRolle;
import net.sourceforge.kas.cTree.adapter.C_Changer;
import net.sourceforge.kas.cTree.adapter.C_Event;
import net.sourceforge.kas.cTree.adapter.C_No;
import net.sourceforge.kas.cTree.cCombine.CC_Base;
import net.sourceforge.kas.cTree.cDefence.CD_Event;

public class CC_StrichMinrowFrac extends CC_Base {

    private CC_StrichMinNumFrac cmnf;

    private CC_StrichMinFracFrac cmff;

    private CC_StrichMinMixedFrac cmmf;

    @Override
    public C_Changer getChanger(final C_Event e) {
        System.out.println("Try to combine Minrow Frac");
        if ((((CMinTerm) e.getFirst()).getValue() instanceof CNum)) {
            return this.getCMinNumFrac().getChanger(e);
        } else if ((((CMinTerm) e.getFirst()).getValue() instanceof CFrac)) {
            return this.getCMinFracFrac().getChanger(e);
        } else if ((((CMinTerm) e.getFirst()).getValue() instanceof CMixedNumber)) {
            return this.getCMinMixedFrac().getChanger(e);
        }
        return new C_No(e);
    }

    protected CC_StrichMinNumFrac getCMinNumFrac() {
        if (this.cmnf == null) {
            this.cmnf = new CC_StrichMinNumFrac();
        }
        return this.cmnf;
    }

    protected CC_StrichMinFracFrac getCMinFracFrac() {
        if (this.cmff == null) {
            this.cmff = new CC_StrichMinFracFrac();
        }
        return this.cmff;
    }

    protected CC_StrichMinMixedFrac getCMinMixedFrac() {
        if (this.cmmf == null) {
            this.cmmf = new CC_StrichMinMixedFrac();
        }
        return this.cmmf;
    }

    @Override
    public boolean canDo() {
        System.out.println(" Repell Add Minrow and Frac?");
        if ((((CMinTerm) this.getFirst()).getValue() instanceof CNum)) {
            return true;
        } else if ((((CMinTerm) this.getFirst()).getValue() instanceof CFrac)) {
            return true;
        } else if ((((CMinTerm) this.getFirst()).getValue() instanceof CMixedNumber)) {
            return true;
        }
        return false;
    }

    @Override
    protected CElement createComb(final CElement parent, final CElement cE1, final CElement cE2, CD_Event cDEvent) {
        final int wertE = Integer.parseInt(((CNum) ((CMinTerm) cE1).getValue()).getText());
        final int wertZ = Integer.parseInt(cE2.getText());
        CElement newChild = null;
        System.out.println("// Minterm in Summe muss erster Summand sein");
        if (!cE2.hasExtMinus() && (wertE <= wertZ)) {
            newChild = CNum.createNum(parent, "" + (wertZ - wertE));
            newChild.setCRolle(CRolle.SUMMAND1);
        } else {
            System.out.println("// Minterm in Summe muss erster Summand sein");
            newChild = cE1.cloneCElement(false);
            int value = wertE - wertZ;
            if (cE2.hasExtMinus()) {
                value = wertE + wertZ;
            }
            ((CMinTerm) newChild).getValue().setText("" + value);
        }
        return newChild;
    }
}
