package net.sourceforge.kas.cTree.cCombine.cStrich;

import java.util.HashMap;
import net.sourceforge.kas.cTree.CType;
import net.sourceforge.kas.cTree.cCombine.CC_Base;
import net.sourceforge.kas.cTree.cCombine.CCombiner1;

public class CCombiner1StrichTR extends CCombiner1 {

    public CCombiner1StrichTR() {
        super();
    }

    @Override
    public HashMap<CType, CC_Base> getOp2Comb() {
        if (this.op2Combiner == null) {
            this.op2Combiner = super.getOp2Comb();
            this.op2Combiner.put(CType.IDENT, new CC_StrichTRIdent());
            this.op2Combiner.put(CType.TIMESROW, new CC_StrichTRTR());
            this.op2Combiner.put(CType.POT, new CC_StrichTRPot());
        }
        return this.op2Combiner;
    }
}
