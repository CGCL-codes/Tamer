package net.sf.kpex.gui.builtins;

import net.sf.kpex.gui.JinniCanvas;
import net.sf.kpex.prolog.Fun;
import net.sf.kpex.prolog.FunBuiltin;
import net.sf.kpex.prolog.JavaObject;
import net.sf.kpex.prolog.Prog;

/**
 * Examples of Jinni GUI components - add more !
 */
@Deprecated
public class Draw extends FunBuiltin {

    public Draw() {
        super("draw", 2);
    }

    @Override
    public int exec(Prog p) {
        JinniCanvas C = (JinniCanvas) ((JavaObject) getArg(0)).toObject();
        Fun D = (Fun) getArg(1);
        C.to_draw(D);
        return 1;
    }
}
