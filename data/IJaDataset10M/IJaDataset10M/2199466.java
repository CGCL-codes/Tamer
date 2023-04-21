package net.sf.kpex.parser;

import net.sf.kpex.prolog.Const;
import net.sf.kpex.prolog.Fun;

class ConstToken extends Fun {

    public ConstToken(Const c) {
        super("constToken", c);
    }
}
