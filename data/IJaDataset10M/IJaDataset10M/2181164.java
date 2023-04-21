package at.ac.tuwien.infosys.www.pixy.analysis.type.tf;

import at.ac.tuwien.infosys.www.pixy.analysis.LatticeElement;
import at.ac.tuwien.infosys.www.pixy.analysis.TransferFunction;
import at.ac.tuwien.infosys.www.pixy.analysis.type.TypeLatticeElement;
import at.ac.tuwien.infosys.www.pixy.conversion.TacPlace;
import at.ac.tuwien.infosys.www.pixy.conversion.Variable;

public class TypeTfAssignRef extends TransferFunction {

    private Variable left;

    private TacPlace right;

    public TypeTfAssignRef(Variable left, TacPlace right) {
        this.left = left;
        this.right = right;
    }

    public LatticeElement transfer(LatticeElement inX) {
        TypeLatticeElement in = (TypeLatticeElement) inX;
        TypeLatticeElement out = new TypeLatticeElement(in);
        out.assign(left, right);
        return out;
    }
}
