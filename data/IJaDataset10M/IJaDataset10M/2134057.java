package net.sf.signs.gates;

import net.sf.signs.Gate;
import net.sf.signs.NetList;
import net.sf.signs.intermediate.IntermediateObject;

@SuppressWarnings("serial")
public class FJK2SArch extends BuiltinGate {

    public FJK2SArch() {
        super("FJK2S", null);
    }

    @Override
    public Gate getInstance(NetList parent_, IntermediateObject src_) {
        return new FJK2S(parent_, src_);
    }
}
