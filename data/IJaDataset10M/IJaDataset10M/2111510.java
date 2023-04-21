package net.sf.signs.gates;

import net.sf.signs.Gate;
import net.sf.signs.NetList;
import net.sf.signs.intermediate.IntermediateObject;

@SuppressWarnings("serial")
public class FJK2Arch extends BuiltinGate {

    public FJK2Arch() {
        super("FJK2", null);
    }

    @Override
    public Gate getInstance(NetList parent_, IntermediateObject src_) {
        return new FJK2(parent_, src_);
    }
}
