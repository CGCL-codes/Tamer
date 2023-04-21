package net.sourceforge.kas.cTree.cCombine.cStrich;

import java.util.HashMap;
import net.sourceforge.kas.cTree.CElement;
import net.sourceforge.kas.cTree.CFences;
import net.sourceforge.kas.cTree.CType;
import net.sourceforge.kas.cTree.adapter.C_Changer;
import net.sourceforge.kas.cTree.adapter.C_Event;
import net.sourceforge.kas.cTree.adapter.C_No;
import net.sourceforge.kas.cTree.cCombine.CC_Base;

public class CC_StrichFencedPFences extends CC_Base {

    protected HashMap<CType, CC_Base> op2Combiner;

    @Override
    public HashMap<CType, CC_Base> getOp2Comb() {
        if (this.op2Combiner == null) {
            this.op2Combiner = new HashMap<CType, CC_Base>();
            this.op2Combiner.put(CType.MINROW, new CC_StrichFencedPlusFencedMin());
            this.op2Combiner.put(CType.PLUSTERM, new CC_StrichFencedPlusFencedPlus());
        }
        return this.op2Combiner;
    }

    @Override
    public C_Changer getChanger(final C_Event e) {
        if (e != null && e.getFirst() != null && e.getFirst().hasNextC()) {
            this.setEvent(e);
            System.out.println("Try to combine FencesP Fences");
            final CElement cE2 = this.getSec();
            final CType cType = ((CFences) cE2).getInnen().getCType();
            if (this.getOp2Comb().containsKey(cType)) {
                return this.getOp2Comb().get(cType).getChanger(e);
            }
        }
        return new C_No(e);
    }
}
