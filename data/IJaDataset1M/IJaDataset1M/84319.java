package net.sourceforge.kas.cTree.cCombine;

import java.util.HashMap;
import net.sourceforge.kas.cTree.CType;

public class CCombiner1PotFences extends CCombiner1 {

    public CCombiner1PotFences() {
        super();
    }

    @Override
    public HashMap<CType, CC_Base> getOp2Comb() {
        if (this.op2Combiner == null) {
            this.op2Combiner = super.getOp2Comb();
            this.op2Combiner.put(CType.NUM, new CC_PotFencesNum());
        }
        return this.op2Combiner;
    }
}
