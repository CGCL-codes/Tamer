package net.sf.kpex.parser;

import net.sf.kpex.prolog.Const;
import net.sf.kpex.prolog.Fun;
import net.sf.kpex.prolog.Int;
import net.sf.kpex.prolog.Var;

class VarToken extends Fun {

    public VarToken(Var X, Const C, Int I) {
        super("varToken", 3);
        args[0] = X;
        args[1] = C;
        args[2] = I;
    }
}
