package nts.typo;

import nts.node.Box;
import nts.command.Prim;

public class CopyPrim extends FetchBoxPrim {

    protected final SetBoxPrim reg;

    public CopyPrim(String name, SetBoxPrim reg) {
        super(name);
        this.reg = reg;
    }

    protected Box getBox(int idx) {
        return reg.get(idx);
    }

    public Box getBoxValue() {
        return getBox(Prim.scanRegisterCode());
    }
}
