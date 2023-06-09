package jp.ac.kobe_u.cs.prolog.builtin;

import jp.ac.kobe_u.cs.prolog.lang.*;

/**
 <code>otherwise/0</code> defined in builtins.pl<br>
 @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 @author Douglas R. Miles (dmiles@users.sourceforge.net) for Object(s) 
 @version 1.0-dmiles
*/
class PRED_otherwise_0 extends PredicateBase {

    public PRED_otherwise_0(Predicate cont) {
        this.cont = cont;
    }

    public PRED_otherwise_0() {
    }

    public void setArgument(Object[] args, Predicate cont) {
        this.cont = cont;
    }

    public int arity() {
        return 0;
    }

    public String nameUQ() {
        return "otherwise";
    }

    public void sArg(int i0, Object val) {
        switch(i0) {
            default:
                newIndexOutOfBoundsException("setarg", i0, val);
        }
    }

    public Object gArg(int i0) {
        switch(i0) {
            default:
                return newIndexOutOfBoundsException("getarg", i0, null);
        }
    }

    public String toPrologString(java.util.Collection newParam) {
        return "'otherwise";
    }

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        engine.setB0();
        return exit(engine, cont);
    }
}
