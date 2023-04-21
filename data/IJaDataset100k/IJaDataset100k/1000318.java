package at.ac.tuwien.infosys.www.pixy.analysis.literal.tf;

import java.util.*;
import at.ac.tuwien.infosys.www.pixy.analysis.LatticeElement;
import at.ac.tuwien.infosys.www.pixy.analysis.TransferFunction;
import at.ac.tuwien.infosys.www.pixy.analysis.literal.LiteralLatticeElement;
import at.ac.tuwien.infosys.www.pixy.conversion.TacPlace;
import at.ac.tuwien.infosys.www.pixy.conversion.Variable;

public class LiteralTfAssignSimple extends TransferFunction {

    private Variable left;

    private TacPlace right;

    private Set mustAliases;

    private Set mayAliases;

    public LiteralTfAssignSimple(TacPlace left, TacPlace right, Set mustAliases, Set mayAliases) {
        this.left = (Variable) left;
        this.right = right;
        this.mustAliases = mustAliases;
        this.mayAliases = mayAliases;
    }

    public LatticeElement transfer(LatticeElement inX) {
        LiteralLatticeElement in = (LiteralLatticeElement) inX;
        LiteralLatticeElement out = new LiteralLatticeElement(in);
        out.assignSimple(left, right, mustAliases, mayAliases);
        return out;
    }
}
