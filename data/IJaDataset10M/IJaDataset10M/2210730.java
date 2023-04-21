package net.sourceforge.kas.cTree.cCombine.cStrich;

import net.sourceforge.kas.cTree.CElement;
import net.sourceforge.kas.cTree.CFrac;
import net.sourceforge.kas.cTree.CMinTerm;
import net.sourceforge.kas.cTree.CType;
import net.sourceforge.kas.cTree.cCombine.CC_Base;
import net.sourceforge.kas.cTree.cDefence.CD_Event;

public class CC_StrichMinFracFrac extends CC_Base {

    @Override
    public boolean canDo() {
        System.out.println("Repell minrow frac frac?");
        CElement innen = ((CMinTerm) this.getFirst()).getValue();
        return !(((CFrac) innen).isUndefined() || ((CFrac) this.getSec()).isUndefined());
    }

    @Override
    protected CElement createComb(final CElement parent, final CElement cE1, final CElement cE2, final CD_Event cDEvent) {
        System.out.println("Strich MinFrac Frac");
        final CFrac inCE1 = ((CFrac) cE1.getFirstChild().cloneCElement(false));
        final CFrac fCE2 = (CFrac) cE2;
        if (inCE1.hasNumberValue() && inCE1.getZaehler().getCType().equals(CType.NUM) && inCE1.getNenner().getCType().equals(CType.NUM) && fCE2.hasNumberValue() && fCE2.getZaehler().getCType().equals(CType.NUM) && fCE2.getNenner().getCType().equals(CType.NUM)) {
            return CCombinerTStrich.createNumberCombination(parent, cE1, cE2, cDEvent);
        } else {
            return CCombiner1StrichMinrow.combineMinExpExp(inCE1, cE2, cDEvent);
        }
    }
}
