package net.sf.signs.gates;

import net.sf.signs.Gate;
import net.sf.signs.NetList;
import net.sf.signs.intermediate.IntermediateObject;

@SuppressWarnings("serial")
public class FDS2LArch extends BuiltinGate {

    public FDS2LArch() {
        super("FDS2L", null);
    }

    @Override
    public Gate getInstance(NetList parent_, IntermediateObject src_) {
        return new FDS2L(parent_, src_);
    }
}
