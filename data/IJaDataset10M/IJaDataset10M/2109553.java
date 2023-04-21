package nts.typo;

import nts.node.Box;
import nts.builder.Builder;
import nts.command.Token;
import nts.command.Prim;

public class AnyUnCopyPrim extends BuilderPrim {

    protected final SetBoxPrim reg;

    public AnyUnCopyPrim(String name, SetBoxPrim reg) {
        super(name);
        this.reg = reg;
    }

    protected Box getBox(int idx) {
        return reg.get(idx);
    }

    protected void finishBox(int idx) {
    }

    public final Action NORMAL = new Action() {

        public void exec(Builder bld, Token src) {
            int idx = Prim.scanRegisterCode();
            Box box = getBox(idx);
            if (!box.isVoid()) {
                if (bld.unBox(box)) finishBox(idx); else error("IncompatibleUnbox");
            }
        }
    };
}
