package at.ac.tuwien.infosys.www.pixy.analysis.type.tf;

import at.ac.tuwien.infosys.www.pixy.analysis.LatticeElement;
import at.ac.tuwien.infosys.www.pixy.analysis.TransferFunction;
import at.ac.tuwien.infosys.www.pixy.analysis.type.TypeLatticeElement;
import at.ac.tuwien.infosys.www.pixy.conversion.Variable;

public class TypeTfAssignBinary extends TransferFunction {

    private Variable left;

    public TypeTfAssignBinary(Variable left) {
        this.left = left;
    }

    public LatticeElement transfer(LatticeElement inX) {
        TypeLatticeElement in = (TypeLatticeElement) inX;
        TypeLatticeElement out = new TypeLatticeElement(in);
        out.assignBinary(left);
        return out;
    }
}
