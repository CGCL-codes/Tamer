package at.ac.tuwien.infosys.www.pixy.analysis.literal.tf;

import at.ac.tuwien.infosys.www.pixy.analysis.LatticeElement;
import at.ac.tuwien.infosys.www.pixy.analysis.TransferFunction;
import at.ac.tuwien.infosys.www.pixy.analysis.inter.Context;
import at.ac.tuwien.infosys.www.pixy.analysis.literal.LiteralLatticeElement;
import at.ac.tuwien.infosys.www.pixy.conversion.Literal;
import at.ac.tuwien.infosys.www.pixy.conversion.nodes.CfgNodeCallRet;

public class LiteralTfCallRetUnknown extends TransferFunction {

    private CfgNodeCallRet retNode;

    public LiteralTfCallRetUnknown(CfgNodeCallRet retNode) {
        this.retNode = retNode;
    }

    public LatticeElement transfer(LatticeElement inX, Context context) {
        LiteralLatticeElement in = (LiteralLatticeElement) inX;
        LiteralLatticeElement out = new LiteralLatticeElement(in);
        out.handleReturnValueUnknown(this.retNode.getTempVar());
        return out;
    }

    public LatticeElement transfer(LatticeElement inX) {
        throw new RuntimeException("SNH");
    }
}
