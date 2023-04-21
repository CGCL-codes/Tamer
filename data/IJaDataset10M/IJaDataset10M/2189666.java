package net.sf.kpex.builtins;

import net.sf.kpex.DataBase;
import net.sf.kpex.prolog.Const;
import net.sf.kpex.prolog.FunBuiltin;
import net.sf.kpex.prolog.Prog;

/**
 * reconsults a file of clauses while overwriting old predicate definitions
 * 
 * @see Consult
 */
public class Reconsult extends FunBuiltin {

    public Reconsult() {
        super("reconsult", 1);
    }

    @Override
    public int exec(Prog p) {
        String f = ((Const) getArg(0)).getName();
        return DataBase.fromFile(f, p.getBuiltins()) ? 1 : 0;
    }
}
