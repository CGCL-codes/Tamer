package net.sf.kpex.gui.builtins;

import java.awt.Label;
import net.sf.kpex.prolog.FunBuiltin;
import net.sf.kpex.prolog.JavaObject;
import net.sf.kpex.prolog.Prog;

@Deprecated
public class SetLabel extends FunBuiltin {

    public SetLabel() {
        super("set_label", 2);
    }

    @Override
    public int exec(Prog p) {
        JavaObject wrapper = (JavaObject) getArg(0);
        Label L = (Label) wrapper.toObject();
        L.setText(getArg(1).toUnquoted());
        return 1;
    }
}
