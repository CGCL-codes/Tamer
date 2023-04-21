package net.sf.signs.gates;

import net.sf.signs.*;
import net.sf.signs.intermediate.IntermediateObject;

@SuppressWarnings("serial")
public class Nand2 extends LogicGate implements IConstantPropagation {

    private static final String nand2_str = new String("NAND_2");

    public String getClassName() {
        return nand2_str;
    }

    public Nand2(NetList parent_, IntermediateObject src_) {
        super(parent_, null, src_);
        a = new PortBit(this, PortBit.a_str, PortBit.DIR_IN, null);
        b = new PortBit(this, PortBit.b_str, PortBit.DIR_IN, null);
        z = new PortBit(this, PortBit.z_str, PortBit.DIR_OUT, null);
    }

    public Object clone() {
        return (Object) new Nand2(this.getParent(), getSource());
    }

    public boolean propagateConstant(PortBit in_, char v_) throws SignsException {
        if (v_ == '1') {
            NetList nl = getParent();
            SignalBit sa;
            if (in_ == a) {
                sa = b.getSignalBit();
            } else {
                sa = a.getSignalBit();
            }
            SignalBit nssb = getParent().placeSimpleLogic(new Not1(nl, getInstanceName() + "_INVS", null), sa, null, null);
            SignalBit sz = z.getSignalBit();
            nl.sigJoin(nssb, sz, null);
        } else if (v_ == '0') {
            NetList nl = getParent();
            SignalBit sone = nl.getOne();
            nl.sigJoin(sone, z.getSignalBit(), null);
        }
        return true;
    }
}
