package net.sf.signs.gates;

import net.sf.signs.Gate;
import net.sf.signs.NetList;
import net.sf.signs.intermediate.IntermediateObject;

@SuppressWarnings("serial")
public class LD1Arch extends BuiltinGate {

    public LD1Arch() {
        super("LD1", null);
    }

    @Override
    public Gate getInstance(NetList parent_, IntermediateObject src_) {
        return new LD1(parent_, src_);
    }
}
